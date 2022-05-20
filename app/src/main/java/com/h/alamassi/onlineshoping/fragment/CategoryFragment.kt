package com.h.alamassi.onlineshoping.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.MainActivity
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.adapter.CategoryAdapter
import com.h.alamassi.onlineshoping.databinding.FragmentCategoryBinding
import com.h.alamassi.onlineshoping.model.Category

class CategoryFragment : Fragment() {

    private lateinit var categoryBinding: FragmentCategoryBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var progressDialog: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        categoryBinding = FragmentCategoryBinding.inflate(inflater, container, false)
        showDialog()
        firebaseFirestore = FirebaseFirestore.getInstance()
        return categoryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Categories"

        readeData()
        categoryBinding.fabAddCategory.setOnClickListener {
            addCategoryFragment()
        }

    }


    private fun readeData() {
        firebaseFirestore.collection("categories")
            .get()
            .addOnCompleteListener { it ->
                if (it.isSuccessful && !it.result.isEmpty) {
                    val cats = it.result.map {
                        it.toObject(Category::class.java)
                    }
                    hideDialog()
                    val categoryAdapter = CategoryAdapter(
                        requireActivity() as MainActivity,
                        cats as ArrayList<Category>
                    )
                    categoryBinding.rvCategory.layoutManager =
                        LinearLayoutManager(requireActivity())
                    categoryBinding.rvCategory.adapter = categoryAdapter
                    categoryBinding.root.setOnClickListener {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .addToBackStack("")
                            .replace(R.id.fragment_container, ProductFragment()).commit()


                    }
                }

                hideDialog()
            }
    }

    private fun addCategoryFragment() {
        requireActivity().supportFragmentManager.beginTransaction().addToBackStack("")
            .replace(R.id.fragment_container, CreateCategoriesFragment()).commit()
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
