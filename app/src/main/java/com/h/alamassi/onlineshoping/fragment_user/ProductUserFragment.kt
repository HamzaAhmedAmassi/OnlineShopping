package com.h.alamassi.onlineshoping.fragment_user

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.MainActivity
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.adapter.ProductAdapter
import com.h.alamassi.onlineshoping.databinding.FragmentProductBinding
import com.h.alamassi.onlineshoping.model.Product


class ProductUserFragment : Fragment() {
    private lateinit var productBinding: FragmentProductBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var progressDialog: ProgressDialog

    companion object {
        var catId = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        showDialog()
        firebaseFirestore = FirebaseFirestore.getInstance()
        productBinding = FragmentProductBinding.inflate(inflater, container, false)
        return productBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Products"

        catId = arguments?.getString("catId") ?: ""

        readData()
        productBinding.fabAddProduct.visibility = View.INVISIBLE

    }


    private fun readData() {
        firebaseFirestore
            .collection("categories")
            .document(catId)
            .collection("products")
            .get()
            .addOnCompleteListener { it ->
                if (it.isSuccessful && !it.result.isEmpty) {
                    val products = it.result.map {
                        it.toObject(Product::class.java)
                    }
                    hideDialog()
                    val productAdapter = ProductAdapter(
                        requireActivity() as MainActivity,
                        products as ArrayList<Product>
                    )
                    productBinding.rvBook.layoutManager =
                        LinearLayoutManager(requireActivity())
                    productBinding.rvBook.adapter = productAdapter
                    productBinding.root.setOnClickListener {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .addToBackStack("")
                            .replace(
                                R.id.fragment_container,
                                ProductDescriptionUserFragment()
                            ).commit()
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
