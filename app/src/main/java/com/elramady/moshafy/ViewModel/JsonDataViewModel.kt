package com.elramady.moshafy.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elramady.moshafy.FragmentPlayer.ApplicationClass
import com.elramady.moshafy.utils.ReadJson
import com.elramady.moshafy.vo.jsonModel.JsonModelItem

class JsonDataViewModel(
    app:Application
): AndroidViewModel(app) {
    private var _jsonModelItem= MutableLiveData<List<JsonModelItem>>()
    val jsonModelItem:LiveData<List<JsonModelItem>> =_jsonModelItem

  private  fun getData(path: String): List<JsonModelItem> {
        val jsonString = ReadJson.ReadJSONFromAssets(getApplication(), path)
        return ReadJson.parseJsonToObjects(jsonString)
    }

   fun getDataList(path:String){
       _jsonModelItem.value=getData(path)
   }





}