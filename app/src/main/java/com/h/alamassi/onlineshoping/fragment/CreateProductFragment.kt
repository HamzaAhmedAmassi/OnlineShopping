package com.h.alamassi.onlineshoping.fragment

import android.Manifest
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
import com.h.alamassi.onlineshoping.databinding.FragmentCreateProductBinding


class CreateProductFragment : Fragment() {
    private lateinit var createProductBinding: FragmentCreateProductBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    companion object {
        private const val TAG = "CreateProductFragment"
        const val IMAGE_REQUEST_CODE = 103
    }

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
        firebaseAuth = FirebaseAuth.getInstance()

        createProductBinding.btnSave.setOnClickListener {
            createProduct()
        }
        createProductBinding.fabChooseImage.setOnClickListener {
//            chooseImage()
        }
    }

    private fun chooseImage() {
        val galleryPermission = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (galleryPermission != PackageManager.PERMISSION_DENIED) {
            //Open PickImageActivity
            chooseImageFromGallery()
        } else {
            //Ask User
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                IMAGE_REQUEST_CODE
            )
        }
    }

    private fun chooseImageFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(
            intent,
            IMAGE_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            if (data.data != null) {
                val split: Array<String> =
                    data.data!!.path!!.split(":".toRegex()).toTypedArray() //split the path.
                val filePath = split[1] //assign it to a string(your choice).
                val bm = BitmapFactory.decodeFile(filePath)
                createProductBinding.ivBookImage.setImageBitmap(bm)

                imagePath = filePath
                Log.d(TAG, "onActivityResult: imagePath $imagePath")
            }
        }
    }

    private fun createProduct() {
        val name = createProductBinding.edName.text.toString()
        val description = createProductBinding.edDescription.text.toString()
        val image = imagePath
        val price = createProductBinding.edPrice.text.toString()
        val quantity = createProductBinding.edQuantity.text.toString()

        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Name required", Toast.LENGTH_SHORT).show()
        } else if (price.isEmpty()) {

            Toast.makeText(requireContext(), "Price required", Toast.LENGTH_SHORT).show()
        } else if (description.isEmpty()) {
            Toast.makeText(requireContext(), "Description required", Toast.LENGTH_SHORT).show()
        } else {

            val data = HashMap<String, String>()
            data["productId"] = firebaseFirestore.collection("products").document().id
            data["catId"] = firebaseFirestore.collection("categories").document().id
            data["name"] = name
            data["description"] = description
            data["image"] = image
            data["price"] = price
            data["quantity"] = quantity
            firebaseFirestore.collection("products").add(data)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Created Successfully", Toast.LENGTH_LONG)
                            .show()
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, ProductFragment()).commit()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Something error, Please try again later",
                            Toast.LENGTH_SHORT
                        ).show()
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, ProductFragment()).commit()
                    }
                }
        }
    }
}
