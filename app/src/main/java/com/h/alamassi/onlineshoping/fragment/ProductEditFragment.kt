package com.h.alamassi.onlineshoping.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.FragmentProductDescriptionBinding
import com.h.alamassi.onlineshoping.databinding.FragmentProductEditBinding
import com.h.alamassi.onlineshoping.model.Product

class ProductEditFragment : Fragment() {
    private lateinit var productEditBinding: FragmentProductEditBinding
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        productEditBinding =
            FragmentProductEditBinding.inflate(inflater, container, false)
        return productEditBinding.root
    }

    @SuppressLint("LogConditional")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val catId = arguments?.getString("catId") ?: ""
        val productId = arguments?.getString("productId") ?: ""

        Log.d("TAG", "catId: ${arguments?.getString("catId") ?: ""}")
        Log.d("TAG", "productId: ${arguments?.getString("productId") ?: ""}")

        super.onViewCreated(view, savedInstanceState)
        firebaseFirestore = FirebaseFirestore.getInstance()

        firebaseFirestore
            .collection("categories")
            .document(catId)
            .collection("products")
            .whereEqualTo("productId", productId)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && !it.result.isEmpty) {
                    for (q in it.result) {
                        productEditBinding.edName.setText(q.data["name"].toString())
                        productEditBinding.edDescription.setText(q.data["description"].toString())
                        productEditBinding.edPrice.setText(q.data["price"].toString())
                        productEditBinding.edQuantity.setText(q.data["quantity"].toString())
//                        val bm = BitmapFactory.decodeFile(q.data["image"].toString())
//                        productDescriptionBinding.ivProductImage.setImageBitmap(bm)
                    }
                }
            }
        productEditBinding.fabChooseImage.setOnClickListener {
            chooseImage()
        }
        productEditBinding.btnUpdate.setOnClickListener {
            update()

//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(
//                    R.id.fragment_container,
//                    ProductFragment()).commit()
        }


    }

    private fun chooseImage() {}

    private fun update() {

    }
}

