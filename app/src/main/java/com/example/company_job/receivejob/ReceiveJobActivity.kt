package com.example.company_job.receivejob

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import com.example.company_job.R
import com.example.company_job.activity.PrefsHelper
import com.example.company_job.adapter.DataJobAdapter
import com.example.company_job.data.SettingApi
import com.example.company_job.model.JobModel
import com.google.firebase.database.*

class ReceiveJobActivity : AppCompatActivity() {

    private var actionBar: ActionBar? = null
    lateinit var helperPrefs : PrefsHelper
    lateinit var dbref: DatabaseReference
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
                    if (addDataAll!!.getDepartment() == helperPrefs.getDepartment()!!.toString() &&
                            addDataAll.getId_receive() == "null") {
                        addDataAll!!.setKey(dataSnapshot.key!!)
                        list.add(addDataAll)
                        DataJobAdapter = DataJobAdapter(this@ReceiveJobActivity, list)
                        rvDataJob!!.adapter = DataJobAdapter
                    }
//                    Log.e("TAG_ERROR", "${list}")

//                    Log.e("view", "${dataSnapshot}")
                }
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
        actionBar!!.setTitle("Receive Job")
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