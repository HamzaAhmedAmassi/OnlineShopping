package com.h.alamassi.onlineshoping.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.adapter.ProductAdapter
import com.h.alamassi.onlineshoping.MainActivity
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.FragmentProductBinding


class ProductFragment : Fragment() {
    private lateinit var productBinding: FragmentProductBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        productBinding = FragmentProductsBinding.inflate(inflater, container, false)
//        return productBinding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        firebaseFirestore = FirebaseFirestore.getInstance()
//        firebaseAuth = FirebaseAuth.getInstance()
//        val categoryId = firebaseAuth.currentUser
//        // TODO: 12/24/2021 Init databaseHelper object
//        if (categoryId == null) {
//
//        } else {
//            val products = databaseHelper.getCategoryProducts(categoryId)
//            productBinding.rvProduct.layoutManager = LinearLayoutManager(requireActivity())
//            val productAdapter = ProductAdapter(requireContext() as MainActivity, products)
//            productBinding.rvroduct.adapter = ProductAdapter
//
//            productBinding.fabAddProduct.setOnClickListener {
//                val bundle = Bundle()
//                bundle.putLong("category_id", categoryId)
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, CreateProductFragment::class.java, bundle)
//                    .commit()
//            }
//        }
//
//
//    }
}