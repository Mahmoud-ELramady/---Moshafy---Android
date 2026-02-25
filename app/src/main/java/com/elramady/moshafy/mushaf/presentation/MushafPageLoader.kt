package com.elramady.moshafy.mushaf.presentation

import android.graphics.PointF
import android.view.View
import android.widget.ImageView
import coil.load
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.ImageViewState
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.elramady.moshafy.mushaf.data.repository.MushafImageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Loads Mushaf page images into ImageView using Coil.
 * Handles both local files and remote URLs.
 * When loading a remote page, shows [loadingOverlay] until the image is available.
 */
class MushafPageLoader(
    private val imageRepository: MushafImageRepository,
    private val placeholderResId: Int
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun loadPage(pageNumber: Int, imageView: ImageView, loadingOverlay: View? = null) {
        loadingOverlay?.visibility = View.GONE
        imageView.load(placeholderResId) { crossfade(true) }
        scope.launch {
            val source = imageRepository.getPageImageSource(pageNumber)
            val data = when (source) {
                is MushafImageRepository.ImageSource.Local -> File(source.file.absolutePath)
                is MushafImageRepository.ImageSource.Remote -> source.url
            }
            val showLoading = source is MushafImageRepository.ImageSource.Remote && loadingOverlay != null
            if (showLoading) {
                loadingOverlay?.post { loadingOverlay.visibility = View.VISIBLE }
            }
            imageView.load(data) {
                crossfade(true)
                placeholder(placeholderResId)
                error(placeholderResId)
                listener(
                    onSuccess = { _, _ ->
                        if (showLoading) loadingOverlay?.post { loadingOverlay?.visibility = View.GONE }
                    },
                    onError = { _, _ ->
                        if (showLoading) loadingOverlay?.post { loadingOverlay?.visibility = View.GONE }
                    }
                )
            }
        }
    }

    fun loadPage2(
        pageNumber: Int,
        imageView: SubsamplingScaleImageView,
        loadingOverlay: View? = null
    ) {

        // reset view
        imageView.recycle()
        imageView.setImage(ImageSource.resource(placeholderResId))

        scope.launch {

            val source = imageRepository.getPageImageSource(pageNumber)

            val showLoading =
                source is MushafImageRepository.ImageSource.Remote && loadingOverlay != null

            if (showLoading) {
                loadingOverlay?.post { loadingOverlay.visibility = View.VISIBLE }
            }

            val imageSource = when (source) {

                is MushafImageRepository.ImageSource.Local -> {
                    ImageSource.uri(source.file.absolutePath)
                }

                is MushafImageRepository.ImageSource.Remote -> {
                    ImageSource.uri(source.url)
                }
            }

            withContext(Dispatchers.Main) {

                imageView.setImage(
                    imageSource,
                    ImageViewState(1f, PointF(0f, 0f), 0)
                )

                loadingOverlay?.visibility = View.GONE
            }
        }
    }

}
