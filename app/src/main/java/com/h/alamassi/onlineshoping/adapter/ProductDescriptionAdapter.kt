package com.h.alamassi.onlineshoping.adapter

import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.h.alamassi.onlineshoping.databinding.FragmentProductDescriptionBinding
import com.h.alamassi.onlineshoping.model.Product

class ProductDescriptionAdapter(private var activity: AppCompatActivity, var data: ArrayList<Product>) :
    RecyclerView.Adapter<ProductDescriptionAdapter.MyViewHolder>() {
    class MyViewHolder(var binding: FragmentProductDescriptionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = FragmentProductDescriptionBinding.inflate(activity.layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//
//        val currentBook = data[position]
//        val bundle = Bundle()
//        bundle.getLong("book_id", currentBook.id!!)
//        holder.binding.ivImageBook.setImageURI(Uri.parse(currentBook.image))
//        holder.binding.tvBookName.text = currentBook.name
//        holder.binding.tvAuthor.text = currentBook.author
//        holder.binding.tvCategory.text = currentBook.categoryId.toString()
//        holder.binding.tvShelf.text = currentBook.name
//        holder.binding.tvLanguage.text = currentBook.language
//        holder.binding.tvDescription.text = currentBook.description
//        holder.binding.tvNuOfCopies.text = currentBook.numOfCopies
//        holder.binding.tvNumOfPages.text = currentBook.numOfPages
//        holder.binding.tvYear.text = currentBook.year

    }

    override fun getItemCount(): Int {
        return data.size
    }

}