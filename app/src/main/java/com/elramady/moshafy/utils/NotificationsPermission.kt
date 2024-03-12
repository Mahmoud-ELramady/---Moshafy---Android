package com.elramady.moshafy.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBindings
import com.elramady.moshafy.R
import com.google.android.material.snackbar.Snackbar

object NotificationsPermission {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>




     fun requestPermission(context: AppCompatActivity,binding:View): ActivityResultLauncher<String> {
       this.requestPermissionLauncher = context.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(context, "Notifications permission granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
//            Toast.makeText(
//                this, "${getString(R.string.app_name)} can't post notifications without Notification permission",
//                Toast.LENGTH_LONG
//            ).show()

                Snackbar.make(
                    binding,
                    String.format(
                        String.format(
                            "فعل النوتفكيشن عشان تقدر تشغل التلاوات",
                            context.getString(R.string.app_name)
                        )
                    ),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("go to setting") {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val settingsIntent: Intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        context.startActivity(settingsIntent)
                    }
                }.show()
            }
        }
        return requestPermissionLauncher
    }



}