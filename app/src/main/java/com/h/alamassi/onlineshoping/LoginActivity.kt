package com.h.alamassi.onlineshoping

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
        this.supportActionBar?.title = "Login"

        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        loginBinding.btnLogin.setOnClickListener {
            login()
        }

        loginBinding.tvSignUp.setOnClickListener {
            signUp()
        }


    }

    private fun login() {
        val email = loginBinding.edEmail.text.toString()
        val password = loginBinding.edPassword.text.toString()
        when {
            email.isEmpty() -> {
                Toast.makeText(this, "Email required", Toast.LENGTH_LONG).show()
            }
            password.isEmpty() -> {
                Toast.makeText(this, "password required", Toast.LENGTH_LONG).show()

            }
            else -> {
                showDialog()
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Login Successfully", Toast.LENGTH_LONG).show()
                        val i = Intent(this, MainActivity::class.java)
                        startActivity(i)
                        finish()
                        hideDialog()

                    } else {
                        Toast.makeText(
                            this,
                            "Login Failed, something error,\n check email and password",
                            Toast.LENGTH_LONG
                        ).show()
                        hideDialog()


                    }
                }

            }
        }
    }

    private fun signUp() {
        val toSignUpActivity = Intent(this, SignUpActivity::class.java)
        startActivity(toSignUpActivity)
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