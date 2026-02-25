package com.elramady.moshafy.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.ActivityAboutAppBinding
import androidx.core.net.toUri

class AboutAppActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAboutAppBinding = DataBindingUtil.setContentView(this, R.layout.activity_about_app)
        window.statusBarColor = getColor(R.color.purple_700)
        window.decorView.windowInsetsController?.setSystemBarsAppearance(
            android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, // أيقونات غامقة (سوداء)
            android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )


        binding.facebook.setOnClickListener {
            val uri: Uri = Uri.parse("https://www.facebook.com/mahmoudelrmady")
            val intent= Intent(Intent.ACTION_VIEW,uri)
            this.startActivity(intent)
        }
        binding.twitter.setOnClickListener {
            val uri: Uri = Uri.parse("https://twitter.com/melramady84")
            val intent= Intent(Intent.ACTION_VIEW,uri)
            this.startActivity(intent)
        }

        binding.linkedin.setOnClickListener {
            val uri = "https://linkedin.com/in/mahmoud-elramady-05b79518a".toUri()
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

    }


//    https://www.facebook.com/mahmoudelrmady
//    https://twitter.com/melramady84
//    linkedin.com/in/mahmoud-el-ramady-05b79518a


}