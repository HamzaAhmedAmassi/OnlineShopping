package com.h.alamassi.onlineshoping.fragment

import android.Manifest
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
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.FragmentCreateProductBinding
import com.h.alamassi.onlineshoping.model.Product


class CreateProductFragment : Fragment() {
//    companion object {
//        private const val TAG = "CreateProductFragment"
//        const val IMAGE_REQUEST_CODE = 103
//    }
//
//    lateinit var databaseHelper: DatabaseHelper
//    private lateinit var createProductBinding: FragmentCreateProductBinding
//    private var imagePath: String = ""
//    var categoryId: Long = -1L
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        createProductBinding = FragmentCreateProductBinding.inflate(inflater, container, false)
//        return createProductBinding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        databaseHelper = DatabaseHelper(requireContext())
//        categoryId = arguments?.getLong("category_id") ?: -1
//        if (categoryId == -1L) {
//            return
//        }
//
//
//
//        createProductBinding.btnCreateProduct.setOnClickListener {
//            createProduct()
//        }
//        createProductBinding.fabChooseImage.setOnClickListener {
//            chooseImage()
//        }
//    }
//
//    private fun chooseImage() {
//        val galleryPermission = ActivityCompat.checkSelfPermission(
//            requireContext(),
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        )
//        if (galleryPermission != PackageManager.PERMISSION_DENIED) {
//            //Open PickImageActivity
//            chooseImageFromGallery()
//        } else {
//            //Ask User
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                IMAGE_REQUEST_CODE
//            )
//        }
//    }
//
//    private fun chooseImageFromGallery() {
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "image/*"
//        startActivityForResult(
//            intent,
//            IMAGE_REQUEST_CODE
//        )
//    }
//
//    private fun createProduct() {
//        val productName = createProductBinding.edName.text.toString()
//        val productAuthor = createProductBinding.edAuthor.text.toString()
//        val productYear = createProductBinding.edYear.text.toString()
//        val productCopies = createProductBinding.edCopies.text.toString()
//        val productImage = imagePath
//        val productPages = createProductBinding.edPages.text.toString()
//        val productDescription = createProductBinding.edDescription.text.toString()
//        val productLanguage = createProductBinding.edLanguage.text.toString()
//        val productShelf = createProductBinding.edShelf.text.toString()
//        if (productName.isEmpty() && productAuthor.isEmpty() && productDescription.isEmpty() && productLanguage.isEmpty() && productShelf.isEmpty()) {
//            Toast.makeText(requireContext(), "Invalid data", Toast.LENGTH_SHORT).show()
//        } else {
//            val product = Product(
//                productName,
//                productAuthor,
//                productYear,
//                categoryId,
//                productDescription,
//                productLanguage,
//                productCopies,
//                productPages,
//                productShelf,
//                productImage
//            )
//
//            val result = databaseHelper.insertProduct(Product)
//            if (result != -1L) {
//                Toast.makeText(requireContext(), "Product Created Successfully", Toast.LENGTH_SHORT)
//                    .show()
//            } else {
//                Toast.makeText(
//                    requireContext(),
//                    "Something error, Check data again",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, ProductsFragment()).commit()
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
//            if (data.data != null) {
//                val split: Array<String> =
//                    data.data!!.path!!.split(":".toRegex()).toTypedArray() //split the path.
//                val filePath = split[1] //assign it to a string(your choice).
//                val bm = BitmapFactory.decodeFile(filePath)
//                createProductBinding.ivProductImage.setImageBitmap(bm)
//
//                imagePath = filePath
//                Log.d(TAG, "onActivityResult: imagePath $imagePath")
//            }
//        }
//    }

}