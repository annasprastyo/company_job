package com.example.company_job.createjob

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.company_job.R
import com.example.company_job.activity.ChatDetailsActivity
import com.example.company_job.activity.MainActivity
import com.example.company_job.activity.PrefsHelper
import com.example.company_job.adapter.ProsesCreateJobAdapter
import com.example.company_job.data.SettingApi
import com.example.company_job.model.DetailJobModel
import com.example.company_job.model.Friend
import com.example.company_job.receivejob.ProsesReceiveJobActivity
import com.example.company_job.utilities.Const
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.add_detail.view.*
import kotlinx.android.synthetic.main.job_info_proses.*
import kotlinx.android.synthetic.main.view_image.view.*

class ProsesCreateJobActivity : AppCompatActivity(){

    var a: Long? = null
    var Id_job : Long? = null
    internal lateinit var set: SettingApi
    private var rvDetailJob: RecyclerView? = null
    lateinit var helperPrefs: PrefsHelper
    lateinit var rvDetail : RecyclerView
    lateinit var falldown : Animation
    private var ProsesCreateJobAdapter: ProsesCreateJobAdapter? = null
    private var actionBar: ActionBar? = null
    private var list: MutableList<DetailJobModel> = ArrayList<DetailJobModel>()
    lateinit var dbref: DatabaseReference

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.job_info_proses)

        initToolbar()
        set = SettingApi(this)
        helperPrefs = PrefsHelper(this)
        Id_job = intent.getLongExtra("Id_job",0)
        rvDetailJob = findViewById(R.id.rvDetailJob)
        rvDetailJob!!.layoutManager = LinearLayoutManager(this)
        rvDetailJob!!.setHasFixedSize(true)

        if (helperPrefs.getPilih()!!.toString().equals("historyc") ||
            helperPrefs.getPilih()!!.toString().equals("historyr")){
            add.visibility = View.INVISIBLE
            id_chat.visibility = View.GONE
        }
        if (helperPrefs.getPilih()!!.toString() == "create") {
            l_fab.visibility = View.INVISIBLE
        }

        getDataJob(Id_job!!.toLong())
        getDataDetailJob(Id_job!!.toLong())

//        Toast.makeText(this, "${helperPrefs.getPilih()!!.toString()}", Toast.LENGTH_SHORT).show()



        add.setOnClickListener {
//            content_add.visibility = View.VISIBLE
                //Inflate the dialog with custom view
                val aDialogView = LayoutInflater.from(this@ProsesCreateJobActivity).inflate(R.layout.add_detail, null)
                //AlertDialogBuilder
                val mBuilder = AlertDialog.Builder(this@ProsesCreateJobActivity)
                    .setView(aDialogView)
                    .setTitle("Tambah Detail")
                //show dialog
                val  mAlertDialog = mBuilder.show()

             aDialogView.btn_simpan_detail.setOnClickListener {
                //dismiss dialog

                //get text from EditTexts of custom layout
                var deskripsi = aDialogView.et_deskripsi.text.toString()
                //set the input text in TextView
                 if (deskripsi == ""){
                     Toast.makeText(this@ProsesCreateJobActivity, "Deskripsi Harus Di isi!!", Toast.LENGTH_SHORT).show()
                 }else{
                addDetailJob(Id_job!!.toLong(), deskripsi)
                     mAlertDialog.dismiss()
                 }
             }

        }

    }

    fun transactionDone(id_job : Long){
        val builder = AlertDialog.Builder(this@ProsesCreateJobActivity)
        builder.setTitle("Pekerjaan Selesai?")
        builder.setMessage("Apakah pekerjaan sudah di selesaikan?")
        builder.setPositiveButton("YES"){dialog, which ->
//            Toast.makeText(applicationContext,"Ok, we change the app background.",Toast.LENGTH_SHORT).show()
            dbref = FirebaseDatabase.getInstance().getReference("DataJob/${id_job}")
            dbref.child("/isdone").setValue(1)
            Toast.makeText(this, "Pekerjaan Telah di Selesaikan", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@ProsesCreateJobActivity, MainActivity::class.java))
        }

