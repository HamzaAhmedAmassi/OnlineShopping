package com.h.alamassi.onlineshoping.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.MainActivity
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.adapter.CategoryAdapter
import com.h.alamassi.onlineshoping.databinding.FragmentCategoryBinding
import com.h.alamassi.onlineshoping.model.Category

class CategoryFragment : Fragment() {

    private lateinit var categoryBinding: FragmentCategoryBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        categoryBinding = FragmentCategoryBinding.inflate(inflater, container, false)
        return categoryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore.collection("categories")
            .get()
            .addOnCompleteListener { it ->
                if (it.isSuccessful && !it.result.isEmpty) {
                showDialog()
                    val cats = it.result.map {
                        it.toObject(Category::class.java)

                    }
                    val categoryAdapter = CategoryAdapter(
                        requireActivity() as MainActivity,
                        cats as ArrayList<Category>
                    )
                    categoryBinding.rvCategory.layoutManager =
                        GridLayoutManager(requireActivity(), 2)
                    categoryBinding.rvCategory.adapter = categoryAdapter
                    hideDialog()
                    categoryBinding.root.setOnClickListener {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, ProductFragment()).commit()


                    }
                }



                categoryBinding.fabAddCategory.setOnClickListener {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CreateCategoriesFragment()).commit()
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