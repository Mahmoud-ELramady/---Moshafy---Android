package com.elramady.moshafy.mushaf.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.ActivityMushafReaderBinding
import com.elramady.moshafy.mushaf.config.MushafConfig
import com.elramady.moshafy.mushaf.data.local.MushafDatabase
import com.elramady.moshafy.mushaf.data.repository.MushafImageRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MushafReaderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMushafReaderBinding
    private lateinit var viewModel: MushafReaderViewModel
    private lateinit var pageLoader: MushafPageLoader
    private lateinit var adapter: MushafPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        observeDownloadProgress()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupViewPager() {
        adapter = MushafPageAdapter(pageLoader)
        binding.viewPager.adapter = adapter
        binding.viewPager.orientation = androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewPager.offscreenPageLimit = 2

        val startPage = intent.getIntExtra(EXTRA_PAGE_NUMBER, -1).let {
            if (it in 1..MushafConfig.TOTAL_PAGES) it else viewModel.getLastReadPage()
        }
        binding.viewPager.setCurrentItem(startPage - 1, false)
        updatePageIndicator(startPage)

        binding.viewPager.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val pageNumber = position + 1
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
    }

    private fun showPagePickerDialog() {
        val currentPage = (binding.viewPager.currentItem + 1).coerceIn(1, MushafConfig.TOTAL_PAGES)
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
                binding.viewPager.setCurrentItem(page - 1, true)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun toggleBookmark() {
        val pageNumber = binding.viewPager.currentItem + 1
        viewModel.toggleBookmark(pageNumber) { isBookmarked ->
            val msg = if (isBookmarked) getString(R.string.bookmark_added) else getString(R.string.bookmark_removed)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeDownloadProgress() {
        lifecycleScope.launch {
            viewModel.downloadProgress.collectLatest { progress ->
                val isDownloading = progress.downloaded < progress.total
                binding.downloadProgressContainer.isVisible = isDownloading
                binding.tvDownloadProgress.text = getString(R.string.download_progress_format, progress.downloaded, progress.total)
            }
        }
        viewModel.refreshDownloadProgress()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshDownloadProgress()
    }

    companion object {
        const val EXTRA_PAGE_NUMBER = "extra_page_number"
    }
}
