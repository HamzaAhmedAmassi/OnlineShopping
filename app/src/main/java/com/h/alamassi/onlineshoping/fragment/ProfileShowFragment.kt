package com.h.alamassi.onlineshoping.fragment

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.databinding.FragmentProfileShowBinding

class ProfileShowFragment : Fragment() {

    private lateinit var profileShowFragmentBinding: FragmentProfileShowBinding
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
        profileShowFragmentBinding = FragmentProfileShowBinding.inflate(inflater)
        return profileShowFragmentBinding.root
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
                if (it.isSuccessful) {
                    for (q in it.result) {
                        profileShowFragmentBinding.txtEmail.text = q.data["email"].toString()
                        profileShowFragmentBinding.txtPassword.text = q.data["password"].toString()
                        profileShowFragmentBinding.txtUsername.text = q.data["username"].toString()
//                        profileShowFragmentBinding.ivUser.setImageBitmap(q.data["image"] as Bitmap?)
                        hideDialog()
                    }
                }
            }
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