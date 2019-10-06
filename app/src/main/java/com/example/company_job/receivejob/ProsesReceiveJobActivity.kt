package com.example.company_job.receivejob

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.company_job.R
import com.example.company_job.activity.MainActivity
import com.example.company_job.activity.PrefsHelper
import com.example.company_job.adapter.DataJobAdapter
import com.example.company_job.data.SettingApi
import com.example.company_job.model.JobModel
import com.google.firebase.database.*

class ProsesReceiveJobActivity : AppCompatActivity() {
    private var actionBar: ActionBar? = null
    lateinit var helperPrefs : PrefsHelper
    lateinit var dbref: DatabaseReference
    lateinit var rvProses: RecyclerView
    lateinit var falldown : Animation
    internal lateinit var set: SettingApi
    private var DataJobAdapter: DataJobAdapter? = null
    private var rvDataJob: RecyclerView? = null
    private var list: MutableList<JobModel> = ArrayList<JobModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.data_proses_createjob)
        initToolbar()

        set = SettingApi(this)
        helperPrefs = PrefsHelper(this)
        rvDataJob = findViewById(R.id.rvDataJob)
        rvDataJob!!.layoutManager = LinearLayoutManager(this)
        rvDataJob!!.setHasFixedSize(true)
        getDataDetailJob()
    }

    fun getDataDetailJob(){
        dbref = FirebaseDatabase.getInstance().getReference("DataJob/")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                list = ArrayList<JobModel>()
                for (dataSnapshot in p0.children) {
                    val addDataAll = dataSnapshot.getValue(JobModel::class.java)
                    if (addDataAll!!.getId_receive() == helperPrefs.getUID()!!.toString() &&
                        addDataAll!!.getIsdone() == 0L) {
//                        addDataAll!!.setKey(dataSnapshot.key!!)
                        list.add(addDataAll)

                    }
//                    Log.e("TAG_ERROR", "${list}")
//                    Log.e("view", "${dataSnapshot}")
                }
                DataJobAdapter = DataJobAdapter(this@ProsesReceiveJobActivity, list)
                rvDataJob!!.adapter = DataJobAdapter
                rvProses = findViewById(R.id.rvDataJob)
                falldown = AnimationUtils.loadAnimation(this@ProsesReceiveJobActivity, R.anim.item_animation_fall_down)
                rvProses.startAnimation(falldown)

            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e("TAG_ERROR", p0.message)
            }

        })

    }

    fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        actionBar = supportActionBar
        actionBar!!.setTitle("Data Proses Terima pekerjaan")
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}