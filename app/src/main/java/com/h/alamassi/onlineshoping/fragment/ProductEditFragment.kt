package com.h.alamassi.onlineshoping.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.FragmentProductEditBinding

class ProductEditFragment : Fragment() {
    private lateinit var productEditBinding: FragmentProductEditBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var productCollectionReference: CollectionReference

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Edit Product"

        firebaseFirestore = FirebaseFirestore.getInstance()

        catId = arguments?.getString("catId") ?: ""
        productId = arguments?.getString("productId") ?: ""

        productCollectionReference = firebaseFirestore
            .collection("categories")
            .document(catId)
            .collection("products")


        productCollectionReference
            .whereEqualTo("productId", productId)
            .limit(1)
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
        }


    }

    private fun chooseImage() {}

    private fun update() {
        val newName = productEditBinding.edName.text.toString()
        val newDescription = productEditBinding.edDescription.text.toString()
        val newPrice = productEditBinding.edPrice.text.toString()
        val newQuantity = productEditBinding.edQuantity.text.toString()
//        val image = productEditBinding.ivProductImage
        val newImage = ""


        val data = HashMap<String, Any>()
        data["name"] = newName
        data["description"] = newDescription
        data["image"] = newImage
        data["price"] = newPrice
        data["quantity"] = newQuantity
        productCollectionReference
            .document(productId)
            .update(data)
            .addOnSuccessListener {
                Toast.makeText(activity, "Updated Successfully", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        CategoryFragment()
                    ).commit()
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Updated Failed", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        ProductDescriptionFragment()
                    ).commit()

            }
    }

}




