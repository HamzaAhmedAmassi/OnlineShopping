package com.h.alamassi.onlineshoping.adapter

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.ItemProductBinding
import com.h.alamassi.onlineshoping.fragment.CategoryFragment
import com.h.alamassi.onlineshoping.fragment.ProductDescriptionFragment
import com.h.alamassi.onlineshoping.fragment.ProductFragment
import com.h.alamassi.onlineshoping.model.Product

class ProductAdapter(
    private var activity: AppCompatActivity,
    private var data: ArrayList<Product>
) :
    RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var progressDialog: ProgressDialog

    class MyViewHolder(var binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemProductBinding.inflate(activity.layoutInflater, parent, false)
        firebaseFirestore = FirebaseFirestore.getInstance()

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val catId = ProductFragment.catId
        val currentProduct = data[position]
        showDialog()
        holder.binding.root.setOnLongClickListener {
            val alertDialog = AlertDialog.Builder(activity)
            alertDialog.setTitle("Delete Category")
            alertDialog.setMessage("Are you sure to delete category ?")
            alertDialog.setIcon(R.drawable.delete)
            alertDialog.setPositiveButton("Yes") { _, _ ->
                firebaseFirestore
                    .collection("categories")
                    .document(catId)
                    .collection("products")
                    .document(currentProduct.productId)
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
                                .replace(R.id.fragment_container, CategoryFragment())
                                .commit()
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
            bundle.putString("productId", currentProduct.productId)
            bundle.putString("catId", catId)
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProductDescriptionFragment::class.java, bundle)
                .commit()
        }
        holder.binding.tvPrice.text = currentProduct.price
        holder.binding.tvProductName.text = currentProduct.name
//      holder.binding.ivCategory.setImageURI(Uri.parse(currentCategory.image))
        hideDialog()
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
