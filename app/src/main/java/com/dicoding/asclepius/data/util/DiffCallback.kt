package com.dicoding.asclepius.data.util

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.asclepius.data.database.CancerClassification

class CancerClassificationDiffCallback (private val oldList: List<CancerClassification>, private val newList: List<CancerClassification>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}