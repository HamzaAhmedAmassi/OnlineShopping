package com.h.alamassi.onlineshoping

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.databinding.ActivitySignUpBinding
import com.h.alamassi.onlineshoping.fragment.CreateCategoriesFragment

class SignUpActivity : AppCompatActivity() {
    private lateinit var signUpBinding: ActivitySignUpBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog


    companion object {
        private const val TAG = "SignUpActivity"
        const val IMAGE_REQUEST_CODE = 101

    }

    private var imagePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(signUpBinding.root)
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        signUpBinding.btnSignUp.setOnClickListener {
            signUp()
        }
        signUpBinding.fabChooseImage.setOnClickListener {
//            chooseImage()
        }
        signUpBinding.tvLogin.setOnClickListener {
            onBackPressed()
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
                    .addOnCompleteListener { it ->
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Created Account Successfully", Toast.LENGTH_LONG)
                                .show()
                            val user = firebaseAuth.currentUser
                            if (user != null) {
                                val uid = user.uid

                                val data = HashMap<String, String>()
                                data["uid"] = uid
                                data["email"] = email
                                data["username"] = username
                                data["image"] = image
                                firebaseFirestore.collection("user")
                                    .document(firebaseAuth.currentUser!!.uid)
                                    .set(data)
                                    .addOnCompleteListener {
                                        if (!it.isSuccessful) {
                                            Toast.makeText(this, "Insert Failed", Toast.LENGTH_LONG)
                                                .show()
                                        }
                                    }
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
}