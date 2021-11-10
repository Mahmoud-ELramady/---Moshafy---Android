package com.elramady.moshafy

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater

class LoadingDialog(var activity: Activity) {

     lateinit var dialog: AlertDialog

    fun startLoadingDialog(){

        val builder:AlertDialog.Builder=AlertDialog.Builder(activity)
        val inflater:LayoutInflater=activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_dialog,null))
        builder.setCancelable(true)
        dialog=builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnCancelListener {
            activity.finish()
        }
        dialog.show()

    }

    fun dismissDialog(){
        dialog.dismiss()
    }
}