package com.elramady.moshafy.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.elramady.moshafy.R
import com.elramady.moshafy.ViewModel.JsonDataViewModel
import com.elramady.moshafy.ViewModel.JsonViewModelFactory
import com.elramady.moshafy.databinding.ActivitySeraNabwyaBinding
import com.elramady.moshafy.utils.ReadJson
import com.elramady.moshafy.vo.jsonModel.JsonModel
import com.elramady.moshafy.vo.jsonModel.JsonModelItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SeraNabwyaActivity : AppCompatActivity() {
    lateinit var binding:ActivitySeraNabwyaBinding
    lateinit var pref: SharedPreferences

     private var activityName=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  enableEdgeToEdge()
        binding=ActivitySeraNabwyaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        pref=getSharedPreferences("activityNames", MODE_PRIVATE)

        activityName=  pref.getString("activityName","").toString()



        if (activityName=="ramadan"){
            binding.prophetLottie.setAnimation(R.raw.ramadan_lottie)
            bindButtonsNamesRamadan()
        }else{

            bindButtonsNamesSera()

        }



        binding.department1.containerTitleJson.setOnClickListener {
            val i= Intent(this,JsonDataListActivity::class.java)
            if (activityName=="ramadan"){
                i.putExtra("jsonName","rules_ramadan.json")
                i.putExtra("jsonTitle",getString(R.string.rules_ramadan))
            }else{
                i.putExtra("jsonName","life_prophet.json")
                i.putExtra("jsonTitle",getString(R.string.life_prophet))
            }

            startActivity(i)
        }


        binding.department2.containerTitleJson.setOnClickListener {
            val i= Intent(this,JsonDataListActivity::class.java)

            if (activityName=="ramadan"){
                i.putExtra("jsonName","doaa_ramadan.json")
                i.putExtra("jsonTitle",getString(R.string.doaa_ramadan))
            }else{
                i.putExtra("jsonName","wife_prophet.json")
                i.putExtra("jsonTitle",getString(R.string.wife_prophet))

            }


            startActivity(i)
        }


        binding.department3.containerTitleJson.setOnClickListener {
            val i= Intent(this,JsonDataListActivity::class.java)

            if (activityName=="ramadan") {
                i.putExtra("jsonName", "health_ramadan.json")
                i.putExtra("jsonTitle", getString(R.string.health_ramadan))
            }else{
                i.putExtra("jsonName","ghazwa_prophet.json")
                i.putExtra("jsonTitle",getString(R.string.ghazwa_prophet))

            }


            startActivity(i)
        }


        binding.department4.containerTitleJson.setOnClickListener {
            val i= Intent(this,JsonDataListActivity::class.java)

            if (activityName=="ramadan") {
                i.putExtra("jsonName", "finish_quran_ramadan.json")
                i.putExtra("jsonTitle", getString(R.string.finish_quran_ramadan))
            }else{

                i.putExtra("jsonName","about_prophet.json")
                i.putExtra("jsonTitle",getString(R.string.about_prophet))
            }

            startActivity(i)
        }

        binding.department5.containerTitleJson.setOnClickListener {
            val i= Intent(this,JsonDataListActivity::class.java)

            if (activityName=="ramadan") {
                i.putExtra("jsonName", "advice_ramadan.json")
                i.putExtra("jsonTitle", getString(R.string.adivce_ramadan))
            }else{

//                i.putExtra("jsonName","about_prophet.json")
//                i.putExtra("jsonTitle",getString(R.string.about_prophet))
            }

            startActivity(i)
        }



    }

    private fun bindButtonsNamesSera() {
        binding.department1.titleJson.text= getString(R.string.life_prophet)
        binding.department2.titleJson.text= getString(R.string.wife_prophet)
        binding.department3.titleJson.text= getString(R.string.ghazwa_prophet)
        binding.department4.titleJson.text= getString(R.string.about_prophet)

    }
    private fun bindButtonsNamesRamadan() {
        binding.department5.root.visibility= View.VISIBLE
        binding.department1.titleJson.text= getString(R.string.rules_ramadan)
        binding.department2.titleJson.text= getString(R.string.doaa_ramadan)
        binding.department3.titleJson.text= getString(R.string.health_ramadan)
        binding.department4.titleJson.text= getString(R.string.finish_quran_ramadan)
        binding.department5.titleJson.text= getString(R.string.adivce_ramadan)

    }


}