package com.dicoding.asclepius.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CancerClassificationDao {
    @Query("SELECT * FROM cancerclassification")
    fun getAll(): LiveData<List<CancerClassification>>

    @Query("SELECT * FROM cancerclassification WHERE id = :id")
    fun getById(id: Int): LiveData<CancerClassification>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(classification: CancerClassification)

    @Delete
    fun delete(classification: CancerClassification)
}