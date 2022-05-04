package com.h.alamassi.onlineshoping.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.h.alamassi.onlineshoping.MainActivity
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.adapter.CategoryAdapter
import com.h.alamassi.onlineshoping.databinding.FragmentStoresBinding

class StoresFragment : Fragment() {

//    lateinit var databaseHelper: DatabaseHelper
//    lateinit var categoryBinding: FragmentCategoryBinding
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        categoryBinding = FragmentCategoryBinding.inflate(inflater, container, false)
//        return categoryBinding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        databaseHelper = DatabaseHelper(requireContext())
//
//        val data = databaseHelper.getAllCategories()
//        val productAdapter = CategoryAdapter(requireActivity() as MainActivity, data)
//
//        categoryBinding.rvCategory.layoutManager = GridLayoutManager(requireActivity(), 3)
//        categoryBinding.rvCategory.adapter = productAdapter
//        categoryBinding.root.setOnClickListener {
//            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_container,ProductsFragment()).commit()
//
//        }
//
//        categoryBinding.fabAddCategory.setOnClickListener {
//            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_container,CreateCategoryFragment()).commit()
//        }
//    }

}