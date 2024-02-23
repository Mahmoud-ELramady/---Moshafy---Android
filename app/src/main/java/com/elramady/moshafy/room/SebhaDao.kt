package com.elramady.moshafy.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elramady.moshafy.vo.AzkarListen.AzkarListeningItem
import com.elramady.moshafy.vo.RecitersDetails.RecitersDetails
import com.elramady.moshafy.vo.RecitersNames.Reciter
import com.elramady.moshafy.vo.SurahText.SurahText
import com.elramady.moshafy.vo.SurahsNames.Data
import com.elramady.moshafy.vo.TafseerText.TafseerText
import com.elramady.moshafy.vo.TafseersNames.TafseersNamesItem
import com.elramady.moshafy.vo.sebha.SebhaData
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single


@Dao
interface SebhaDao {

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertSebha(sebha:SebhaData):Long

    @Query("select * from sebha_table")
    fun getAllSebha(): LiveData<List<SebhaData>>

    @Query("UPDATE sebha_table SET numberOfSebha = :countSebha WHERE id = :idSebha")
   suspend fun updateSebha(countSebha:Int,idSebha:Int)

    @Query("UPDATE sebha_table SET numberOfSebha = 0 WHERE id = :idSebha")
    suspend fun deleteCountOfSebha(idSebha:Int)

    @Query("UPDATE sebha_table SET numberOfSebha = 0")
    suspend fun deleteAllCountOfSebha()

    @Query("DELETE FROM sebha_table WHERE id = :idSebha")
    suspend  fun deleteSebha(idSebha:Int)

    @Query("SELECT SUM(numberOfSebha) FROM sebha_table")
    fun _getSumNumberOfAllSebha():LiveData<Int>


}