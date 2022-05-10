package com.h.alamassi.onlineshoping.fragment

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.FragmentCreateCategoriesBinding


class CreateCategoriesFragment : Fragment() {

    private lateinit var createCategoryBinding: FragmentCreateCategoriesBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    companion object {
        private const val TAG = "CreateCategoryFragment"
        const val IMAGE_REQUEST_CODE = 102
        var categoryName = ""
    }

    private var imagePath: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        createCategoryBinding = FragmentCreateCategoriesBinding.inflate(inflater, null, false)
        return createCategoryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        createCategoryBinding.btnSaveCategory.setOnClickListener {
            createCategory()
        }
        createCategoryBinding.fabChooseImage.setOnClickListener {
            chooseImage()
        }
    }

    private fun chooseImage() {}


    private fun createCategory() {
        categoryName = createCategoryBinding.etCategoryName.text.toString()
        val image = imagePath
        val categoriesCollectionReference = firebaseFirestore.collection("categories")

        if (categoryName.isEmpty()) {
            Toast.makeText(requireContext(), "Name required", Toast.LENGTH_SHORT).show()
        } else {
            val catId = categoriesCollectionReference.document().id
            val data = HashMap<String, String>()
            data["name"] = categoryName
            data["image"] = image
            data["catId"] = catId
            categoriesCollectionReference.document(catId).set(data)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Created Successfully", Toast.LENGTH_LONG)
                            .show()
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, CategoryFragment()).commit()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Something error, Please try again later",
                            Toast.LENGTH_SHORT
                        ).show()
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, CategoryFragment()).commit()
                    }
                }


        }
    }
}