package com.dicoding.asclepius.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CancerClassification::class], version = 1)
abstract class CancerClassificationRoomDatabase : RoomDatabase(){
    abstract fun cancerClassificationDao(): CancerClassificationDao

    companion object{
        @Volatile
        private var INSTANCE: CancerClassificationRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): CancerClassificationRoomDatabase {
            Log.e("CancerClassificationRoomDatabase", "getDatabase: $INSTANCE")
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CancerClassificationRoomDatabase::class.java,
                    "cancer_classification_database"
                ).build()
                Log.e("CancerClassificationRoomDatabase", "getDatabase: $instance")
                INSTANCE = instance
                return instance
            }
        }
    }
}