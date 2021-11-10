package com.elramady.moshafy.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.ActivityAboutAppBinding

class AboutAppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAboutAppBinding = DataBindingUtil.setContentView(this, R.layout.activity_about_app)

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
            val uri: Uri = Uri.parse("https://www.linkedin.com/in/mahmoud-el-ramady-05b79518a/")
            val intent= Intent(Intent.ACTION_VIEW,uri)
            this.startActivity(intent)
        }

    }


//    https://www.facebook.com/mahmoudelrmady
//    https://twitter.com/melramady84
//    linkedin.com/in/mahmoud-el-ramady-05b79518a


}