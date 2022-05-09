package com.h.alamassi.onlineshoping.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.MainActivity
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.adapter.ProductAdapter
import com.h.alamassi.onlineshoping.databinding.FragmentProductBinding
import com.h.alamassi.onlineshoping.model.Product


class ProductFragment : Fragment() {
    private lateinit var productBinding: FragmentProductBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        productBinding = FragmentProductBinding.inflate(inflater, container, false)
        return productBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore.collection("products")
            .get()
            .addOnCompleteListener { it ->
                if (it.isSuccessful && !it.result.isEmpty) {
                    showDialog()
                    val products = it.result.map {
                        it.toObject(Product::class.java)

                    }
                    val productAdapter = ProductAdapter(
                        requireActivity() as MainActivity,
                        products as ArrayList<Product>
                    )
                    productBinding.rvBook.layoutManager =
                        GridLayoutManager(requireActivity(), 2)
                    productBinding.rvBook.adapter = productAdapter
                    hideDialog()
                    productBinding.root.setOnClickListener {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, ProductDescriptionFragment()).commit()


                    }
                }



                productBinding.fabAddBook.setOnClickListener {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CreateProductFragment()).commit()
                }
            }

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