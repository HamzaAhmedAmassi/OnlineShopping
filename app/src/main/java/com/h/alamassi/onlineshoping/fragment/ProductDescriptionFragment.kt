package com.h.alamassi.onlineshoping.fragment

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.FragmentProductDescriptionBinding


class ProductDescriptionFragment : Fragment() {
//    private lateinit var databaseHelper: DatabaseHelper
//    private lateinit var productDescriptionBinding: FragmentProductDescriptionBinding
//    private var imagePath: String = ""
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        productDescriptionBinding = FragmentProductDescriptionBinding.inflate(inflater, container, false)
//        return productDescriptionBinding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val ProductId = arguments?.getLong("Product_id") ?: -1
//        databaseHelper = DatabaseHelper(requireContext())
//        if (ProductId == -1L) {
//            val login = Intent(activity, CategoriesFragment::class.java)
//            startActivity(login)
//        } else {
//            val Products = databaseHelper.getDescriptionProducts(ProductId)
//            productDescriptionBinding.tvYear.text = Products.year
//            productDescriptionBinding.tvAuthor.text = Products.author
//            productDescriptionBinding.tvProductName.text = Products.name
//            productDescriptionBinding.tvDescription.text = Products.description
//            productDescriptionBinding.tvLanguage.text = Products.language
//            productDescriptionBinding.tvNuOfCopies.text = Products.numOfCopies
//            productDescriptionBinding.tvNumOfPages.text = Products.numOfPages
//            productDescriptionBinding.tvShelf.text = Products.shelf
//            val bm = BitmapFactory.decodeFile(Products.image!!)
//            productDescriptionBinding.ivImageProduct.setImageBitmap(bm)
//            productDescriptionBinding.ibEdit.setOnClickListener {
//                val bundle = Bundle()
//                bundle.putLong("Product_id", ProductId)
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container,ProductEditFragment::class.java, bundle).commit()
//            }
//        }
//
//    }
//

}