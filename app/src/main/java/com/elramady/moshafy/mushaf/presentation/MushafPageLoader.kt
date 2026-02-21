package com.elramady.moshafy.mushaf.presentation

import android.widget.ImageView
import coil.load
import com.elramady.moshafy.mushaf.data.repository.MushafImageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File

/**
 * Loads Mushaf page images into ImageView using Coil.
 * Handles both local files and remote URLs.
 */
class MushafPageLoader(
    private val imageRepository: MushafImageRepository,
    private val placeholderResId: Int
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun loadPage(pageNumber: Int, imageView: ImageView) {
        imageView.load(placeholderResId) { crossfade(true) }
        scope.launch {
            val source = imageRepository.getPageImageSource(pageNumber)
            val data = when (source) {
                is MushafImageRepository.ImageSource.Local -> File(source.file.absolutePath)
                is MushafImageRepository.ImageSource.Remote -> source.url
            }
            imageView.load(data) {
                crossfade(true)
                placeholder(placeholderResId)
                error(placeholderResId)
            }
        }
    }
}
