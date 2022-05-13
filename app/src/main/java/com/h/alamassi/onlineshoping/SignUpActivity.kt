package com.h.alamassi.onlineshoping

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.h.alamassi.onlineshoping.databinding.ActivitySignUpBinding
import java.util.*


class SignUpActivity : AppCompatActivity() {
    private lateinit var signUpBinding: ActivitySignUpBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private val storage = FirebaseStorage.getInstance();
    private val storageReference = storage.reference;


    companion object {
        const val IMAGE_REQUEST_CODE = 101
        var imagePath: Uri? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.title = "SignUp"

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


    private fun chooseImage() {
        val galleryPermission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (galleryPermission != PackageManager.PERMISSION_DENIED) {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                IMAGE_REQUEST_CODE
            )
        }
    }

    private fun signUp() {
        val email = signUpBinding.edEmail.text.toString()
        val password = signUpBinding.edPassword.text.toString()
        val username = signUpBinding.edUsername.text.toString()
        val image = imagePath
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
                        if (it.isSuccessful) {
                            showDialog()
                            val user = firebaseAuth.currentUser
                            if (user != null) {
                                val uid = user.uid
                                val data = HashMap<String, Any>()
                                data["uid"] = uid
                                data["email"] = email
                                data["username"] = username
                                data["password"] = password
                                data["image"] = image.toString()
                                firebaseFirestore.collection("user")
                                    .document(firebaseAuth.currentUser!!.uid)
                                    .set(data)
                                uploadImage()
                                Toast.makeText(
                                    this,
                                    "Created Account Successfully",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                hideDialog()
                                val i = Intent(this, MainActivity::class.java)
                                startActivity(i)
                                finish()
                                hideDialog()

                            } else {
                                Toast.makeText(this, "Created Failed", Toast.LENGTH_LONG).show()
                                hideDialog()

                            }
                        } else {
                            Toast.makeText(this, "Created Failed", Toast.LENGTH_LONG).show()
                            hideDialog()
                        }

                    }
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imagePath = data!!.data
            signUpBinding.ivUserPhoto.setImageURI(imagePath)
        }
    }

    private fun uploadImage() {

//        val bitmap = (signUpBinding.ivUserPhoto as BitmapDrawable).bitmap
//        val baos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
//        val data = baos.toByteArray()
//        val uploadTask = imageRef.putBytes(data)
//        uploadTask.addOnCompleteListener {
//            if (it.isSuccessful) {
//                Log.d("TAG", "onActivityResult: image Uploaded Successfully")
//            } else {
//                Log.d("TAG", "onActivityResult: image Uploaded Successfully")
//            }
//        }

        if (imagePath != null) {
            val ref: StorageReference =
                storageReference.child("images/")

            ref.putFile(imagePath!!)
                .addOnSuccessListener {
                    Log.d("this", "Uploaded Successfully")
                }
                .addOnFailureListener {
                    Log.d("this", "Uploaded Failed")

                }
        }
    }
}


