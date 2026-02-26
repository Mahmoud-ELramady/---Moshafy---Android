package com.elramady.moshafy.mushaf.presentation

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.elramady.moshafy.Api.SwarClient
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.ActivityMushafReaderBinding
import com.elramady.moshafy.mushaf.config.MushafConfig
import com.elramady.moshafy.mushaf.config.MushafSurahPageMapping
import com.elramady.moshafy.mushaf.data.local.MushafDatabase
import com.elramady.moshafy.mushaf.data.repository.MushafImageRepository
import com.elramady.moshafy.room.DataBase
import com.elramady.moshafy.vo.SurahsNames.Data
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MushafReaderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMushafReaderBinding
    private lateinit var viewModel: MushafReaderViewModel
    private lateinit var pageLoader: MushafPageLoader
    private lateinit var adapter: MushafPageAdapter

    private val roomDisposables = CompositeDisposable()
    private val networkDisposables = io.reactivex.rxjava3.disposables.CompositeDisposable()
    private var isFetchingSurahs = false
    private var surahsLoadingDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        binding = ActivityMushafReaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = MushafDatabase.getInstance(this).mushafDao()
        val imageRepository = MushafImageRepository(this, dao)
        viewModel = ViewModelProvider(this, MushafViewModelFactory(application))[MushafReaderViewModel::class.java]
        pageLoader = MushafPageLoader(
            imageRepository = imageRepository,
            placeholderResId = R.drawable.mushaf_page_placeholder
        )

        setupToolbar()
        setupViewPager()
        setupPageIndicator()
        setupButtons()
        setupSettingsToggle()
        observeDownloadProgress()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val titleView = TextView(this).apply {
            text = getString(R.string.moshaf)
            setTextColor(ContextCompat.getColor(this@MushafReaderActivity, android.R.color.white))
            textSize = 20f
            gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
            setPadding(0, 0, resources.getDimensionPixelSize(R.dimen._10dp), 0)
            ResourcesCompat.getFont(this@MushafReaderActivity, R.font.font_app)?.let { typeface ->
                setTypeface(typeface)
            }
        }
        val params = androidx.appcompat.widget.Toolbar.LayoutParams(
            androidx.appcompat.widget.Toolbar.LayoutParams.MATCH_PARENT,
            androidx.appcompat.widget.Toolbar.LayoutParams.MATCH_PARENT
        ).apply {
            gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
        }
        binding.toolbar.addView(titleView, params)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    /** ViewPager position to mushaf page number (1-based). Reversed so swipe-right = next page. */
    private fun positionToPage(position: Int): Int = MushafConfig.TOTAL_PAGES - position

    /** Mushaf page number (1-based) to ViewPager position. */
    private fun pageToPosition(pageNumber: Int): Int = MushafConfig.TOTAL_PAGES - pageNumber

    private fun setupViewPager() {
        adapter = MushafPageAdapter(pageLoader)
        binding.viewPager.adapter = adapter
        binding.viewPager.orientation = androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewPager.offscreenPageLimit = 2
        binding.viewPager.layoutDirection = View.LAYOUT_DIRECTION_LTR
        val startPage = intent.getIntExtra(EXTRA_PAGE_NUMBER, -1).let {
            if (it in 1..MushafConfig.TOTAL_PAGES) it else viewModel.getLastReadPage()
        }
        binding.viewPager.setCurrentItem(pageToPosition(startPage), false)
        updatePageIndicator(startPage)

        binding.viewPager.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val pageNumber = positionToPage(position)
                viewModel.setCurrentPage(pageNumber)
                updatePageIndicator(pageNumber)
            }
        })
    }

    private fun setupPageIndicator() {
        // Handled in setupViewPager callback
    }

    private fun updatePageIndicator(pageNumber: Int) {
        binding.tvPageIndicator.text = getString(R.string.page_indicator_format, pageNumber, MushafConfig.TOTAL_PAGES)
    }

    private fun setupButtons() {
        binding.btnPagePicker.setOnClickListener { showPagePickerDialog() }
        binding.btnBookmark.setOnClickListener { toggleBookmark() }
        binding.btnBookmarksList.setOnClickListener {
            startActivity(Intent(this, MushafBookmarksActivity::class.java))
        }
        binding.btnSurahs.setOnClickListener { showSurahsDialog() }
    }

    private fun setupSettingsToggle() {

        binding.tvMushafSettingsToggle.setOnClickListener {
            if (binding.settingMushaf.isVisible) {
               visibleMushafSettings(false)
                binding.ivCloseMushafSettings.visibility= View.GONE
            } else {
                visibleMushafSettings(true)
                binding.ivCloseMushafSettings.visibility= View.VISIBLE
            }
        }

        binding.ivCloseMushafSettings.setOnClickListener {
            if (binding.settingMushaf.isVisible) {
                visibleMushafSettings(false)
                binding.ivCloseMushafSettings.visibility= View.GONE
            } else {
                visibleMushafSettings(true)
            }
        }
    }

    private fun visibleMushafSettings(show: Boolean) {
        if (show) {
            binding.settingMushaf.alpha = 0f
            binding.settingMushaf.visibility = View.VISIBLE
            binding.settingMushaf.animate()
                .alpha(1f)
                .setDuration(200)
                .start()
        } else {
            binding.settingMushaf.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction {
                    binding.settingMushaf.visibility = View.GONE
                    binding.settingMushaf.alpha = 1f
                }
                .start()
        }
    }

    private fun showSurahsDialog() {
        if (isFetchingSurahs) return

        val db = DataBase.getInstance(this)
        val disposable = db.surahsDao.getSurahsRoom()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { surahs ->
                    if (surahs.isNotEmpty()) {
                        showSurahsDialogWithList(surahs)
                    } else {
                        fetchSurahsFromRemoteThenCacheAndShow()
                    }
                },
                { _ -> fetchSurahsFromRemoteThenCacheAndShow() }
            )

        roomDisposables.add(disposable)
    }

    private fun fetchSurahsFromRemoteThenCacheAndShow() {
        if (isFetchingSurahs) return
        isFetchingSurahs = true
        showSurahsLoading(true)

        val api = SwarClient.getSwarClient()
        val disposable = api.getSurhasNames()
            .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
            .observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    val surahs = response.data
                    cacheSurahsToRoomAndShow(surahs)
                },
                { _ ->
                    isFetchingSurahs = false
                    showSurahsLoading(false)
                    Toast.makeText(this, getString(R.string.surahs_loading_failed), Toast.LENGTH_SHORT).show()
                }
            )

        networkDisposables.add(disposable)
    }

    private fun cacheSurahsToRoomAndShow(surahs: List<Data>) {
        val db = DataBase.getInstance(this)
        val disposable = db.surahsDao.doInsert(surahs)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    isFetchingSurahs = false
                    showSurahsLoading(false)
                    showSurahsDialogWithList(surahs)
                },
                { _ ->
                    isFetchingSurahs = false
                    showSurahsLoading(false)
                    Toast.makeText(this, getString(R.string.surahs_loading_failed), Toast.LENGTH_SHORT).show()
                }
            )
        roomDisposables.add(disposable)
    }

    private fun showSurahsLoading(show: Boolean) {
        if (show) {
            if (surahsLoadingDialog?.isShowing == true) return
            val progress = android.widget.ProgressBar(this)
            surahsLoadingDialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.surahs_loading))
                .setView(progress)
                .setCancelable(true)
                .setOnCancelListener {
                    // allow cancel; next click can retry
                    isFetchingSurahs = false
                }
                .show()
        } else {
            surahsLoadingDialog?.dismiss()
            surahsLoadingDialog = null
        }
    }

    private fun showSurahsDialogWithList(surahs: List<Data>) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_mushaf_surahs, null)
        val recycler = dialogView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_surahs)
        recycler.layoutManager = LinearLayoutManager(this)
        val dialogAdapter = MushafSurahsDialogAdapter { surah ->
            val page = MushafSurahPageMapping.getPageForSurah(surah.number)
            binding.viewPager.setCurrentItem(pageToPosition(page), true)
            surahsDialog?.dismiss()
        }
        recycler.adapter = dialogAdapter
        dialogAdapter.submitList(surahs)
        surahsDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.surahs_dialog_title))
            .setView(dialogView)
            .setNegativeButton(getString(R.string.cancel)) { d, _ -> d.dismiss() }
            .show()
    }

    private var surahsDialog: AlertDialog? = null

    private fun showPagePickerDialog() {
        val currentPage = positionToPage(binding.viewPager.currentItem).coerceIn(1, MushafConfig.TOTAL_PAGES)
        val input = android.widget.EditText(this).apply {
            setHint("1 - ${MushafConfig.TOTAL_PAGES}")
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            setText(currentPage.toString())
            setPadding(48, 48, 48, 48)
        }
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.page_picker))
            .setView(input)
            .setPositiveButton(getString(R.string.sure)) { _, _ ->
                val page = input.text.toString().toIntOrNull()?.coerceIn(1, MushafConfig.TOTAL_PAGES) ?: currentPage
                binding.viewPager.setCurrentItem(pageToPosition(page), true)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun toggleBookmark() {
        val pageNumber = positionToPage(binding.viewPager.currentItem)
        viewModel.toggleBookmark(pageNumber) { isBookmarked ->
            val msg = if (isBookmarked) getString(R.string.bookmark_added) else getString(R.string.bookmark_removed)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeDownloadProgress() {
        lifecycleScope.launch {
            viewModel.downloadProgress.collectLatest { progress ->
                val isDownloading = progress.downloaded < progress.total
                binding.settingMushafDownloadSection.isVisible = isDownloading
                if (isDownloading) {
                    binding.tvSettingMushafDownloadPages.text = getString(R.string.mushaf_download_pages_format, progress.downloaded, progress.total)
                    val percent = if (progress.total > 0) progress.downloaded * 100 / progress.total else 0
                    binding.progressSettingMushafDownload.progress = percent
                    binding.tvSettingMushafDownloadPercent.text = getString(R.string.mushaf_download_percent_format, percent)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        surahsDialog?.dismiss()
        surahsDialog = null
        surahsLoadingDialog?.dismiss()
        surahsLoadingDialog = null
        roomDisposables.clear()
        networkDisposables.clear()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_PAGE_NUMBER = "extra_page_number"
    }
}
