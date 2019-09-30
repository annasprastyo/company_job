package com.example.company_job.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.company_job.R
import com.example.company_job.activity.PrefsHelper
import com.example.company_job.createjob.CreateJobActivity
import com.example.company_job.createjob.DataCreateJobActivity
import com.example.company_job.createjob.HistoryCreateJobActivity
import kotlinx.android.synthetic.main.fr_createjob_activity.*

class FrCreateJob: Fragment() {
    lateinit var helperPrefs : PrefsHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fr_createjob_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        helperPrefs = PrefsHelper(activity!!)
        id_createdjob.setOnClickListener {
            startActivity(Intent(activity!!, CreateJobActivity::class.java))
            helperPrefs.savePilih("create")
        }
        ll_proses.setOnClickListener {
            startActivity(Intent(activity!!, DataCreateJobActivity::class.java))
            helperPrefs.savePilih("create")
        }
        ll_history_create.setOnClickListener {
            startActivity(Intent(activity!!, HistoryCreateJobActivity::class.java))
            helperPrefs.savePilih("historyc")
        }

    }
}