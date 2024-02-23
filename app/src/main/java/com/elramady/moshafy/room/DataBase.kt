package com.elramady.moshafy.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.elramady.moshafy.vo.AzkarListen.AzkarListeningItem
import com.elramady.moshafy.vo.RecitersDetails.RecitersDetails
import com.elramady.moshafy.vo.RecitersNames.Reciter
import com.elramady.moshafy.vo.SurahText.SurahText
import com.elramady.moshafy.vo.SurahsNames.Data
import com.elramady.moshafy.vo.TafseerText.TafseerText
import com.elramady.moshafy.vo.TafseersNames.TafseersNamesItem
import com.elramady.moshafy.vo.sebha.SebhaData


//@Database(entities = [Data::class,SurahText::class,Reciter::class,RecitersDetails::class
//    ,TafseersNamesItem::class,TafseerText::class, AzkarListeningItem::class,SebhaData::class]
//    ,version = 2,)

@Database(entities = [Data::class,SurahText::class,Reciter::class,RecitersDetails::class
    ,TafseersNamesItem::class,TafseerText::class, AzkarListeningItem::class,SebhaData::class]
    ,version = 2, autoMigrations = [AutoMigration(from = 1, to = 2)])
@TypeConverters(Converters::class,ConvertersReciations::class)
abstract class DataBase :RoomDatabase() {

    abstract val surahsDao : SurahsNamesDao
    abstract val sebhaDao : SebhaDao

    companion object{


        @Volatile
        private var INSTANCE:DataBase ?= null

        fun getInstance(context: Context):DataBase{
            synchronized(this){
                var instance:DataBase?= INSTANCE
                if (instance==null){
                    instance= Room.databaseBuilder(context.applicationContext
                        ,DataBase::class.java
                        ,"surahs_data_base")

                      // .fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }

    }


}