package com.h.alamassi.onlineshoping.adapter

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.ItemCategoryBinding
import com.h.alamassi.onlineshoping.fragment.CategoryEditFragment
import com.h.alamassi.onlineshoping.fragment.CategoryFragment
import com.h.alamassi.onlineshoping.fragment.ProductFragment
import com.h.alamassi.onlineshoping.fragment_user.ProductUserFragment
import com.h.alamassi.onlineshoping.model.Category
import com.squareup.picasso.Picasso


class CategoryAdapter(
    private var activity: AppCompatActivity,
    var data: ArrayList<Category>
) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog


    class MyViewHolder(var binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCategory = data[position]
        if (firebaseAuth.currentUser!!.uid == "bH2ND7OZnvR0drNd0vHiGGxaez33") {
            holder.binding.btnEdit.visibility = View.VISIBLE
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
            holder.binding.btnEdit.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("catId", currentCategory.catId)
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CategoryEditFragment::class.java, bundle)
                    .commit()

            }
        } else {
            holder.binding.root.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("catId", currentCategory.catId)
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProductUserFragment::class.java, bundle)
                    .commit()

            }

        }

        holder.binding.tvCategoryName.text = currentCategory.name
        Picasso
            .get()
            .load(currentCategory.image.ifEmpty { "a" })
            .placeholder(R.drawable.defult_category)
            .error(R.drawable.online_shopping_apps)
            .into(holder.binding.ivCategory)

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
