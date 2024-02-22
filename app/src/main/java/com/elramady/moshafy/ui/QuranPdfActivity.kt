package com.elramady.moshafy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.ActivityQuranPdfBinding

class QuranPdfActivity : AppCompatActivity() {

    lateinit var binding: ActivityQuranPdfBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding=ActivityQuranPdfBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_quran_pdf)

        try {
            renderPdf()
        }catch (e:Exception){
            Log.e("errorPdf",e.message.toString())
        }

    }



  fun  renderPdf(){

        binding.pdfView.fromAsset(this.assets.toString())
            .load()
            //   .enableSwipe(true) // allows to block changing pages
            // using swipe
//            .swipeHorizontal(false)
//            .enableDoubletap(true)
  //          .pages(0)
            //   .defaultPage(0)
            // allows to draw something on the current page, usually visible in the middle of the screen
            //    .onDraw(onDrawListener)
            // allows to draw something on all pages, separately for every page. Called only for visible pages
            // .onDrawAll(onDrawListener)
            // .onLoad(onLoadCompleteListener) // called after document is loaded and starts to be rendered
//            .onPageChange(onPageChangeListener)
//            .onPageScroll(onPageScrollListener)
//            .onError(onErrorListener)
//            .onPageError(onPageErrorListener)
//            .onRender(onRenderListener) // called after document is rendered for the first time
//            // called on single tap, return true if handled, false to toggle scroll handle visibility
//            .onTap(onTapListener)
//            .onLongPress(onLongPressListener)
//            .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
//            .password(null)
//            .scrollHandle(null)
//            .enableAntialiasing(true) // improve rendering a little bit on low-res screens
//            // spacing between pages in dp. To define spacing color, set view background
//            .spacing(0)
//            .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
////            .linkHandler(DefaultLinkHandler)
////            .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
//            .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
//            .pageSnap(false) // snap pages to screen boundaries
//            .pageFling(false) // make a fling change only a single page like ViewPager
//            .nightMode(false) // toggle night mode
          //  .load()

    }
}