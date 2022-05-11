package com.h.alamassi.onlineshoping

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.databinding.ActivityMainBinding
import com.h.alamassi.onlineshoping.fragment.CategoryFragment
import com.h.alamassi.onlineshoping.fragment.ProfileShowFragment


class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        firebaseFirestore = FirebaseFirestore.getInstance()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CategoryFragment())
            .commit()

        mainBinding.bottomNavigation.setOnItemSelectedListener {
            val fragment = when (it.itemId) {
                R.id.nav_home -> CategoryFragment()
                R.id.nav_profile -> ProfileShowFragment()
//                R.id.nav_favorite -> FavouritesFragment()
                else -> CategoryFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            false
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logOutItem -> onClickLogout()
            //R.id.searchItem -> onClickLogout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onClickLogout() {
         FirebaseAuth.getInstance().signOut()
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
        finish()
    }
}
