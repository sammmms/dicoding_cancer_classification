package com.dicoding.asclepius.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.databinding.ArticleItemBinding

class ArticlesAdapter: RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder>() {
    private val listArticles = ArrayList<ArticlesItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<ArticlesItem>){
        listArticles.clear()
        listArticles.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        val view = ArticleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticlesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        holder.bind(listArticles[position])
    }

    override fun getItemCount(): Int {
        return listArticles.size
    }

    inner class ArticlesViewHolder(private val binding: ArticleItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(articlesItem: ArticlesItem) {
            with(binding){
                articleTitle.text = articlesItem.title
                articleDescription.text = articlesItem.description
                Glide.with(itemView.context)
                    .load(articlesItem.urlToImage)
                    .into(articleImage)
        }
    }}
}