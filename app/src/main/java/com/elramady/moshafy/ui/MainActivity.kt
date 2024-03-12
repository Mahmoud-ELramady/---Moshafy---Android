package com.elramady.moshafy.ui

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.ActivityMainBinding
import com.elramady.moshafy.utils.NotificationsPermission
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

lateinit var binding:ActivityMainBinding
private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
lateinit var pref: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding=DataBindingUtil.setContentView(this,R.layout.activity_main)

        pref=getSharedPreferences("activityNames", MODE_PRIVATE)

        requestPermissionLauncher=  NotificationsPermission.requestPermission(this,binding.root)


        binding.cardSebha.setOnClickListener {
            val intent=Intent(this, SebhaActivity::class.java)
            this.startActivity(intent)
        }

        binding.cardQuranTranslate.setOnClickListener {
            val intent=Intent(this, SurahsNamesActivity::class.java)
            this.startActivity(intent)

        }


        binding.cardReciter.setOnClickListener {
            val thread=Thread(Runnable {

                // This is only necessary for API level >= 33 (TIRAMISU)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_GRANTED
                    ) {
                        val intent=Intent(this, RecitersActivity::class.java)
                        startActivity(intent)
                        Log.e("errorNoti","error")
                    } else {
                        Log.e("errorNoti","erro2r")

                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }else{

                    val intent=Intent(this, RecitersActivity::class.java)
                    startActivity(intent)
                    Log.e("errorNoti","error")

                }


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


        binding.seraNabawya.setOnClickListener {

                pref.edit().putString("activityName", "sera").apply()
                val intent=Intent(this, SeraNabwyaActivity::class.java)
                startActivity(intent)


        }

        binding.ramadansNights.setOnClickListener {

            pref.edit().putString("activityName", "ramadan").apply()
            val intent=Intent(this, SeraNabwyaActivity::class.java)
            startActivity(intent)


        }




    }


    private fun requestPermission(){
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
//            Toast.makeText(
//                this, "${getString(R.string.app_name)} can't post notifications without Notification permission",
//                Toast.LENGTH_LONG
//            ).show()

                Snackbar.make(
                    binding.root,
                    String.format(
                        String.format(
                            "فعل النوتفكيشن عشان تقدر تشغل التلاوات",
                            getString(R.string.app_name)
                        )
                    ),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("go to setting") {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val settingsIntent: Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                        startActivity(settingsIntent)
                    }
                }.show()
            }
        }
    }






}