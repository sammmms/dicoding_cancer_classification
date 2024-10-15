package com.dicoding.asclepius.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.database.CancerClassification
import com.dicoding.asclepius.data.database.CancerClassificationDao
import com.dicoding.asclepius.data.database.CancerClassificationRoomDatabase
import java.util.concurrent.Executors

class CancerClassificationRepository(application: Application) {
    private val cancerClassificationDao: CancerClassificationDao
    private val executorService = Executors.newSingleThreadExecutor()

    init {
        Log.d("CancerClassificationRepository", "init: $application")

        val db = CancerClassificationRoomDatabase.getDatabase(application)
        Log.d("CancerClassificationRepository", "init: $db")
        cancerClassificationDao = db.cancerClassificationDao()
    }

    fun getAllClassification(): LiveData<List<CancerClassification>> = cancerClassificationDao.getAll()

    fun getClassificationById(id: Int): LiveData<CancerClassification> = cancerClassificationDao.getById(id)

    fun insertClassification(classification: CancerClassification) {
        executorService.execute {
            cancerClassificationDao.insert(classification)
        }
    }

    fun deleteClassification(classification: CancerClassification) {
        executorService.execute {
            cancerClassificationDao.delete(classification)
        }
    }

}