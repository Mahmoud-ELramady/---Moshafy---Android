package com.elramady.moshafy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.elramady.moshafy.BuildConfig
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding=DataBindingUtil.setContentView(this,R.layout.activity_main)


        binding.cardQuranFull.setOnClickListener {
            val intent=Intent(this, SebhaActivity::class.java)
            this.startActivity(intent)
        }

        binding.cardQuranTranslate.setOnClickListener {
            val intent=Intent(this, SurahsNamesActivity::class.java)
            this.startActivity(intent)

        }


        binding.cardReciter.setOnClickListener {
            val thread=Thread(Runnable {
                val intent=Intent(this, RecitersActivity::class.java)
                this.startActivity(intent)
            }).start()


        }





        binding.cardTafseers.setOnClickListener {
            val intent=Intent(this, TafseerActivity::class.java)
            this.startActivity(intent)

        }

        binding.azkarCard.setOnClickListener {
            val intent=Intent(this, AzkarTapActivity::class.java)
            this.startActivity(intent)

        }

        binding.aboutApp.setOnClickListener {
            val thread=Thread(Runnable {
                val intent=Intent(this, AboutAppActivity::class.java)
                this.startActivity(intent)
            }).start()


        }


        binding.shareAppButton.setOnClickListener {
            try {
               val shareIntent:Intent  =  Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
               var shareMessage:String= "\nقم بتحميل تطبيق مُصحفى يحتوى على القرأن كاملا وأكثر من 200 قارئ ومجموعة كبيره من التفاسير والأذكار حمل الأن\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + applicationContext.packageName +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Share Moshafy مُصحفى"));
            } catch(e:Exception) {
                //e.toString();
            }
        }

    }








}