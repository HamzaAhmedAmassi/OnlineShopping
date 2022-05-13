package com.h.alamassi.onlineshoping.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.FragmentProductDescriptionBinding
import com.squareup.picasso.Picasso


class ProductDescriptionFragment : Fragment() {
    private lateinit var productDescriptionBinding: FragmentProductDescriptionBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var progressDialog: ProgressDialog
    private var catId = ""
    private var productId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        showDialog()
        firebaseFirestore = FirebaseFirestore.getInstance()
        productDescriptionBinding =
            FragmentProductDescriptionBinding.inflate(inflater, container, false)
        return productDescriptionBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Product Detailes"

        catId = arguments?.getString("catId") ?: ""
        productId = arguments?.getString("productId") ?: ""

        readData()

        productDescriptionBinding.btnUpdate.setOnClickListener {
            updateProductFragment()
        }
    }


    private fun readData() {
        firebaseFirestore
            .collection("categories")
            .document(catId)
            .collection("products")
            .whereEqualTo("productId", productId)
            .limit(1)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && !it.result.isEmpty) {
                    for (q in it.result) {
                        productDescriptionBinding.edName.setText(q.data["name"].toString())
                        productDescriptionBinding.edDescription.setText(q.data["description"].toString())
                        productDescriptionBinding.edPrice.setText(q.data["price"].toString())
                        productDescriptionBinding.edQuantity.setText(q.data["quantity"].toString())
                        Picasso.get().load(q.data["image"].toString())
                            .into(productDescriptionBinding.ivProductImage)

                    }
                }
            }
        hideDialog()
    }

    private fun updateProductFragment() {
        val bundle = Bundle()
        bundle.putString("productId", productId)
        bundle.putString("catId", catId)
        requireActivity().supportFragmentManager.beginTransaction().addToBackStack("")
            .replace(
                R.id.fragment_container,
                ProductEditFragment::class.java,
                bundle
            ).commit()
    }

    private fun showDialog() {
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading ....")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun hideDialog() {
        if (progressDialog.isShowing)
            progressDialog.dismiss()
    }
}

