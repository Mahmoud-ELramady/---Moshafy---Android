package com.elramady.moshafy.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.ActivityDetailsJsonBinding

class DetailsJsonActivity : AppCompatActivity() {

    lateinit var binding:ActivityDetailsJsonBinding

    var titleJson=""
    var descJson=""

    var activityName=""
    lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  enableEdgeToEdge()
        binding=ActivityDetailsJsonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pref=getSharedPreferences("activityNames", MODE_PRIVATE)

        activityName=  pref.getString("activityName","").toString()

        if (activityName=="ramadan"){
            binding.imageDetails.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.phanos))
        }


        titleJson= intent.getStringExtra("titleJson").toString()
        descJson= intent.getStringExtra("descJson").toString()

        binding.titleDetails.text=titleJson
        binding.descDetails.text=descJson


    }
}