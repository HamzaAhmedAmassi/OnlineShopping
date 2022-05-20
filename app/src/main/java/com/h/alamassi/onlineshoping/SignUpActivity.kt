package com.h.alamassi.onlineshoping

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.h.alamassi.onlineshoping.databinding.ActivitySignUpBinding
import java.util.*


class SignUpActivity : AppCompatActivity() {

    private lateinit var signUpBinding: ActivitySignUpBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private var imagePath: Uri? = null

    companion object {
        const val IMAGE_REQUEST_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar!!.hide()
        
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(signUpBinding.root)
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        signUpBinding.btnSignUp.setOnClickListener {
            signUp()
        }
        signUpBinding.fabChooseImage.setOnClickListener {
            chooseImage()
        }
        signUpBinding.tvLogin.setOnClickListener {
            onBackPressed()
        }
    }


    private fun signUp() {
        val email = signUpBinding.edEmail.text.toString()
        val password = signUpBinding.edPassword.text.toString()
        val username = signUpBinding.edUsername.text.toString()
        when {
            email.isEmpty() -> {
                Toast.makeText(this, "email required, can’t be empty", Toast.LENGTH_LONG).show()

            }
            username.isEmpty() -> {
                Toast.makeText(this, "username required, can’t be empty", Toast.LENGTH_LONG).show()

            }
            password.isEmpty() -> {
                Toast.makeText(this, "password required 6 digits or more", Toast.LENGTH_LONG).show()

            }
            else -> {
                showDialog()
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        showDialog()
                        if (it.isSuccessful) {
                            uploadImage()
                        } else {
                            Toast.makeText(this, "Created Failed", Toast.LENGTH_LONG).show()
                            hideDialog()
                        }

                    }
            }
        }

    }

    private fun uploadImage() {
        if (imagePath != null) {
            val imageName = "${UUID.randomUUID()}.jpeg"
            val ref = storageReference.child("images/$imageName")

            ref.putFile(imagePath!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        storeUserInDB(it.toString())
                    }
                }
                .addOnFailureListener {

                }
        }
    }

    private fun storeUserInDB(imageURI: String) {
        val user = firebaseAuth.currentUser
        if (user != null) {
            showDialog()
            val email = signUpBinding.edEmail.text.toString()
            val password = signUpBinding.edPassword.text.toString()
            val username = signUpBinding.edUsername.text.toString()

            val uid = user.uid
            val data = HashMap<String, Any>()
            data["uid"] = uid
            data["email"] = email
            data["username"] = username
            data["password"] = password
            data["image"] = imageURI

            firebaseFirestore.collection("user")
                .document(firebaseAuth.currentUser!!.uid)
                .set(data)

            Toast.makeText(
                this,
                "Created Account Successfully",
                Toast.LENGTH_LONG
            ).show()

            hideDialog()
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        } else {
            hideDialog()
            Toast.makeText(this, "Created Account Failed", Toast.LENGTH_LONG).show()
            onBackPressed()
        }
    }

    private fun chooseImage() {
        val galleryPermission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (galleryPermission != PackageManager.PERMISSION_DENIED) {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            startActivityForResult(intent, Companion.IMAGE_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Companion.IMAGE_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Companion.IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imagePath = data!!.data
            signUpBinding.ivUserPhoto.setImageURI(imagePath)
        }
    }

    private fun showDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading ....")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun hideDialog() {
        if (progressDialog.isShowing)
            progressDialog.dismiss()
    }
}