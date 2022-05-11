package com.h.alamassi.onlineshoping.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.FragmentProductEditBinding

class ProductEditFragment : Fragment() {
    private lateinit var productEditBinding: FragmentProductEditBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var productId: String
    lateinit var catId: String

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
        catId = arguments?.getString("catId") ?: ""
        productId = arguments?.getString("productId") ?: ""

        Log.d("TAG", "catIdEdit: ${arguments?.getString("catId") ?: ""}")
        Log.d("TAG", "productIdEdit: ${arguments?.getString("productId") ?: ""}")

        super.onViewCreated(view, savedInstanceState)
        firebaseFirestore = FirebaseFirestore.getInstance()
        Log.e("TAG", "cat id: PEF $catId")
        Log.e("TAG", "product id: PEF $productId")
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
        val name = productEditBinding.edName.text.toString()
        val description = productEditBinding.edDescription.text.toString()
        val price = productEditBinding.edPrice.text.toString()
        val quantity = productEditBinding.edQuantity.text.toString()
//        val image = productEditBinding.ivProductImage
        val image = ""


        val map = HashMap<String, Any>()
        map["name"] = name
        map["description"] = description
        map["price"] = price
        map["quantity"] = quantity
        map["image"] = image
        firebaseFirestore.collection("categories").document(catId).collection("product")
            .document(productId)
            .update(map)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(activity, "Updated Successfully", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ProductDescriptionFragment()).commit()
                } else {
                    Toast.makeText(activity, "Updated Failed", Toast.LENGTH_SHORT).show()
                    Log.e("TAG", "Cat Id : $catId")
                    Log.e("TAG", "Product Id : $productId")
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ProductDescriptionFragment()).commit()

                }
            }

    }


}

