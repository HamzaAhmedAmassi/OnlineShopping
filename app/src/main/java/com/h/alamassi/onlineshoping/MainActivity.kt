package com.h.alamassi.onlineshoping

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.h.alamassi.onlineshoping.databinding.ActivityMainBinding
import com.h.alamassi.onlineshoping.datasourse.SharedPreferenceHelper
import com.h.alamassi.onlineshoping.fragment.CategoriesFragment
import com.h.alamassi.onlineshoping.fragment.FavouritesFragment
import com.h.alamassi.onlineshoping.fragment.ProfileShowFragment
import com.h.alamassi.onlineshoping.model.Category
import java.util.*


class MainActivity : AppCompatActivity() {
    val data = ArrayList<Category>()
    val displaylist =  ArrayList<Category>()

    companion object {
        private const val TAG = "MainActivity"
        const val NAME = "Hamza"
    }
    lateinit var mainBinding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CategoriesFragment())
            .commit()

        mainBinding.bottomNavigation.setOnItemSelectedListener {
            val fragment = when (it.itemId) {
//                R.id.nav_home -> CategoriesFragment()
//                R.id.nav_profile -> ProfileShowFragment()
//                R.id.nav_favorite -> FavouritesFragment()
                else -> CategoriesFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            false
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.option_menu, menu)
        //_______________________________________


        //_______________________________________
        return super.onCreateOptionsMenu(menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.logOutItem -> onClickLogout()
            //R.id.searchItem -> onClickLogout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onClickLogout() {

        SharedPreferenceHelper.getInstance(this)?.setInt("currentUserId", -1)
        SharedPreferenceHelper.getInstance(this)?.setBoolean("isLogin", false)
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
        finish()
    }
}