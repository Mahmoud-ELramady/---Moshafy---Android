package com.elramady.moshafy.mushaf.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding = ActivityMushafBookmarksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
