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
    private var list: MutableList<JobModel> = ArrayList<JobModel>()
    lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fr_receivejob_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        count_receive
//        count_data()
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

//    fun count_data(){
//        dbRef = FirebaseDatabase.getInstance().getReference("DataJob/")
//        dbRef.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//                Toast.makeText(activity!!, "gak kenek", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                list = ArrayList<JobModel>()
//
//                for(dataSnapshot in p0.children){
//                    val addDataAll = dataSnapshot.getValue(JobModel::class.java)
//                    if (addDataAll!!.getDepartment() == helperPrefs.getDepartment()!!.toString() &&
//                        addDataAll.getId_receive() == "null") {
//                        addDataAll!!.setKey(dataSnapshot.key!!)
//                        list.add(addDataAll)
//                    }
//                }
////                Toast.makeText(activity!!, "${list.size}", Toast.LENGTH_SHORT).show()
//                if(list.size.equals(0)){
//                    count_receive.visibility = View.GONE
//                }else{
//                    count_receive.text = "+"+list.size
//                }
////                e("coco","${list.size}")
//            }
//
//        })
//    }
}