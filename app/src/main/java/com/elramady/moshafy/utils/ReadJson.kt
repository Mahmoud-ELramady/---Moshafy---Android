package com.elramady.moshafy.utils

import android.content.Context
import android.util.Log
import com.elramady.moshafy.vo.jsonModel.JsonModelItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader

object ReadJson {

    fun ReadJSONFromAssets(context: Context, path: String): String {
        val identifier = "[ReadJSON]"
        try {
            val file = context.assets.open("$path")
            Log.i(
                identifier,
                "{DebuggingIdentifiers.actionOrEventSucceded} Found File: $file.",
            )
            val bufferedReader = BufferedReader(InputStreamReader(file))
            val stringBuilder = StringBuilder()
            bufferedReader.useLines { lines ->
                lines.forEach {
                    stringBuilder.append(it)
                }
            }
            Log.i(
                identifier,
                "getJSON  {DebuggingIdentifiers.actionOrEventSucceded} stringBuilder: $stringBuilder.",
            )
            val jsonString = stringBuilder.toString()
            Log.i(
                identifier,
                "{DebuggingIdentifiers.actionOrEventSucceded} JSON as String: $jsonString.",
            )
            return jsonString
        } catch (e: Exception) {
            Log.e(
                identifier,
                "{DebuggingIdentifiers.actionOrEventFailed} Error reading JSON: $e.",
            )
            e.printStackTrace()
            return ""
        }
    }


    fun parseJsonToObjects(jsonString: String): List<JsonModelItem> {
        val gson = Gson()
        val objectType = object : TypeToken<List<JsonModelItem>>() {}.type
        return gson.fromJson(jsonString, objectType)
    }

}