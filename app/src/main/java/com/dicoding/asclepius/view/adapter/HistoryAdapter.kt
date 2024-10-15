package com.dicoding.asclepius.view.adapter

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.database.CancerClassification
import com.dicoding.asclepius.data.util.CancerClassificationDiffCallback
import com.dicoding.asclepius.data.util.CancerClassificationHelper
import com.dicoding.asclepius.databinding.ClassificationItemBinding
import com.dicoding.asclepius.view.ResultActivity

class HistoryAdapter(private val application: Application) : RecyclerView.Adapter<HistoryAdapter.CancerClassificationViewHolder>() {
    private val listClassification = ArrayList<CancerClassification>()
    private var cancerClassificationHelper: CancerClassificationHelper? = null
    fun setData(list: List<CancerClassification>) {
        val diffCallback = CancerClassificationDiffCallback(listClassification, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listClassification.clear()
        listClassification.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CancerClassificationViewHolder {
        val view = ClassificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        cancerClassificationHelper = CancerClassificationHelper(application)
        return CancerClassificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: CancerClassificationViewHolder, position: Int) {
        holder.bind(listClassification[position])
    }

    override fun getItemCount(): Int {
        return listClassification.size
    }

    inner class CancerClassificationViewHolder(private val binding: ClassificationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("StringFormatInvalid")
        fun bind(cancerClassification: CancerClassification) {
            with(binding){
                classificationImage.setImageURI(Uri.parse(cancerClassification.image))
                classificationLabel.text = cancerClassification.prediction
                classificationPrediction.text = itemView.context.getString(R.string.percentation, (cancerClassification.confidence * 100).toInt())
                classificationItem.setOnClickListener{
                    val intent = Intent(itemView.context, ResultActivity::class.java)
                    intent.putExtra(ResultActivity.EXTRA_CLASSIFICATION, cancerClassification)
                    itemView.context.startActivity(intent)
                }
                classificationDelete.setImageDrawable(AppCompatResources.getDrawable(itemView.context, R.drawable.baseline_delete_24))
                classificationDelete.setOnClickListener{
                    cancerClassificationHelper?.deleteClassification(cancerClassification)
                    listClassification.remove(cancerClassification)
                    notifyItemRemoved(adapterPosition)
                }
            }
        }
    }
}