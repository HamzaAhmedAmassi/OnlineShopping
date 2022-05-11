package com.h.alamassi.onlineshoping.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.LoginActivity
import com.h.alamassi.onlineshoping.R
import com.h.alamassi.onlineshoping.databinding.FragmentProfileShowBinding

class ProfileShowFragment : Fragment() {

    private lateinit var profileShowBinding: FragmentProfileShowBinding
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
        profileShowBinding = FragmentProfileShowBinding.inflate(inflater)
        return profileShowBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        firebaseFirestore.collection("user")
            .whereEqualTo("uid", firebaseAuth.currentUser!!.uid)
            .limit(1)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && !it.result.isEmpty) {
                    for (q in it.result) {
                        profileShowBinding.edEmail.setText(q.data["email"].toString())
                        profileShowBinding.edPassword.setText(q.data["password"].toString())
                        profileShowBinding.edUsername.setText(q.data["username"].toString())
                        hideDialog()
//                        profileShowBinding.ivUser.setImageBitmap(q.data["image"] as Bitmap?)
                    }
                } else {
                    hideDialog()
                    Toast.makeText(context, "Something Error", Toast.LENGTH_LONG).show()
                }

            }
        profileShowBinding.ibDelete.setOnClickListener {
            delete()

        }
        profileShowBinding.ibEdit.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileEditFragment()).commit()
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
                        if (it.isSuccessful && !it.result.isEmpty) {
                            userCollectionReference.document(uid)
                                .delete()
                            user.delete ()
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