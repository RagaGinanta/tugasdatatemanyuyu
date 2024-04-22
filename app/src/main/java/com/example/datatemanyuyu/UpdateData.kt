package com.example.datatemanyuyu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.TextUtils.isEmpty
import android.view.View
import android.widget.Toast
import com.example.datatemanyuyu.databinding.ActivityMyListDataBinding
import com.example.datatemanyuyu.databinding.ActivityUpdateDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
class UpdateData : AppCompatActivity() {
    //deklarasikan variabel
    private var database :DatabaseReference? =null
    private var auth: FirebaseAuth? = null
    private var cekNama: String? = null
    private var cekAlamat:String? = null
    private var cekNoHP:String? = null
    private lateinit var binding: ActivityUpdateDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Update Data" // Menggunakan safe call operator (?)



    //mendapatkan intance autentikasi dan referensi dari database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        data //memanggil method "data"
        binding.updete.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                // MENDAPATKAN DATA TEMAN YG AKAN DICEK
                cekNama = binding.newNama.getText().toString()
                cekAlamat = binding.newAlamat.getText().toString()
                cekNoHP = binding.newNohp.getText().toString()

                //mengecek agar tidak ada data yang kosong
                if (isEmpty(cekNama!!) || isEmpty(cekAlamat!!) || isEmpty(cekNoHP!!)) {
                    Toast.makeText(this@UpdateData,"Data tidak boleh kosong",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    //menjalankan update data
                    val setTeman = data_teman()
                    setTeman.nama = binding.newNama.getText().toString()
                    setTeman.alamat = binding.newAlamat.getText().toString()
                    setTeman.no_hp = binding.newNohp.getText().toString()
                    updateTeman(setTeman)
                }

            }
        })
    }
    //mengecek apakah ada data kosong,sebelum update
    private fun isEmpty(s: String): Boolean{
        return TextUtils.isEmpty(s)
    }
    //menampilkan data yang akan di updete
    private val data:Unit
        private get(){
         val getNama = intent.extras!!.getString("dataNama")
         val getAlamat = intent.extras!!.getString("dataAlamat")
         val getNoHP = intent.extras!!.getString("dataNoHP")
         binding.newNama!!.setText(getNama)
         binding.newAlamat!!.setText(getAlamat)
         binding.newNohp!!.setText(getNoHP)
        }
    //proses update data yang sudah ditentukam
    private fun updateTeman(teman:data_teman){
        val userID = auth!!.uid
        val getkey = intent.extras!!.getString("getPrimaryKey")
        database!!.child("admin")
            .child(userID.toString())
            .child("DataTeman")
            .child(getkey!!)
            .setValue(teman)
            .addOnSuccessListener{
                binding.newNama!!.setText("")
                binding.newAlamat!!.setText("")
                binding.newNohp!!.setText("")
                Toast.makeText(this@UpdateData,"Data berhasil di ubah",
                    Toast.LENGTH_SHORT)


                    .show()
                finish()


            }
    }

}