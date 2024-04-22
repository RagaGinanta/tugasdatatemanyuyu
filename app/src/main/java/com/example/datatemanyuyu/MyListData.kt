package com.example.datatemanyuyu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.datatemanyuyu.databinding.ActivityMainBinding
import com.example.datatemanyuyu.databinding.ActivityMyListDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyListData : AppCompatActivity(), RecyclerViewAdaptor.dataListener {
    // deklarasi variabel untuk recycler view
    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    //deklarasi variabel data bese reference dan arraylist dengan parameter class model
    val database = FirebaseDatabase.getInstance()
    private var dataTeman = ArrayList<data_teman>()
    private var auth: FirebaseAuth? = null

    private lateinit var binding: ActivityMyListDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyListDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = findViewById(R.id.datalist)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar!!.title = "Data Teman"
        auth = FirebaseAuth.getInstance()
        MyRecyclerView()
        GetData()

    }

    //kode untuk mengambil data dari database & menampilkan ke dalam adapter
    private fun GetData() {
        Toast.makeText(applicationContext, "Mohon Tunggu Sebentar..", Toast.LENGTH_SHORT).show()
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference()
        getReference.child("admin").child(getUserID).child("DataTeman")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        dataTeman.clear()
                        for (snapshot in dataSnapshot.children) {
                            //mapping data pada datasnapshot ke dalam objek datateman
                            val teman = snapshot.getValue(data_teman::class.java)
                            //mengambil primay key untuk membuat proses updet/delete
                            teman?.key = snapshot.key
                            dataTeman.add(teman!!)
                        }
                        //inisialisasi adapter dan data teman dalam bentuk array
                        adapter = RecyclerViewAdaptor(dataTeman, this@MyListData)
                        //memesang adapter pada RecyclerView
                        recyclerView?.adapter = adapter
                        (adapter as RecyclerViewAdaptor).notifyDataSetChanged()
                        Toast.makeText(
                            applicationContext,
                            "Data berhasil dimuat",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //kode ini dijalankan ketika error,simpan ke logcat
                    Toast.makeText(applicationContext, "Data gagal dimuat", Toast.LENGTH_LONG)
                        .show()
                    Log.e("MyListActivity", databaseError.details + " " + databaseError.message)
                }
            })
    }

    //baris kode untuk mengatur RecyclerView
    private fun MyRecyclerView() {
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)

        //buat garis bawah setiap data
        val itemDecoration =
            DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.line)!!)
        recyclerView?.addItemDecoration(itemDecoration)
    }

    override fun onDeleteData(data: data_teman?, position: Int) {
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference()
        if (getReference != null) {
            getReference.child("admin")
                .child(getUserID)
                .child("DataTeman")
                .child(data?.key.toString())
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(
                        this@MyListData, "Data berhsil dihapus",
                        Toast.LENGTH_SHORT
                    ).show();
                }
        } else {
            Toast.makeText(
                this@MyListData, "Reference Kosong",
                Toast.LENGTH_SHORT
            ).show();
        }
    }
}


