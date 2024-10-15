package com.dicoding.asclepius.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dicoding.asclepius.data.database.CancerClassification
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifier: ImageClassifierHelper

    private var currentImageUri: Uri? = null
    private var currentClassifications: List<Classifications>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != android.content.pm.PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }

        imageClassifier = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    showToast(error)
                }

                override fun onResult(result: List<Classifications>?, inferenceTime: Long) {
                    currentClassifications = result
                    moveToResult()
                }
            }
        )

        binding.analyzeButton.setOnClickListener{
            analyzeImage()
        }

        binding.galleryButton.setOnClickListener{
            startGallery()
        }

        binding.historyButton.setOnClickListener{
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    // Digunakan untuk mendeteksi uCrop
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP){
            showImage()
        }
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            .setType("image/*")
            .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

        val chooser = Intent.createChooser(intent, "Pilih gambar")

        launchIntentGallery.launch(chooser)
    }

    private val launchIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result -> if(result.resultCode == RESULT_OK){
            currentImageUri = result.data?.data
            contentResolver.takePersistableUriPermission(currentImageUri!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            cropImage()
        }
    }

    private fun showImage() {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        if(currentImageUri != null){
            binding.previewImageView.setImageURI(currentImageUri)
            binding.analyzeButton.isEnabled = true
        }
    }

    private fun analyzeImage() {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.

        if(currentImageUri != null){
            showToast("Analisis gambar...")
            imageClassifier.classifyStaticImage(currentImageUri!!)
        }else {
            showToast("Harap mengunggah gambar terlebih dahulu")
        }
    }

    private fun cropImage(){
        UCrop.of(currentImageUri!!, currentImageUri!!)
            .withAspectRatio(1f, 1f)
            .start(this)
    }

    private fun moveToResult() {
        val intent = Intent(this, ResultActivity::class.java)
        val cancer = currentClassifications?.get(0)?.categories?.get(0)
        val confidence = cancer?.score
        val nonCancer = currentClassifications?.get(0)?.categories?.get(1)
        val confidenceNonCancer = nonCancer?.score

        Log.d("MainActivity", "Cancer: ${cancer?.label}, Confidence: $confidence")
        Log.d("MainActivity", "Non Cancer: ${nonCancer?.label}, Confidence: $confidenceNonCancer")

        if (confidence != null && confidenceNonCancer != null) {
            if (confidence > confidenceNonCancer) {
                val cancerClassification = CancerClassification(
                    image = currentImageUri.toString(),
                    prediction = cancer.label,
                    confidence = confidence
                )
                intent.putExtra(ResultActivity.EXTRA_CLASSIFICATION, cancerClassification)
            } else {
                val cancerClassification = CancerClassification(
                    image = currentImageUri.toString(),
                    prediction = nonCancer.label,
                    confidence = confidenceNonCancer
                )
                intent.putExtra(ResultActivity.EXTRA_CLASSIFICATION, cancerClassification)
            }
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}