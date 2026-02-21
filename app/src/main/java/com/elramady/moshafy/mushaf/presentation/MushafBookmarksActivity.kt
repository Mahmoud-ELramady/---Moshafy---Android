package com.elramady.moshafy.mushaf.presentation

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.ActivityMushafBookmarksBinding
import com.elramady.moshafy.mushaf.data.local.MushafDatabase
import com.elramady.moshafy.mushaf.data.local.entity.MushafBookmark
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MushafBookmarksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMushafBookmarksBinding
    private lateinit var adapter: MushafBookmarksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        binding = ActivityMushafBookmarksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val titleView = TextView(this).apply {
            text = getString(com.elramady.moshafy.R.string.bookmarks_list)
            setTextColor(androidx.core.content.ContextCompat.getColor(this@MushafBookmarksActivity, android.R.color.white))
            textSize = 20f
            gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
            setPadding(0, 0, resources.getDimensionPixelSize(R.dimen._10dp), 0)
            ResourcesCompat.getFont(this@MushafBookmarksActivity, R.font.font_app)?.let { typeface ->
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

        adapter = MushafBookmarksAdapter { bookmark ->
            val intent = Intent(this, MushafReaderActivity::class.java).apply {
                putExtra(MushafReaderActivity.EXTRA_PAGE_NUMBER, bookmark.pageNumber)
            }
            startActivity(intent)
            finish()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        observeBookmarks()
    }

    private fun observeBookmarks() {
        val dao = MushafDatabase.getInstance(this).mushafDao()
        lifecycleScope.launch {
            dao.getAllBookmarks().collectLatest { bookmarks ->
                adapter.submitList(bookmarks)
                binding.emptyView.isVisible = bookmarks.isEmpty()
            }
        }
    }
}
