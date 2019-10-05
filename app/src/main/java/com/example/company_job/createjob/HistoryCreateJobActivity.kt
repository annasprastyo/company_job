package com.example.company_job.createjob

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
import com.example.company_job.adapter.DataJobAdapter
import com.example.company_job.data.SettingApi
import com.example.company_job.model.JobModel
import com.example.company_job.utilities.Const
import com.google.firebase.database.*

class HistoryCreateJobActivity : AppCompatActivity() {
    private var actionBar: ActionBar? = null
    lateinit var dbref: DatabaseReference
    internal lateinit var set: SettingApi
    lateinit var rvJob : RecyclerView
    lateinit var falldown : Animation
    private var DataJobAdapter: DataJobAdapter? = null
    private var rvDataJob: RecyclerView? = null
    private var list: MutableList<JobModel> = ArrayList<JobModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.data_proses_createjob)
        initToolbar()

        set = SettingApi(this)
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
                    if (addDataAll!!.getId_user() == set.readSetting(Const.PREF_MY_ID)!!.toString() &&
                        addDataAll!!.getIsdone()!!.toLong().equals(1L)) {
                        addDataAll!!.setKey(dataSnapshot.key!!)
                        list.add(addDataAll)

                    }
//                    Log.e("TAG_ERROR", "${list}")

//                    Log.e("view", "${dataSnapshot}")
                }
                DataJobAdapter = DataJobAdapter(this@HistoryCreateJobActivity, list)
                rvDataJob!!.adapter = DataJobAdapter
                rvJob = findViewById(R.id.rvDataJob)
                falldown = AnimationUtils.loadAnimation(this@HistoryCreateJobActivity, R.anim.item_animation_fall_down)
                rvJob.startAnimation(falldown)
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
        actionBar!!.setTitle("Data History Buat Pekerjaan")
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
//                startActivity(Intent(this, MainActivity::class.java))
                onBackPressed()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}