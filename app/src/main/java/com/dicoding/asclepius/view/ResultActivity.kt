package com.dicoding.asclepius.view

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.database.CancerClassification
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.data.response.Response
import com.dicoding.asclepius.data.retrofit.ApiConfig
import com.dicoding.asclepius.data.util.CancerClassificationHelper
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.view.adapter.ArticlesAdapter
import com.google.android.material.R.drawable.ic_arrow_back_black_24
import retrofit2.Call
import retrofit2.Callback

@Suppress("DEPRECATION")
class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var cancerClassificationHelper: CancerClassificationHelper

    companion object{
        const val EXTRA_CLASSIFICATION = "extra_classification"
        const val API_KEY = "2270447881304d0abf1ecceb11709533"
    }

    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            getArticles()
            toolbar.title = getString(R.string.result)
            toolbar.setNavigationIcon(ic_arrow_back_black_24)
            toolbar.setNavigationOnClickListener { finish() }

            articleRv.layoutManager = LinearLayoutManager(this@ResultActivity)
        }

        val classification: CancerClassification? = intent.getParcelableExtra(EXTRA_CLASSIFICATION)
        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        setClassificationView(classification)
    }

    private fun getArticles(){
        val client = ApiConfig.getApiService().getArticles(API_KEY)
        setLoading(true)
        client.enqueue(object: Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                setLoading(false)
                if(response.isSuccessful){
                    setErrorMessage("", false)
                    val articles = response.body()?.articles
                    if(articles != null){
                        if(articles.isNotEmpty()){
                            setArticlesView(articles)
                            setNoDataView(false)
                        } else {
                            setNoDataView(true)
                        }
                    } else {
                        setErrorMessage("Data tidak ditemukan")
                    }}
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                setLoading(false)
                Log.e("MainActivity", "onFailure: ${t.message}")
                setErrorMessage(t.message.toString())
            }
        })
    }

    @SuppressLint("StringFormatInvalid")
    private fun setClassificationView(classification: CancerClassification?) {
        if(classification != null){
            cancerClassificationHelper = CancerClassificationHelper(application)

            cancerClassificationHelper.getClassificationById(classification.id).observe(this) {
                if (it != null) {
                    binding.saveClassificationButton.isEnabled = false
                    binding.removeClassificationButton.visibility = View.VISIBLE
                }
            }

            binding.resultImage.setImageURI(Uri.parse(classification.image))
            binding.resultText.text = getString(R.string.prediction, classification.prediction, (classification.confidence * 100).toInt())

            binding.saveClassificationButton.setOnClickListener{
                cancerClassificationHelper.insertClassification(classification)
                binding.saveClassificationButton.isEnabled = false
                binding.removeClassificationButton.visibility = View.VISIBLE
                Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
            }

            binding.removeClassificationButton.setOnClickListener{
                cancerClassificationHelper.deleteClassification(classification)
                binding.saveClassificationButton.isEnabled = true
                binding.removeClassificationButton.visibility = View.GONE
                Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setArticlesView(articles: List<ArticlesItem>) {
        val articleList = ArrayList<ArticlesItem>(articles).filter {
            it.title != null && it.description != null && it.urlToImage != null
        }
        val adapter = ArticlesAdapter()
        adapter.setData(articleList)
        binding.articleRv.adapter = adapter
    }
    private fun setNoDataView(state: Boolean) {
        if (state) {
            binding.noArticleText.visibility = View.VISIBLE
        } else {
            binding.noArticleText.visibility = View.GONE
        }
    }

    private fun setLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setErrorMessage(message: String, state: Boolean = true) {
        if(state){
            binding.errorArticle.visibility = View.VISIBLE
        } else {
            binding.errorArticle.visibility = View.GONE
        }
        if(message.isEmpty()){
            binding.errorArticle.text = getString(R.string.error_message, "Data tidak ditemukan")
        } else {
            binding.errorArticle.text = getString(R.string.error_message, message)
        }
    }
}