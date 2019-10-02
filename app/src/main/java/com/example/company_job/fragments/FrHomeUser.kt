package com.example.company_job.fragments

import android.os.Bundle
import android.support.design.internal.BottomNavigationMenu
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.company_job.R
import com.example.company_job.activity.MainActivity
import com.example.company_job.activity.PrefsHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fr_homeuser_activity.*
import kotlinx.android.synthetic.main.fr_homeuser_activity.view.*

class FrHomeUser(): Fragment() {

    lateinit var fAuth: FirebaseAuth
    lateinit var helperPrefs: PrefsHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fr_homeuser_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fAuth = FirebaseAuth.getInstance()
        helperPrefs = PrefsHelper(activity!!)
        Glide.with(view.context)
            .load(R.drawable.avatar)
            .into(view.avatar)
        id_tunggu.visibility = View.VISIBLE
        getData()

        val mainActivity = activity!! as MainActivity

        ll_CreateJob.setOnClickListener(({
            val frcreatejob = FrCreateJob()
            val fragmentManager = fragmentManager
            fragmentManager!!.beginTransaction().setCustomAnimations(
                R.anim.design_bottom_sheet_slide_in,
                R.anim.design_bottom_sheet_slide_out
            ).replace(R.id.content, frcreatejob).commit()
            mainActivity.setNavigation(R.id.navigation_create)
        }))

        ll_ReceiveJob.setOnClickListener(({
            val frreceivejob = FrReceiveJob()
            val fragmentManager = fragmentManager
            fragmentManager!!.beginTransaction().setCustomAnimations(
                R.anim.design_bottom_sheet_slide_in,
                R.anim.design_bottom_sheet_slide_out
            ).replace(R.id.content, frreceivejob).commit()
            mainActivity.setNavigation(R.id.navigation_receive)
        }))

        ll_Profile.setOnClickListener(({
            val frprofile = FrProfile()
            val fragmentManager = fragmentManager
            fragmentManager!!.beginTransaction().setCustomAnimations(
                R.anim.design_bottom_sheet_slide_in,
                R.anim.design_bottom_sheet_slide_out
            ).replace(R.id.content, frprofile).commit()
            mainActivity.setNavigation(R.id.navigation_profile)
        }))
    }

    fun getData(){
        val dbRefUser = FirebaseDatabase.getInstance().getReference("users/${helperPrefs.getUID()}")
        dbRefUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.e("uid", helperPrefs.getUID())
                if (p0.child("/foto").value.toString() != "null") {
                    Glide.with(view!!.context)
                        .load(p0.child("/foto").value.toString())
                        .into(view!!.avatar)
                }

                view!!.id_nama.text = p0.child("/nama").value.toString()
                view!!.id_department.text = p0.child("/department").value.toString()
                view!!.id_kontak.text = p0.child("/phone").value.toString()
                view!!.id_email.text = p0.child("/email").value.toString()

                id_tunggu.visibility = View.GONE
            }


        })
    }
}