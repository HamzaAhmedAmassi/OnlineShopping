package com.h.alamassi.onlineshoping.adapter

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.ItemProductBinding
import com.h.alamassi.onlineshoping.fragment.CategoryFragment
import com.h.alamassi.onlineshoping.fragment.ProductDescriptionFragment
import com.h.alamassi.onlineshoping.fragment.ProductFragment
import com.h.alamassi.onlineshoping.fragment_user.ProductDescriptionUserFragment
import com.h.alamassi.onlineshoping.model.Product
import com.squareup.picasso.Picasso

class ProductAdapter(
    private var activity: AppCompatActivity,
    private var data: ArrayList<Product>
) :
    RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    class MyViewHolder(var binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemProductBinding.inflate(activity.layoutInflater, parent, false)
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val catId = ProductFragment.catId
        val currentProduct = data[position]
        if (firebaseAuth.currentUser!!.uid == "36HdizWdowNYJtTA995v2vJGngH2") {
            holder.binding.root.setOnLongClickListener {
                val alertDialog = AlertDialog.Builder(activity)
                alertDialog.setTitle("Delete Category")
                alertDialog.setMessage("Are you sure to delete category ?")
                alertDialog.setIcon(R.drawable.delete)
                alertDialog.setPositiveButton("Yes") { _, _ ->
                    showDialog()
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
                    .replace(
                        R.id.fragment_container,
                        ProductDescriptionFragment::class.java,
                        bundle
                    )
                    .commit()
            }
        } else {

            holder.binding.root.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("productId", currentProduct.productId)
                bundle.putString("catId", catId)
                activity.supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        ProductDescriptionUserFragment::class.java,
                        bundle
                    )
                    .commit()
            }
        }
        holder.binding.tvPrice.text = currentProduct.price
        holder.binding.tvProductName.text = currentProduct.name
        Picasso
            .get()
            .load(currentProduct.image.ifEmpty { "a" })
            .placeholder(R.drawable.default_product)
            .error(R.drawable.online_shopping_image)
            .into(holder.binding.ivImageBook)
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
