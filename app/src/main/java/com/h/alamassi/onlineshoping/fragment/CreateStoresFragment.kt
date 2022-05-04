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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.h.alamassi.onlineshoping.databinding.FragmentCreateStoresBinding


class CreateStoresFragment : Fragment() {

    companion object {
        private const val TAG = "CreateCategoryFragment"

        const val IMAGE_REQUEST_CODE = 102
    }

//    lateinit var databaseHelper: DatabaseHelper
    private lateinit var createCategoryBinding: FragmentCreateStoresBinding
    private var imagePath: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        createCategoryBinding = FragmentCreateStoresBinding.inflate(inflater, null, false)
        return createCategoryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        databaseHelper = DatabaseHelper(requireContext())

        createCategoryBinding.btnSaveCategory.setOnClickListener {
//            createCategory()
        }
        createCategoryBinding.fabChooseImage.setOnClickListener {
            chooseImage()
        }
    }

    private fun chooseImage() {
        val galleryPermission = ActivityCompat.checkSelfPermission(
            requireContext(),
            READ_EXTERNAL_STORAGE
        )
        if (galleryPermission != PackageManager.PERMISSION_DENIED) {
            //Open PickImageActivity
            chooseImageFromGallery()
        } else {
            //Ask User
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(READ_EXTERNAL_STORAGE),
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            if (data.data != null) {
                val split: Array<String> = data.data!!.path!!.split(":".toRegex()).toTypedArray() //split the path.
                val filePath = split[1] //assign it to a string(your choice).
                val bm = BitmapFactory.decodeFile(filePath)
                createCategoryBinding.ivCategoryImage.setImageBitmap(bm)

                imagePath = filePath
                Log.d(TAG, "onActivityResult: imagePath $imagePath")
            }
        }
    }

//    private fun createCategory() {
//        val name = createCategoryBinding.etCategoryName.text.toString()
//
//        if (name.isEmpty()) {
//            Toast.makeText(requireContext(), "Invalid data", Toast.LENGTH_SHORT).show()
//        } else {
//            val result = databaseHelper.createCategory(Category(name, imagePath))
//            if (result != -1L) {
//                Toast.makeText(
//                    requireContext(),
//                    "Category Created Successfully",
//                    Toast.LENGTH_SHORT
//                ).show()
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, CategoriesFragment()).commit()
//            } else {
//                Toast.makeText(
//                    requireContext(),
//                    "Something error, Please try again later",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }


}