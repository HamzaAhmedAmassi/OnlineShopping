package com.h.alamassi.onlineshoping.fragment_user

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.databinding.FragmentProductDescriptionBinding
import com.squareup.picasso.Picasso


class ProductDescriptionUserFragment : Fragment() {
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
        productDescriptionBinding.btnUpdate.visibility = View.INVISIBLE

        catId = ProductUserFragment.catId
        productId = arguments?.getString("productId") ?: ""

        Log.e("TAG", "onViewCreated Category ID : $catId")
        Log.e("TAG", "onViewCreated Product ID : $productId")

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

