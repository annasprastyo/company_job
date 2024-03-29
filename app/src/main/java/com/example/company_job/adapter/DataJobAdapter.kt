package com.example.company_job.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.company_job.R
import com.example.company_job.activity.PrefsHelper
import com.example.company_job.createjob.ProsesCreateJobActivity
import com.example.company_job.data.SettingApi
import com.example.company_job.model.JobModel
import com.example.company_job.utilities.Const
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.dialog_ambil.view.*

class DataJobAdapter: RecyclerView.Adapter<DataJobAdapter.DataJobViewHolder> {

    lateinit var dbref: DatabaseReference
    lateinit var helperPrefs: PrefsHelper
    lateinit var mContext: Context
    lateinit var itemMyorder: List<JobModel>
    internal lateinit var set: SettingApi
//    lateinit var listener : FirebaseDataListener

    constructor()
    constructor(mContext: Context, list: List<JobModel>) {
        this.mContext = mContext
        this.itemMyorder = list
//        listener = mContext as Item1
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DataJobViewHolder {
        val view: View = LayoutInflater.from(p0.context).inflate(
            R.layout.row_data_job, p0, false
        )
        val uploadViewHolder = DataJobViewHolder(view)
        return uploadViewHolder
    }

    override fun getItemCount(): Int {
        return itemMyorder.size
    }

    @SuppressLint("RestrictedApi")
    override fun onBindViewHolder(p0: DataJobViewHolder, p1: Int) {
        val jobModel: JobModel = itemMyorder.get(p1)
        val id_job = jobModel.getId_job()!!.toLong()
        Glide.with(mContext)
            .load(jobModel.getImage())
            .into(p0.image)

        helperPrefs = PrefsHelper(mContext)
        set = SettingApi(mContext)
        p0.id_judul.text = jobModel.getJudul()
        p0.id_department.text = jobModel.getDepartment()
        p0.dodate.text = jobModel.getDodate()


        if (jobModel.getIsdone().toString().equals("0")){
            p0.ll_status.setBackgroundColor(ContextCompat.getColor(mContext, R.color.biruDesain))
            p0.status.text = "Proses"
        }else {
            p0.ll_status.setBackgroundColor(ContextCompat.getColor(mContext, R.color.success))
            p0.status.text = "Selesai"
        }

        if (helperPrefs.getPilih().toString().equals("create")){
            p0.ll_status.visibility = View.VISIBLE

            p0.ll_datajob.setOnClickListener {
                var intent = Intent(mContext, ProsesCreateJobActivity::class.java)
                intent.putExtra("Id_job", jobModel.getId_job()!!.toLong())
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                mContext.applicationContext.startActivity(intent)
            }
        }else{
            p0.ll_status.visibility = View.GONE
        }

        if (helperPrefs.getPilih().toString().equals("receive")){
            if (jobModel.getId_receive().toString().equals("null")){
                p0.ll_status2.visibility = View.VISIBLE
                p0.ll_datajob.setOnClickListener {

                    val aDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_ambil, null)
                    //AlertDialogBuilder

                    val mBuilder = AlertDialog.Builder(mContext)
                        .setView(aDialogView)
                        .setTitle("Ambil Pekerjaan?")
                    //show dialog
                    val  mAlertDialog = mBuilder.show()
                    val dbRefUser = FirebaseDatabase.getInstance().getReference("DataJob/${jobModel.getId_job()!!.toLong()}")
                    dbRefUser.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            Glide.with(mContext)
                                .load(p0.child("/image").value.toString())
                                .into(aDialogView.id_image)
                            aDialogView.id_judul.text = p0.child("/judul").value.toString()
                            aDialogView.id_nama.text = p0.child("/nama").value.toString()
                            aDialogView.id_department.text = p0.child("/department").value.toString()
                            aDialogView.id_tanggal.text = p0.child("/dodate").value.toString()
                            aDialogView.id_deskripsi.text = p0.child("/deskripsi").value.toString()

                            aDialogView.id_ambil.setOnClickListener {
                                //            Toast.makeText(applicationContext,"Ok, we change the app background.",Toast.LENGTH_SHORT).show()
                                dbref = FirebaseDatabase.getInstance().getReference("DataJob/${jobModel.getId_job()!!.toLong()}")
                                dbref.child("id_receive").setValue(set.readSetting(Const.PREF_MY_ID))

                                var intent = Intent(mContext!!, ProsesCreateJobActivity::class.java)
                                intent.putExtra("Id_job", jobModel.getId_job()!!.toLong())
                                e("Id_job", "${jobModel.getId_job()!!.toLong()}")
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                mContext.applicationContext.startActivity(intent)
                                Toast.makeText(mContext, "Job Di Ambil!!", Toast.LENGTH_SHORT).show()
                                mAlertDialog.dismiss()
                            }

                            aDialogView.id_batal.setOnClickListener {
                                //                        Toast.makeText(mContext,"Kamu membatalkan penyelesaian.",Toast.LENGTH_SHORT).show()
                                mAlertDialog.dismiss()
                            }
//                            e("TAHH", "${p0}")
                        }

                    })







                }
            }else{
                p0.ll_status.visibility = View.VISIBLE

                p0.ll_datajob.setOnClickListener {
                    var intent = Intent(mContext, ProsesCreateJobActivity::class.java)
                    intent.putExtra("Id_job", jobModel.getId_job()!!.toLong())
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    mContext.applicationContext.startActivity(intent)
                }
            }

        }else{
            p0.ll_status2.visibility = View.GONE
        }

        if (helperPrefs.getPilih().toString().equals("historyc") ||
            helperPrefs.getPilih().toString().equals("historyr")){
            p0.ll_status.visibility = View.VISIBLE
            p0.ll_status2.visibility = View.GONE
            p0.ll_datajob.setOnClickListener {
                var intent = Intent(mContext, ProsesCreateJobActivity::class.java)
                intent.putExtra("Id_job", jobModel.getId_job()!!.toLong())
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                mContext.applicationContext.startActivity(intent)
            }
        }






//            view.context.startActivity(Intent(view.context, OutletActivity::class.java))
    }

    inner class DataJobViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview){
        var ll_datajob : LinearLayout
        var ll_status : LinearLayout
        var ll_status2 : LinearLayout
        var image : ImageView
        var id_judul : TextView
        var id_department : TextView
        var dodate : TextView
        var status : TextView
        var status2 : TextView
        init {
            ll_datajob = itemview.findViewById(R.id.ll_datajob)
            ll_status = itemview.findViewById(R.id.ll_status)
            ll_status2 = itemview.findViewById(R.id.ll_status2)
            image = itemview.findViewById(R.id.image)
            id_judul = itemview.findViewById(R.id.id_judul)
            id_department = itemview.findViewById(R.id.id_department)
            status = itemview.findViewById(R.id.status)
            status2 = itemview.findViewById(R.id.status2)
            dodate = itemview.findViewById(R.id.dodate)

        }
    }
}