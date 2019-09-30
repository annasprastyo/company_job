package com.example.company_job.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.company_job.R
import kotlinx.android.synthetic.main.fr_homeuser_activity.*

class FrHomeUser: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fr_homeuser_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ll_CreateJob.setOnClickListener(({
            val frcreatejob = FrCreateJob()
            val fragmentManager = fragmentManager
            fragmentManager!!.beginTransaction().setCustomAnimations(
                R.anim.design_bottom_sheet_slide_in,
                R.anim.design_bottom_sheet_slide_out
            ).replace(R.id.content, frcreatejob).commit()
        }))

        ll_ReceiveJob.setOnClickListener(({
            val frreceivejob = FrReceiveJob()
            val fragmentManager = fragmentManager
            fragmentManager!!.beginTransaction().setCustomAnimations(
                R.anim.design_bottom_sheet_slide_in,
                R.anim.design_bottom_sheet_slide_out
            ).replace(R.id.content, frreceivejob).commit()
        }))

        ll_Profile.setOnClickListener(({
            val frprofile = FrProfile()
            val fragmentManager = fragmentManager
            fragmentManager!!.beginTransaction().setCustomAnimations(
                R.anim.design_bottom_sheet_slide_in,
                R.anim.design_bottom_sheet_slide_out
            ).replace(R.id.content, frprofile).commit()
        }))
    }
}