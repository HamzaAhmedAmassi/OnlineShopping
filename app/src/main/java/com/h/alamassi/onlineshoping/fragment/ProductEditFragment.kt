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
import com.h.alamassi.onlineshoping.databinding.FragmentProductEditBinding
import com.squareup.picasso.Picasso
import java.util.*

class ProductEditFragment : Fragment() {
    private lateinit var productEditBinding: FragmentProductEditBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var progressDialog: ProgressDialog
    private lateinit var productCollectionReference: CollectionReference
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    lateinit var productId: String
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
        productEditBinding =
            FragmentProductEditBinding.inflate(inflater, container, false)
        return productEditBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Edit Product"

        readData()

        productEditBinding.fabChooseImage.setOnClickListener {
            chooseImage()
        }

        productEditBinding.btnUpdate.setOnClickListener {
            showDialog()
            update()
        }


    }

    private fun readData() {
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
                        Picasso.get().load(q.data["image"].toString())
                            .into(productEditBinding.ivProductImage)
                    }
                }
            }
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
                        storeProductInDB(it.toString())
                    }
                }
        }
    }

    private fun storeProductInDB(imageURI: String) {
        val newName = productEditBinding.edName.text.toString()
        val newDescription = productEditBinding.edDescription.text.toString()
        val newPrice = productEditBinding.edPrice.text.toString()
        val newQuantity = productEditBinding.edQuantity.text.toString()

        val data = HashMap<String, Any>()
        data["name"] = newName
        data["description"] = newDescription
        data["price"] = newPrice
        data["quantity"] = newQuantity
        data["image"] = imageURI
        productCollectionReference
            .document(productId)
            .update(data)
            .addOnSuccessListener {
                Toast.makeText(activity, "Updated Successfully", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed()
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Updated Failed", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed()

            }
        hideDialog()
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
            productEditBinding.ivProductImage.setImageURI(imagePath)
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




