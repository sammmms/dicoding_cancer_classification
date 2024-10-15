package com.dicoding.asclepius.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.database.CancerClassification
import com.dicoding.asclepius.data.util.CancerClassificationHelper
import com.dicoding.asclepius.databinding.HistoryActivityBinding
import com.dicoding.asclepius.view.adapter.HistoryAdapter
import com.google.android.material.R.drawable.ic_arrow_back_black_24

class HistoryActivity : ComponentActivity() {
    private lateinit var binding: HistoryActivityBinding
    private lateinit var cancerClassificationHelper: CancerClassificationHelper

    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = HistoryActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cancerClassificationHelper = CancerClassificationHelper(application)

        with(binding){
            val rvHistoryActivity = historyRecyclerView
            val layoutManager = LinearLayoutManager(this@HistoryActivity)
            rvHistoryActivity.layoutManager = layoutManager

            historyToolbar.setNavigationIcon(ic_arrow_back_black_24)
            historyToolbar.setNavigationOnClickListener { finish()}
        }

        cancerClassificationHelper.getAllClassification().observe(this) {
            setRecyclerView(it)
        }
    }

    private fun setRecyclerView(listClassification: List<CancerClassification>) {
        val historyAdapter = HistoryAdapter(application)
        historyAdapter.setData(listClassification)
        binding.historyRecyclerView.adapter = historyAdapter
    }
}