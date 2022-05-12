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
        return categoryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Categories"

        firebaseFirestore = FirebaseFirestore.getInstance()

        showDialog()

        firebaseFirestore.collection("categories")
            .get()
            .addOnCompleteListener { it ->
                if (it.isSuccessful && !it.result.isEmpty) {
                    val cats = it.result.map {
                        it.toObject(Category::class.java)

                    }
                    val categoryAdapter = CategoryAdapter(
                        requireActivity() as MainActivity,
                        cats as ArrayList<Category>
                    )
                    categoryBinding.rvCategory.layoutManager =
                        LinearLayoutManager(requireActivity())
                    categoryBinding.rvCategory.adapter = categoryAdapter
                    hideDialog()
                    categoryBinding.root.setOnClickListener {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, ProductFragment()).commit()


                    }
                }else{
                    hideDialog()
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
