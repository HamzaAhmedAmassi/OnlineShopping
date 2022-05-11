package com.h.alamassi.onlineshoping.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.LoginActivity
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.FragmentProfileEditBinding


class ProfileEditFragment : Fragment() {

    lateinit var profileEditBinding: FragmentProfileEditBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        showDialog()
        profileEditBinding = FragmentProfileEditBinding.inflate(inflater)
        return profileEditBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        firebaseFirestore.collection("user")
            .whereEqualTo("uid", firebaseAuth.currentUser!!.uid)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && !it.result.isEmpty) {
                    for (q in it.result) {
                        profileEditBinding.edEmail.setText(firebaseAuth.currentUser!!.email)
                        profileEditBinding.edPassword.setText(q.data["password"].toString())
                        profileEditBinding.edUsername.setText(q.data["username"].toString())
                        hideDialog()
//                        profileShowFragmentBinding.ivUser.setImageBitmap(q.data["image"] as Bitmap?)
                    }
                } else {
                    hideDialog()
                    Toast.makeText(context, "Something Error", Toast.LENGTH_LONG).show()
                }

            }
        profileEditBinding.ibDelete.setOnClickListener {
            delete()
        }

        profileEditBinding.btnUpdate.setOnClickListener {
            update()
        }
        profileEditBinding.fabChooseImage.setOnClickListener {
            chooseImage()
        }

    }

    private fun chooseImage() {

    }


    private fun update() {
        val email = profileEditBinding.edEmail.text.toString()
        val password = profileEditBinding.edPassword.text.toString()
        val username = profileEditBinding.edUsername.text.toString()


        val map = HashMap<String, Any>()
        map["username"] = username
        map["email"] = email
        map["password"] = password
        val auth = FirebaseAuth.getInstance()

        val user = FirebaseAuth.getInstance().currentUser
        // Get auth credentials from the user for re-authentication
        // Get auth credentials from the user for re-authentication
        val credential = EmailAuthProvider
            .getCredential("email@mail.com", "123123") // Current Login Credentials \\

        // Prompt the user to re-provide their sign-in credentials
        // Prompt the user to re-provide their sign-in credentials
        user!!.reauthenticate(credential)
            .addOnCompleteListener {
                Log.d("TAG", "User re-authenticated.")
                //Now change your email address \\
                //----------------Code for Changing Email Address----------\\
                val user = FirebaseAuth.getInstance().currentUser
                user!!.updateEmail("email2@gmail.com")
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("TAG", "User email address updated.")
                        }
                    }
                user.updatePassword("123456")
                //----------------------------------------------------------\\
            }
//        auth.signInWithEmailAndPassword("email@mail.com", "123123")
//            .addOnCompleteListener(requireActivity()) { it ->
//                if (it.isSuccessful) {
//                    auth.currentUser!!.updateEmail(email)
//                        .addOnCompleteListener {
//                            if (it.isSuccessful) {
//                                Log.d("TAG", "update: email Successfully")
//                            } else {
//                                Log.e("TAG", "update: email Failed")
//                            }
//                        }
//                    auth.currentUser!!.updatePassword(password)
//                        .addOnCompleteListener {
//                            if (it.isSuccessful) {
//                                Log.d("TAG", "update: password Successfully")
//                            } else {
//                                Log.e("TAG", "update: password Failed")
//                            }
//                        }
//                } else {
//                    Log.e("TAG", "update: Login Failed")
//                }
//            }
        firebaseFirestore.collection("user").document(firebaseAuth.currentUser!!.uid)
            .update(map)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(activity, "Updated Successfully", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ProfileShowFragment()).commit()
                } else {
                    Toast.makeText(activity, "Updated Failed", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ProfileShowFragment()).commit()

                }
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
                            hideDialog()
                            startActivity(Intent(activity, LoginActivity::class.java))
                            Toast.makeText(activity, "Deleted Successfully", Toast.LENGTH_SHORT)
                                .show()

                        } else {
                            hideDialog()
                            Toast.makeText(activity, "Deleted Failed", Toast.LENGTH_LONG)
                                .show()
                        }
                    }


            } else {
                Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CategoryFragment()).commit()
            }

        }
        alertDialog.setNegativeButton("No") { _, _ ->
        }
        alertDialog.create().show()
        true

    }


    private fun showDialog() {
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading ....")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun hideDialog() {
        if (progressDialog.isShowing)
            progressDialog.dismiss()
    }
}