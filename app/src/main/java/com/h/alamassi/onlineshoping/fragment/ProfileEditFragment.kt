package com.h.alamassi.onlineshoping.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
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
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.h.alamassi.onlineshoping.LoginActivity
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.FragmentProfileEditBinding
import com.squareup.picasso.Picasso
import java.util.*


class ProfileEditFragment : Fragment() {
    lateinit var profileEditBinding: FragmentProfileEditBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private var currentUserDoc: DocumentReference? = null
    private var currentEmail: String = ""
    private var currentPassword: String = ""
    private var imagePath: Uri? = null

    companion object {
        const val IMAGE_REQUEST_CODE = 101
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        showDialog()
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        profileEditBinding = FragmentProfileEditBinding.inflate(inflater)
        return profileEditBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Edit Profile"

        readeData()
        profileEditBinding.ibDelete.setOnClickListener {
            delete()
        }

        profileEditBinding.btnUpdate.setOnClickListener {
            showDialog()
            update()
        }
        profileEditBinding.fabChooseImage.setOnClickListener {
            chooseImage()
        }


    }

    private fun readeData() {
        firebaseFirestore.collection("user")
            .whereEqualTo("uid", firebaseAuth.currentUser!!.uid)
            .limit(1)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && !it.result.isEmpty) {
                    for (q in it.result) {
                        currentEmail = q.data["email"].toString()
                        currentPassword = q.data["password"].toString()
                        profileEditBinding.edEmail.setText(currentEmail)
                        profileEditBinding.edPassword.setText(currentPassword)
                        profileEditBinding.edUsername.setText(q.data["username"].toString())
                        Picasso.get().load(q.data["image"].toString())
                            .into(profileEditBinding.ivUser)
                    }
                } else {
                    Toast.makeText(context, "Something Error", Toast.LENGTH_LONG).show()
                    requireActivity().onBackPressed()
                }

            }
        hideDialog()
    }

    private fun update() {
        val newEmail = profileEditBinding.edEmail.text.toString()
        val newPassword = profileEditBinding.edPassword.text.toString()

        val user = FirebaseAuth.getInstance().currentUser
        user?.let { authUser ->
            val credential = EmailAuthProvider.getCredential(currentEmail, currentPassword)

            currentUserDoc = firebaseFirestore
                .collection("user")
                .document(firebaseAuth.currentUser!!.uid)

            authUser.reauthenticate(credential)

                .addOnCompleteListener {
                    // update email
                    authUser.updateEmail(newEmail)
                        .addOnSuccessListener {
                            //update password
                            authUser.updatePassword(newPassword).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    uploadData()
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "Updated Failed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    requireActivity().supportFragmentManager.beginTransaction()
                                        .replace(
                                            R.id.fragment_container,
                                            ProfileShowFragment()
                                        ).commit()
                                }
                            }
                        }
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
                        storeUserInDB(it.toString())
                    }
                }
        }
    }

    private fun storeUserInDB(imageURI: String) {
        val newEmail = profileEditBinding.edEmail.text.toString()
        val newPassword = profileEditBinding.edPassword.text.toString()
        val newUsername = profileEditBinding.edUsername.text.toString()
        val user = firebaseAuth.currentUser
        if (user != null) {
            currentUserDoc!!.update(
                mapOf(
                    "email" to newEmail,
                    "password" to newPassword,
                    "username" to newUsername,
                    "image" to imageURI
                )
            )
            Toast.makeText(
                requireContext(),
                "Updated Successfully",
                Toast.LENGTH_LONG
            ).show()
            hideDialog()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    CategoryFragment()
                ).commit()

        } else {
            Toast.makeText(requireContext(), "Updated Failed", Toast.LENGTH_LONG).show()
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
            profileEditBinding.ivUser.setImageURI(imagePath)
        }
    }

    private fun delete() {
        val userCollectionReference =
            firebaseFirestore.collection("user")
        val alertDialog = AlertDialog.Builder(activity)
        alertDialog.setTitle("Delete User")
        alertDialog.setMessage("Are you sure to delete Store ?")
        alertDialog.setIcon(R.drawable.delete)
        alertDialog.setPositiveButton("Yes") { _, _ ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                val uid = user.uid
                firebaseFirestore.collection("user")
                    .get()
                    .addOnCompleteListener {
                        showDialog()
                        if (it.isSuccessful) {
                            userCollectionReference.document(uid).delete()
                            user.delete()
                            firebaseAuth.signOut()
                            startActivity(Intent(activity, LoginActivity::class.java))
                            Toast.makeText(activity, "Deleted Successfully", Toast.LENGTH_SHORT)
                                .show()

                        } else {
                            Toast.makeText(activity, "Deleted Failed", Toast.LENGTH_LONG)
                                .show()
                            requireActivity().onBackPressed()
                        }
                    }
            } else {
                Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProfileShowFragment()).commit()
            }

        }
        alertDialog.setNegativeButton("No") { _, _ ->
        }
        alertDialog.create().show()
        true
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