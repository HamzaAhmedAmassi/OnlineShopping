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
import com.h.alamassi.onlineshoping.databinding.FragmentCreateProductBinding
import com.h.alamassi.onlineshoping.fragment.ProductFragment.Companion.catId
import java.util.*


class CreateProductFragment : Fragment() {

    private lateinit var createProductBinding: FragmentCreateProductBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var progressDialog: ProgressDialog
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private var productCollectionReference: CollectionReference? = null
    private var imagePath: Uri? = null

    companion object {
        const val IMAGE_REQUEST_CODE = 103

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        firebaseFirestore = FirebaseFirestore.getInstance()
        createProductBinding = FragmentCreateProductBinding.inflate(inflater, container, false)
        return createProductBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Crate Product"

        createProductBinding.btnSave.setOnClickListener {
            showDialog()
            createProduct()
        }
        createProductBinding.fabChooseImage.setOnClickListener {
            chooseImage()
        }
    }


    private fun createProduct() {
        val name = createProductBinding.edName.text.toString()
        val description = createProductBinding.edDescription.text.toString()
        val price = createProductBinding.edPrice.text.toString()
        val quantity = createProductBinding.edQuantity.text.toString()

        val catId = arguments?.getString("catId") ?: ""

        productCollectionReference = firebaseFirestore
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
            quantity.isEmpty() -> {
                Toast.makeText(requireContext(), "Quantity required", Toast.LENGTH_SHORT).show()
            }
            else -> {
                showDialog()
                uploadData()
            }
        }
        hideDialog()
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
        val productId = productCollectionReference!!.document().id
        val data = HashMap<String, String>()
        val name = createProductBinding.edName.text.toString()
        val description = createProductBinding.edDescription.text.toString()
        val price = createProductBinding.edPrice.text.toString()
        val quantity = createProductBinding.edQuantity.text.toString()

        data["productId"] = productId
        data["name"] = name
        data["description"] = description
        data["image"] = imageURI
        data["price"] = price
        data["quantity"] = quantity
        productCollectionReference!!.document(productId).set(data)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Created Successfully", Toast.LENGTH_LONG)
                        .show()
                    val bundle = Bundle()
                    bundle.putString("catId", catId)
                    hideDialog()
                    requireActivity().supportFragmentManager.beginTransaction().addToBackStack("")
                        .replace(
                            R.id.fragment_container,
                            ProductFragment::class.java,
                            bundle
                        ).commit()
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
            createProductBinding.ivProductImage.setImageURI(imagePath)
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
