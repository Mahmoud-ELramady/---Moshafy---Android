package com.elramady.moshafy.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elramady.moshafy.R
//import com.github.barteksc.pdfviewer.PDFView
//import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle


class MoshfPdfActivity : AppCompatActivity() {
   // lateinit var pdf:PDFView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moshf_pdf)

//         pdf=findViewById(R.id.pdfView)
//
//        pdf.fromAsset("quran3.pdf")
//            .enableSwipe(true) // allows to block changing pages using swipe
//            .swipeHorizontal(false)
//            .enableDoubletap(true)
//            .defaultPage(0)
//            .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
//            .password(null)
//            .scrollHandle(DefaultScrollHandle(this))
//            .enableAntialiasing(true) // improve rendering a little bit on low-res screens
//            // spacing between pages in dp. To define spacing color, set view background
//            .spacing(0)
//            .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
//
//            .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
//            .pageSnap(false) // snap pages to screen boundaries
//            .pageFling(false) // make a fling change only a single page like ViewPager
//            .nightMode(false) // toggle night mode
////            .swipeHorizontal(true)
////            .pageSnap(true)
////            .autoSpacing(true)
////            .pageFling(true)
//            .load()

    }









//    fun onPageScrolled(page: Int, positionOffset: Float) {
//        val pageCount: Int = pdf.getPageCount()
//        val pageOffset = 1f / pageCount
//        var currentPage = (positionOffset / pageOffset).toInt() + 1
//        currentPage = Math.min(currentPage, pageCount)
//    }
//
}


