package com.h.alamassi.onlineshoping.fragment

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.FragmentProductDescriptionBinding


class ProductDescriptionFragment : Fragment() {
    private lateinit var productDescriptionBinding: FragmentProductDescriptionBinding
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        productDescriptionBinding =
            FragmentProductDescriptionBinding.inflate(inflater, container, false)
        return productDescriptionBinding.root
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
                        productDescriptionBinding.edName.setText(q.data["name"].toString())
                        productDescriptionBinding.edDescription.setText(q.data["description"].toString())
                        productDescriptionBinding.edPrice.setText(q.data["price"].toString())
                        productDescriptionBinding.edQuantity.setText(q.data["quantity"].toString())
//                        val bm = BitmapFactory.decodeFile(q.data["image"].toString())
//                        productDescriptionBinding.ivProductImage.setImageBitmap(bm)
                    }
                }
            }
        productDescriptionBinding.btnUpdate.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("productId", productId)
            bundle.putString("catId",catId)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    ProductEditFragment::class.java,
                    bundle
                ).commit()
        }


    }
}

