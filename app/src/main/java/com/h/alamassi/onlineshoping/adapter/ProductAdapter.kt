package com.h.alamassi.onlineshoping.adapter

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.ItemProductBinding
import com.h.alamassi.onlineshoping.fragment.ProductDescriptionFragment
import com.h.alamassi.onlineshoping.model.Product

class ProductAdapter(private var activity: AppCompatActivity, var data: ArrayList<Product>) :
    RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {
    class MyViewHolder(var binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemProductBinding.inflate(activity.layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        holder.binding.root.setOnLongClickListener {
//            val alertDialog = AlertDialog.Builder(activity)
//            alertDialog.setTitle("Delete Book")
//            alertDialog.setMessage("Are you sure to delete book ?")
//            alertDialog.setIcon(R.drawable.delete)
//            alertDialog.setPositiveButton("Yes") { _, _ ->
//               if (DatabaseHelper(activity).deleteBook(data[position].id!!))
//                    data.removeAt(position)
//                Toast.makeText(activity, "Deleted Successfully", Toast.LENGTH_SHORT).show()
//            }
//            alertDialog.setNegativeButton("No") { _, _ ->
//            }
//            alertDialog.create().show()
//            true
//        }
//        val currentBook = data[position]
//
//        holder.binding.root.setOnClickListener {
//            val bundle = Bundle()
//            bundle.getLong("book_id", currentBook.id ?: -1)
//            activity.supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, ProductDescriptionFragment()).commit()
//        }
//        holder.binding.ivImageBook.setImageURI(Uri.parse(currentBook.image))
//        holder.binding.tvBookName.text = data[position].name
//        holder.binding.tvAuthor.text = data[position].author
//        holder.binding.tvCategory.text = data[position].categoryId.toString()
//        holder.binding.tvShelf.text = data[position].shelf
//        holder.binding.ibFavorite.setOnClickListener {
//        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

}
