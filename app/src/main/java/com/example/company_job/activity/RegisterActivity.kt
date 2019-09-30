package com.example.company_job.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Log.e
import android.view.View
import android.widget.*
import com.example.company_job.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.register_activity.*

class RegisterActivity: AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()
    lateinit var mDatabase: DatabaseReference
    lateinit var dbRefdepartment: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)
        val regBtn = findViewById<View>(R.id.btn_regis) as Button


        mDatabase = FirebaseDatabase.getInstance().getReference("users")

        member_login_tv.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        listDepartment()
        regBtn.setOnClickListener(View.OnClickListener { view ->
            Register()
        })

    }

    fun listDepartment(){
        dbRefdepartment = FirebaseDatabase.getInstance().getReference("department/")
        dbRefdepartment.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@RegisterActivity, "gak kenek", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.e("uid", helperPrefs.getUID())

//                e("Data : ", p0.child("/name").value.toString())
                val array = ArrayList<String>()
                for(dataSnapshot in p0.children){
                    if (dataSnapshot.child("name").value.toString() != "Admin") {
                        e("Data : ", "${dataSnapshot.child("name").value.toString()}")
                        array.add(dataSnapshot.child("name").value.toString())
                    }
                }
                department.adapter = ArrayAdapter(this@RegisterActivity, android.R.layout.simple_dropdown_item_1line, array)

            }

        })
    }

    private fun Register() {
        val emailTxt = findViewById<View>(R.id.email) as EditText
        val passwordTxt = findViewById<View>(R.id.password) as EditText
        val namaTxt = findViewById<View>(R.id.nama) as EditText
        val nomorTxt = findViewById<View>(R.id.nomor_telepon) as EditText
        val departmentTxt = findViewById<View>(R.id.department) as Spinner

        var email = emailTxt.text.toString()
        var nama = namaTxt.text.toString()
        var password = passwordTxt.text.toString()
        var department = departmentTxt.selectedItem.toString()
        var nomor = nomorTxt.text.toString()

        if (!nama.isEmpty() && !email.isEmpty() && !password.isEmpty() && !department.isEmpty() && !nomor.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        val uid = user!!.uid
                        mDatabase.child(uid).child("userid").setValue(uid)
                        mDatabase.child(uid).child("nama").setValue(nama)
                        mDatabase.child(uid).child("email").setValue(email)
                        mDatabase.child(uid).child("department").setValue(department)
                        mDatabase.child(uid).child("phone").setValue(nomor)
                        mDatabase.child(uid).child("password").setValue(password)
                        mDatabase.child(uid).child("foto").setValue("null")
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))

                        Toast.makeText(this, "Berhasil Daftar", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Email Anda Sudah Terdaftar!!!", Toast.LENGTH_SHORT).show()
                    }
                })
        } else {
            Toast.makeText(this, "tolong isi dengan lengkap", Toast.LENGTH_SHORT).show()
        }
    }
}