//        builder.setNegativeButton("No"){dialog,which ->
//            Toast.makeText(applicationContext,"You are not agree.",Toast.LENGTH_SHORT).show()
//        }

        builder.setNeutralButton("Cancel"){_,_ ->
            Toast.makeText(applicationContext,"Kamu membatalkan penyelesaian.",Toast.LENGTH_SHORT).show()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun getDataReceive(id: String) {
        val dbRefUser = FirebaseDatabase.getInstance().getReference("users/${id}")
        dbRefUser.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.child("/foto").value.toString() != "null") {
                    Glide.with(this@ProsesCreateJobActivity)
                        .load(p0.child("/foto").value.toString())
                        .into(avatar)
                }
                nama_receive.text = p0.child("/nama").value.toString()
                receive_department.text = p0.child("/department").value.toString()
                id_chat.setOnClickListener {
                    val friend = Friend("${p0.child("/userid").value.toString()}" , "${p0.child("nama").value.toString()}", "${p0.child("/foto").value.toString()}")
                    ChatDetailsActivity.navigate(this@ProsesCreateJobActivity ,it, friend)
                }
            }

        })
    }

    private fun getDataJob(Id_job : Long) {

        val dbRefUser = FirebaseDatabase.getInstance().getReference("DataJob/$Id_job")
        dbRefUser.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                Glide.with(this@ProsesCreateJobActivity)
                    .load(p0.child("/image").value.toString())
                    .into(id_image)
                id_judul.text = p0.child("/judul").value.toString()
                id_nama.text = p0.child("/nama").value.toString()
                id_department.text = p0.child("/department").value.toString()
                id_tanggal.text = p0.child("/dodate").value.toString()
                id_deskripsi.text = p0.child("/deskripsi").value.toString()

                if (p0.child("/isdone").value.toString().toLong() == 1L){
                    if(helperPrefs.getPilih()!!.toString() == "historyc" || helperPrefs.getPilih()!!.toString() == "historyr"){

                    }else{
                        Toast.makeText(applicationContext,"Pekerjaan Selesai.",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@ProsesCreateJobActivity, MainActivity::class.java))
                    }
                }else{

                }
                if (p0.child("/id_receive").value.toString() == "null"){
                    id_wait.visibility = View.VISIBLE
                    btn_selesai.setOnClickListener {
                        alertReceive()
                    }
                }else{
                    id_wait.visibility = View.GONE
                    btn_selesai.setOnClickListener {
                        transactionDone(Id_job!!.toLong())
                    }
                    if (helperPrefs.getPilih()!!.toString() == "create") {
                        btn_selesai.visibility = View.VISIBLE
                        getDataReceive(p0.child("/id_receive").value.toString())
                    }else if(helperPrefs.getPilih()!!.toString() == "historyc"){
                        btn_selesai.visibility = View.GONE
                        getDataReceive(p0.child("/id_receive").value.toString())
                    }else if(helperPrefs.getPilih()!!.toString() == "receive" ||
                        helperPrefs.getPilih()!!.toString() == "historyr"){
                        btn_selesai.visibility = View.GONE
                        getDataReceive(p0.child("/id_user").value.toString())
                    }
                }
                id_image.setOnClickListener {
                    //Inflate the dialog with custom view
                    val mDialogView = LayoutInflater.from(this@ProsesCreateJobActivity).inflate(R.layout.view_image, null)
                    //AlertDialogBuilder
                    val mBuilder = AlertDialog.Builder(this@ProsesCreateJobActivity)
                        .setView(mDialogView)
                        .setTitle("Image")
                    //show dialog
                    val  mAlertDialog = mBuilder.show()
                    //login button click of custom layout
                    //cancel button click of custom layout
                    Glide.with(this@ProsesCreateJobActivity)
                        .load(p0.child("/image").value.toString())
                        .into(mDialogView.id_view_image)
                    mDialogView.dialogCancelBtn.setOnClickListener {
                        //dismiss dialog
                        mAlertDialog.dismiss()
                    }
                }
            }

        })
    }

    fun alertReceive(){
        val builder = AlertDialog.Builder(this@ProsesCreateJobActivity)
        builder.setTitle("Penerima Belum Ada!!")
        builder.setMessage("Pekerjaan tidak bisa di selesai apabila belum ada penerima!")
//        builder.setNegativeButton("No"){dialog,which ->
//            Toast.makeText(applicationContext,"You are not agree.",Toast.LENGTH_SHORT).show()
//        }

        builder.setNeutralButton("Tutup"){_,_ ->
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun getDataDetailJob(Id_job : Long){
        dbref = FirebaseDatabase.getInstance().getReference("DataDetailJob")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                list = ArrayList<DetailJobModel>()
                for (dataSnapshot in p0.children) {
                    val addDataAll = dataSnapshot.getValue(DetailJobModel::class.java)

//                    if(dataSnapshot == null){
//                        rvDetailJob!!.adapter = null
//                    }else{

                    if (addDataAll!!.getId_job() == Id_job!!.toLong()) {
//                        addDataAll!!.setKey(dataSnapshot.key!!)
                        list.add(addDataAll)
                    }
//                    Log.e("TAG_ERROR", "${list}")
//                    }
//                    Log.e("view", "${dataSnapshot}")
                }
                ProsesCreateJobAdapter = ProsesCreateJobAdapter(this@ProsesCreateJobActivity, list)
                rvDetailJob!!.adapter = ProsesCreateJobAdapter
                rvDetail = findViewById(R.id.rvDetailJob)
                falldown = AnimationUtils.loadAnimation(this@ProsesCreateJobActivity, R.anim.item_animation_fall_down)
                rvDetail.startAnimation(falldown)
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e("TAG_ERROR", p0.message)
            }

        })

    }

    fun addDetailJob(Id_job : Long, deskripsi : String){

        dbref = FirebaseDatabase.getInstance().getReference("DataDetailJob/")
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(this@ProsesCreateJobActivity, "Gk iso", Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    a = 1
                    if (p0.exists()) {
                        p0.children.indexOfLast {
                            a = it.key!!.toLong() + 1
                            true
                        }
                    }
                    dbref.child("/$a/id_job").setValue(Id_job!!.toLong())
                    dbref.child("/$a/id_detail_job").setValue(a)
                    dbref.child("/$a/id_user").setValue(set.readSetting(Const.PREF_MY_NAME))
                    dbref.child("/$a/deskripsi").setValue(deskripsi)
                    dbref.child("/$a/isdone").setValue(0)
                    Toast.makeText(this@ProsesCreateJobActivity, "Sukses!!", Toast.LENGTH_SHORT).show()
                }

            })
    }

    fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        actionBar = supportActionBar
        actionBar!!.setTitle("Proses Pekerjaan")
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                if (helperPrefs.getPilih()!!.toString() == "create"){
                startActivity(Intent(this, DataCreateJobActivity::class.java))
                }else if (helperPrefs.getPilih()!!.toString() == "receive"){
                    startActivity(Intent(this, ProsesReceiveJobActivity::class.java))
                }
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}