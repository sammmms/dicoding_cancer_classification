package com.dicoding.asclepius.data.util

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.database.CancerClassification
import com.dicoding.asclepius.data.repository.CancerClassificationRepository

class CancerClassificationHelper(application: Application) : ViewModel(){
    private val repository = CancerClassificationRepository(application)

    fun getAllClassification() = repository.getAllClassification()

    fun getClassificationById(id: Int) = repository.getClassificationById(id)

    fun insertClassification(cancerClassification: CancerClassification) {
        repository.insertClassification(cancerClassification)
    }

    fun deleteClassification(cancerClassification: CancerClassification) {
        repository.deleteClassification(cancerClassification)
    }
}