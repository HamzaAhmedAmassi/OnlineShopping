package com.h.alamassi.onlineshoping.fragment


import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.FragmentCreateCategoriesBinding
import java.util.*


class CreateCategoriesFragment : Fragment() {

    private lateinit var createCategoryBinding: FragmentCreateCategoriesBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var progressDialog: ProgressDialog
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private var categoriesCollectionReference: CollectionReference? = null
    private var categoryName = ""
    private var imagePath: Uri? = null

    companion object {
        const val IMAGE_REQUEST_CODE = 102
        var catId: String? = ""
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        firebaseFirestore = FirebaseFirestore.getInstance()
        createCategoryBinding = FragmentCreateCategoriesBinding.inflate(inflater, null, false)
        return createCategoryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Crate Category"


        createCategoryBinding.btnSave.setOnClickListener {
            showDialog()
            createCategory()
        }
        createCategoryBinding.fabChooseImage.setOnClickListener {
            chooseImage()
        }
    }

    private fun createCategory() {
        categoryName = createCategoryBinding.etCategoryName.text.toString()
        categoriesCollectionReference = firebaseFirestore.collection("categories")
        if (categoryName.isEmpty()) {
            Toast.makeText(requireContext(), "Name required", Toast.LENGTH_SHORT).show()
        } else {
            uploadImage()
        }
    }

    private fun uploadImage() {
        if (imagePath != null) {
            val imageName = "${UUID.randomUUID()}.jpeg"
            val ref = storageReference.child("images/$imageName")

            ref.putFile(imagePath!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        storeCategoryInDB(it.toString())
                    }
                }
        }
    }

    private fun storeCategoryInDB(imageURI: String) {
        catId = categoriesCollectionReference!!.document().id
        val data = HashMap<String, String>()
        data["name"] = categoryName
        data["image"] = imageURI
        data["catId"] = catId!!
        categoriesCollectionReference!!.document(catId!!).set(data)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Created Successfully", Toast.LENGTH_LONG)
                        .show()
                    requireActivity().supportFragmentManager.beginTransaction().addToBackStack("")
                        .replace(R.id.fragment_container, CategoryFragment()).commit()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Something error, Please try again later",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                hideDialog()
            }
    }

    private fun chooseImage() {
        val galleryPermission = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (galleryPermission != PackageManager.PERMISSION_DENIED) {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(
                requireContext() as Activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                IMAGE_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imagePath = data!!.data
            createCategoryBinding.ivCategoryImage.setImageURI(imagePath)
        }
    }

    private fun showDialog() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Creating ....")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun hideDialog() {
        if (progressDialog.isShowing)
            progressDialog.dismiss()
    }
}
