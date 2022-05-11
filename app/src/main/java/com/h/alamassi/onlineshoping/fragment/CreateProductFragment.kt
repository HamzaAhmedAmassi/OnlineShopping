package com.h.alamassi.onlineshoping.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.FragmentCreateProductBinding


class CreateProductFragment : Fragment() {
    private lateinit var createProductBinding: FragmentCreateProductBinding
    private lateinit var firebaseFirestore: FirebaseFirestore


    private var imagePath: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createProductBinding = FragmentCreateProductBinding.inflate(inflater, container, false)
        return createProductBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseFirestore = FirebaseFirestore.getInstance()

        createProductBinding.btnSave.setOnClickListener {
            createProduct()
        }
        createProductBinding.fabChooseImage.setOnClickListener {
            chooseImage()
        }
    }

    private fun chooseImage() {}


    private fun createProduct() {
        val name = createProductBinding.edName.text.toString()
        val description = createProductBinding.edDescription.text.toString()
        val image = imagePath
        val price = createProductBinding.edPrice.text.toString()
        val quantity = createProductBinding.edQuantity.text.toString()

        val catId = arguments?.getString("catId") ?: ""

        val productCollectionReference = firebaseFirestore
            .collection("categories")
            .document(catId)
            .collection("products")

        when {
            name.isEmpty() -> {
                Toast.makeText(requireContext(), "Name required", Toast.LENGTH_SHORT).show()
            }
            price.isEmpty() -> {

                Toast.makeText(requireContext(), "Price required", Toast.LENGTH_SHORT).show()
            }
            description.isEmpty() -> {
                Toast.makeText(requireContext(), "Description required", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val productId = productCollectionReference.document().id
                val data = HashMap<String, String>()
                data["productId"] = productId
                data["name"] = name
                data["description"] = description
                data["image"] = image
                data["price"] = price
                data["quantity"] = quantity
                productCollectionReference.document(productId).set(data)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(context, "Created Successfully", Toast.LENGTH_LONG)
                                .show()
                            val bundle = Bundle()
                            bundle.putString("catId",catId)
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, ProductFragment::class.java,bundle).commit()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Something error, Please try again later",
                                Toast.LENGTH_SHORT
                            ).show()
                            requireActivity().onBackPressed()

                        }
                    }
            }
        }
    }
}
