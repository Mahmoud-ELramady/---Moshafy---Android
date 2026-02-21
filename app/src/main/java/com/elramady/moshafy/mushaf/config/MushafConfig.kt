package com.elramady.moshafy.mushaf.config

/**
 * Central configuration for Mushaf (Quran) image sources.
 * All URLs and constants are defined here - do NOT hardcode in other classes.
 */
object MushafConfig {
    /**
     * Base URL for remote Mushaf page images.
     * Must end with trailing slash.
     * Format: page_001.webp, page_002.webp, ... page_604.webp
     */
    const val BASE_URL = "https://raw.githubusercontent.com/Mahmoud-ELramady/open-mushaf-native/refs/heads/main/assets/mushaf-data/mushaf-elmadina-hafs-assim/"

    const val TOTAL_PAGES = 604
    const val PAGE_FILE_PREFIX = "page_"
    const val PAGE_FILE_EXTENSION = ".png"

    fun getRemotePageUrl(pageNumber: Int): String {
        require(pageNumber in 1..TOTAL_PAGES) { "Page number must be between 1 and $TOTAL_PAGES" }
      //  val formattedPage = pageNumber.toString().padStart(3, '0')
        return "$BASE_URL$pageNumber$PAGE_FILE_EXTENSION"
    }

    fun getLocalFileName(pageNumber: Int): String = "$PAGE_FILE_PREFIX${pageNumber.toString().padStart(3, '0')}$PAGE_FILE_EXTENSION"
}
