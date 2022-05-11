package com.h.alamassi.onlineshoping.adapter

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.ItemCategoryBinding
import com.h.alamassi.onlineshoping.fragment.CategoryFragment
import com.h.alamassi.onlineshoping.fragment.ProductFragment
import com.h.alamassi.onlineshoping.model.Category

class CategoryAdapter(
    private var activity: AppCompatActivity,
    var data: ArrayList<Category>
) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var progressDialog: ProgressDialog


    class MyViewHolder(var binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        firebaseFirestore = FirebaseFirestore.getInstance()
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCategory = data[position]
        holder.binding.root.setOnLongClickListener {
            val alertDialog = AlertDialog.Builder(activity)
            alertDialog.setTitle("Delete Category")
            alertDialog.setMessage("Are you sure to delete category ?")
            alertDialog.setIcon(R.drawable.delete)
            alertDialog.setPositiveButton("Yes") { _, _ ->
                showDialog()
                firebaseFirestore
                    .collection("categories")
                    .document(currentCategory.catId)
                    .delete()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            hideDialog()
                            Toast.makeText(
                                activity,
                                "Deleted Successfully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            activity.supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, CategoryFragment()).commit()

                        } else {
                            Toast.makeText(activity, "Something Error ", Toast.LENGTH_LONG)
                                .show()
                            hideDialog()
                        }


                    }

            }
            alertDialog.setNegativeButton("No") { _, _ ->
            }
            alertDialog.create().show()

            true

        }
        holder.binding.root.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("catId", currentCategory.catId)
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProductFragment::class.java, bundle).commit()

        }
//        holder.binding.ivCategory.setImageURI(Uri.parse(currentCategory.image))
        holder.binding.tvCategoryName.text = currentCategory.name
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun showDialog() {
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Loading ....")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun hideDialog() {
        if (progressDialog.isShowing)
            progressDialog.dismiss()
    }
}
