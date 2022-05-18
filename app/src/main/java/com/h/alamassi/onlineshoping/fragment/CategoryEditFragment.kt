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
import com.h.alamassi.onlineshoping.databinding.FragmentCategoryEditBinding
import com.squareup.picasso.Picasso
import java.util.*

class CategoryEditFragment : Fragment() {
    private lateinit var categoryEditBinding: FragmentCategoryEditBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var progressDialog: ProgressDialog
    private lateinit var categoryCollectionReference: CollectionReference
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    lateinit var catId: String
    private var imagePath: Uri? = null

    companion object {
        const val IMAGE_REQUEST_CODE = 101
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        showDialog()
        firebaseFirestore = FirebaseFirestore.getInstance()
        categoryEditBinding =
            FragmentCategoryEditBinding.inflate(inflater, container, false)
        return categoryEditBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Edit Category"

        readData()
        categoryEditBinding.fabChooseImage.setOnClickListener {

            chooseImage()
        }

        categoryEditBinding.btnSave.setOnClickListener {
            showDialog()
            update()
        }
    }


    private fun readData() {
        catId = arguments?.getString("catId") ?: ""
        categoryCollectionReference = firebaseFirestore
            .collection("categories")

        categoryCollectionReference
            .whereEqualTo("catId", catId)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && !it.result.isEmpty) {
                    for (q in it.result) {
                        categoryEditBinding.etCategoryName.setText(q.data["name"].toString())
                        Picasso.get().load(q.data["image"].toString())
                            .into(categoryEditBinding.ivCategoryImage)
                    }
                }
            }
        hideDialog()
    }

    private fun update() {
        uploadData()

    }

    private fun uploadData() {
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
        val newName = categoryEditBinding.etCategoryName.text.toString()
        val data = HashMap<String, Any>()
        data["image"] = imageURI
        data["name"] = newName
        categoryCollectionReference
            .document(catId)
            .update(data)
            .addOnSuccessListener {
                Toast.makeText(activity, "Updated Successfully", Toast.LENGTH_SHORT).show()
                hideDialog()
                requireActivity().onBackPressed()
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Updated Failed", Toast.LENGTH_SHORT).show()
                hideDialog()
                requireActivity().onBackPressed()

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
            categoryEditBinding.ivCategoryImage.setImageURI(imagePath)
        }
    }

    private fun showDialog() {
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Uploading ....")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun hideDialog() {
        if (progressDialog.isShowing)
            progressDialog.dismiss()
    }
}