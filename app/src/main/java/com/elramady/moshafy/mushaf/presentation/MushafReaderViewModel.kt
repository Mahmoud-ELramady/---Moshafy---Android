package com.elramady.moshafy.mushaf.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elramady.moshafy.mushaf.config.MushafConfig
import com.elramady.moshafy.mushaf.data.local.MushafDatabase
import com.elramady.moshafy.mushaf.data.repository.MushafImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class MushafReaderViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = MushafDatabase.getInstance(application).mushafDao()
    private val imageRepository = MushafImageRepository(application, dao)

    init {
        viewModelScope.launch {
            dao.getDownloadedPagesCountFlow()
                .catch { _ -> }
                .collect { count ->
                    _downloadProgress.value = DownloadProgress(count, MushafConfig.TOTAL_PAGES)
                }
        }
    }

    private val _currentPage = MutableLiveData(1)
    val currentPage: LiveData<Int> = _currentPage

    private val _downloadProgress = MutableStateFlow(DownloadProgress(0, MushafConfig.TOTAL_PAGES))
    val downloadProgress: StateFlow<DownloadProgress> = _downloadProgress.asStateFlow()

    private val _pageImageSource = MutableStateFlow<PageImageSource?>(null)
    val pageImageSource: StateFlow<PageImageSource?> = _pageImageSource.asStateFlow()

    fun setCurrentPage(page: Int) {
        _currentPage.value = page.coerceIn(1, MushafConfig.TOTAL_PAGES)
        viewModelScope.launch {
            saveLastReadPage(_currentPage.value ?: 1)
        }
    }

    fun loadPageImageSource(pageNumber: Int) {
        viewModelScope.launch {
            val source = imageRepository.getPageImageSource(pageNumber)
            _pageImageSource.value = when (source) {
                is MushafImageRepository.ImageSource.Local -> PageImageSource.Local(source.file.absolutePath)
                is MushafImageRepository.ImageSource.Remote -> PageImageSource.Remote(source.url)
            }
        }
    }

    fun refreshDownloadProgress() {
        viewModelScope.launch {
            val count = withContext(Dispatchers.IO) { dao.getDownloadedPagesCount() }
            _downloadProgress.value = DownloadProgress(count, MushafConfig.TOTAL_PAGES)
        }
    }

    fun saveLastReadPage(pageNumber: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertLastReadPage(
                com.elramady.moshafy.mushaf.data.local.entity.MushafLastReadPage(
                    id = 1,
                    pageNumber = pageNumber
                )
            )
        }
    }

    fun getLastReadPage(): Int {
        return kotlinx.coroutines.runBlocking(Dispatchers.IO) {
            dao.getLastReadPageNumber() ?: 1
        }
    }

    fun toggleBookmark(pageNumber: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val existing = dao.getBookmarkByPage(pageNumber)
            if (existing != null) {
                dao.deleteBookmarkByPage(pageNumber)
                kotlinx.coroutines.withContext(Dispatchers.Main) { onResult(false) }
            } else {
                dao.insertBookmark(
                    com.elramady.moshafy.mushaf.data.local.entity.MushafBookmark(
                        pageNumber = pageNumber
                    )
                )
                kotlinx.coroutines.withContext(Dispatchers.Main) { onResult(true) }
            }
        }
    }

    fun isBookmarked(pageNumber: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val exists = dao.getBookmarkByPage(pageNumber) != null
            kotlinx.coroutines.withContext(Dispatchers.Main) { onResult(exists) }
        }
    }

    sealed class PageImageSource {
        data class Local(val path: String) : PageImageSource()
        data class Remote(val url: String) : PageImageSource()
    }

    data class DownloadProgress(val downloaded: Int, val total: Int)
}
