package com.example.datatemanyuyu

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.datatemanyuyu.databinding.ActivityLoginBinding
import com.example.datatemanyuyu.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var auth:FirebaseAuth? = null
    private val RC_SING_IN = 1
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logout.setOnClickListener(this)
        binding.save.setOnClickListener(this)
        binding.showData.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()
    }
    private fun isEmty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    override fun onClick(p0: View?) {
        when(p0?.getId()){
            R.id.save ->{
                //mendapatkan userid dari pengguna yang terautentikasi
                val getUserID = auth!!.currentUser!!.uid
                //mendapatkan instance dari database
                val database = FirebaseDatabase.getInstance()
                //menyimpan data yang diimputkan user kedalam variable
                val getName: String = binding.nama.getText().toString()
                val getAlamat: String = binding.alamat.getText().toString()
                val getNoHP: String = binding.noHp.getText().toString()
                //mendapatkan referensi dari database
                val getReference: DatabaseReference
                getReference = database.reference
                //mengecek apakah ada data kosong
                if (isEmty(getName) || isEmty(getAlamat) || isEmty(getNoHP)) {
                //jika ada,maka akan menampilkan pesan singkat seperti berikut ini
                   Toast.makeText(this@MainActivity,"Data tidak boleh ada yg kosong",
                       Toast.LENGTH_SHORT).show()
                } else {
                  //jika tidak ada,maka menyimpan ke database sesuai id masing"akun
                    getReference.child("admin").child(getUserID).child("DataTeman").push()
                        .setValue(data_teman(getName ,getAlamat ,getNoHP))
                        .addOnCompleteListener(this){
                            //bagian ini terjadi ketika user berhsil menyimpan data
                            binding.nama.setText("")
                            binding.alamat.setText("")
                            binding.noHp.setText("")
                            Toast.makeText(this@MainActivity,"Data tersimpan",
                                Toast.LENGTH_SHORT).show()
                        }

                }
                }
            R.id.logout ->{
                AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(object : OnCompleteListener<Void>{
                     override fun onComplete(p0: Task<Void>) {
                         Toast.makeText(
                             this@MainActivity, "logout berhasil",
                             Toast.LENGTH_SHORT
                         ).show()
                         intent = Intent(applicationContext, LoginActivity::class.java)
                         startActivity(intent)
                         finish()
                     }
                    })
            }
            R.id.show_data->{
                startActivity(Intent(this@MainActivity,MyListData::class.java))
            }
        }
    }
}