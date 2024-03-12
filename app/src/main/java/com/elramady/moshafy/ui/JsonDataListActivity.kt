package com.elramady.moshafy.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elramady.moshafy.Adapters.JsonDataAdapter
import com.elramady.moshafy.R
import com.elramady.moshafy.ViewModel.JsonDataViewModel
import com.elramady.moshafy.ViewModel.JsonViewModelFactory
import com.elramady.moshafy.databinding.ActivityJsonDataListBinding

class JsonDataListActivity : AppCompatActivity() {
    lateinit var binding:ActivityJsonDataListBinding

    val jsonViewModel: JsonDataViewModel by viewModels(){ JsonViewModelFactory(application) }
    private val adapterJsonDataList=JsonDataAdapter(this)

    private var jsonName=""
    private var jsonTitle=""
    var activityName=""
    lateinit var pref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        binding=ActivityJsonDataListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pref=getSharedPreferences("activityNames", MODE_PRIVATE)

        activityName=  pref.getString("activityName","").toString()

        if (activityName=="ramadan"){
            binding.imageJsonList.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.ramadan_image))
        }


        jsonName= intent.getStringExtra("jsonName").toString()
        jsonTitle= intent.getStringExtra("jsonTitle").toString()

        initRv()

        binding.tvTitleCard.text=jsonTitle


        jsonViewModel.getDataList(jsonName)

        jsonViewModel.jsonModelItem.observe(this, Observer {
            adapterJsonDataList.setList(it)
        })





    }



    private fun initRv(){
        binding.rvJsonList.apply {
            layoutManager= LinearLayoutManager(this@JsonDataListActivity)
            adapter=adapterJsonDataList
            setHasFixedSize(true)
        }


    }

}