package com.h.alamassi.onlineshoping.adapter

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.ItemStoreBinding
import com.h.alamassi.onlineshoping.fragment.ProductsFragment
import com.h.alamassi.onlineshoping.model.Product

class CategoryAdapter(private var activity: AppCompatActivity, var data: ArrayList<Product>) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    class MyViewHolder(var binding: ItemStoreBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val li = LayoutInflater.from(parent.context)
        val binding = ItemStoreBinding.inflate(li, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        holder.binding.root.setOnLongClickListener {
//            val alertDialog = AlertDialog.Builder(activity)
//            alertDialog.setTitle("Delete Category")
//            alertDialog.setMessage("Are you sure to delete category ?")
//            alertDialog.setIcon(R.drawable.delete)
//            alertDialog.setPositiveButton("Yes") { _, _ ->
//                if (DatabaseHelper(activity).deleteCategory(data[position].id!!))
//                    data.removeAt(position)
//                Toast.makeText(activity, "Deleted Successfully", Toast.LENGTH_SHORT).show()
//            }
//            alertDialog.setNegativeButton("No") { _, _ ->
//            }
//            alertDialog.create().show()
//            true
//        }
//        val currentCategory = data[position]
//        holder.binding.root.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putInt("category_id", currentCategory.id ?: -1)
//            activity.supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, ProductsFragment::class.java, bundle).commit()
//        }
//
//        holder.binding.ivBook.setImageURI(Uri.parse(currentCategory.image))
//        holder.binding.tvCategory.text = currentCategory.name
    }

    override fun getItemCount(): Int {
        return data.size
    }
}