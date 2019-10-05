package com.example.company_job.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.company_job.R
import com.example.company_job.fragments.FrCreateJob
import com.example.company_job.fragments.FrHomeUser
import com.example.company_job.fragments.FrProfile
import com.example.company_job.fragments.FrReceiveJob
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var groupAdapter = GroupAdapter<ViewHolder>()
    lateinit var mAuth: FirebaseAuth
    lateinit var helperPrefs: PrefsHelper

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val fragment = FrHomeUser()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
                finish()
            }
            R.id.navigation_create -> {
                val fragment = FrCreateJob()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
                finish()
            }
            R.id.navigation_receive -> {
                val fragment = FrReceiveJob()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
                finish()
            }
            R.id.navigation_profile -> {
                val fragment = FrProfile()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
                finish()
            }
        }
        false
    }

    fun getData(){
        val dbRefUser = FirebaseDatabase.getInstance().getReference("users/${helperPrefs.getUID()}")
        dbRefUser.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.e("uid", helperPrefs.getUID())
                if (p0.child("/foto").value.toString() != "null") {
                    Glide.with(this@MainActivity)
                        .load(p0.child("/foto").value.toString())
                        .into(avatar)
                }

                et_nama.text = p0.child("/nama").value.toString()
                et_departemen.text = p0.child("/department").value.toString()

            }


        })
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.design_bottom_sheet_slide_in,
                R.anim.design_bottom_sheet_slide_out
            )
            .replace(R.id.content, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    fun setNavigation(id : Int){
        navigation.selectedItemId = id
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        helperPrefs = PrefsHelper(this@MainActivity)
        mAuth = FirebaseAuth.getInstance()
        Glide.with(this)
            .load(R.drawable.avatar)
            .into(avatar)
        getData();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val fragment = FrHomeUser()
        addFragment(fragment)

        ll_akun.setOnClickListener(({
            val frcreatejob = FrCreateJob()
            val fragmentManager = supportFragmentManager
            fragmentManager!!.beginTransaction().setCustomAnimations(
                R.anim.design_bottom_sheet_slide_in,
                R.anim.design_bottom_sheet_slide_out
            ).replace(R.id.content, frcreatejob).commit()
            setNavigation(R.id.navigation_profile)
        }))

        //setting toolbar
//        setSupportActionBar(findViewById(R.id.toolbar))
        //home navigation
//        setSupportActionBar(toolbar)
//        toolbar.title = "Company Job"
//        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.grey_text))
//        toolbar.setLogo(R.mipmap.ic_logo_cj)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_actionbar,menu)
        return super.onCreateOptionsMenu(menu)
    }

//    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
//        R.id.action_chat -> {
//            startActivity(Intent(this, ChatActivity::class.java))
//            true
//        }
//
//        else -> {
//            super.onOptionsItemSelected(item)
//        }
//    }

    override fun onResume() {
        super.onResume()
        val user = mAuth.currentUser
        if (user == null) {
            finish()
        }
    }
}

