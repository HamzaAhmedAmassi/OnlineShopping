package com.h.alamassi.onlineshoping.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseFirestore = FirebaseFirestore.getInstance()

        val catId = arguments?.getString("catId") ?: ""
        val productId = arguments?.getString("productId") ?: ""


        firebaseFirestore
            .collection("categories")
            .document(catId)
            .collection("products")
            .whereEqualTo("productId", productId)
            .limit(1)
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

