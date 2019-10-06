package com.example.company_job.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.company_job.R
import com.example.company_job.activity.PrefsHelper
import com.example.company_job.model.JobModel
import com.example.company_job.receivejob.HistoryReceiveJobActivity
import com.example.company_job.receivejob.ProsesReceiveJobActivity
import com.example.company_job.receivejob.ReceiveJobActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fr_receivejob_activity.*

class FrReceiveJob: Fragment() {

    lateinit var helperPrefs : PrefsHelper
//    private var list: MutableList<JobModel> = ArrayList<JobModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fr_receivejob_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        helperPrefs = PrefsHelper(activity!!)
        ll_ReceiveJob.setOnClickListener {
            startActivity(Intent(activity!!, ReceiveJobActivity::class.java))
            helperPrefs.savePilih("receive")
        }
        ll_Proses_Receive.setOnClickListener {
            startActivity(Intent(activity!!, ProsesReceiveJobActivity::class.java))
            helperPrefs.savePilih("receive")
        }
        ll_History_Receive.setOnClickListener {
            startActivity(Intent(activity!!, HistoryReceiveJobActivity::class.java))
            helperPrefs.savePilih("historyr")
        }
    }


}