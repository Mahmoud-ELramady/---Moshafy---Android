package com.elramady.moshafy.repo

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query
import com.elramady.moshafy.room.DataBase
import com.elramady.moshafy.vo.sebha.SebhaData

class SebhaRepository(private val db:DataBase) {

    suspend fun insertSebha(sebha: SebhaData)=db.sebhaDao.insertSebha(sebha)

    fun getAllSebha()=db.sebhaDao.getAllSebha()

    suspend fun updateSebha(countSebha:Int,idSebha:Int?)=db.sebhaDao.updateSebha(countSebha,idSebha)

    suspend fun deleteCountOfSebha(idSebha:Int?)=db.sebhaDao.deleteCountOfSebha(idSebha)

    suspend fun deleteAllCountOfSebha()=db.sebhaDao.deleteAllCountOfSebha()

    suspend  fun deleteSebha(idSebha:Int?)=db.sebhaDao.deleteSebha(idSebha)

    fun getSumNumberOfAllSebha()=db.sebhaDao.getSumNumberOfAllSebha()

}