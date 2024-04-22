package com.example.datatemanyuyu

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdaptor (private val dataTeman: ArrayList<data_teman>,context: Context):
        RecyclerView.Adapter<RecyclerViewAdaptor.ViewHolder>() {
    private val context:Context

            //view holder digunakan untuk menyimpan referensi dari view"
            inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
                val Nama:TextView
                val alamat:TextView
                val NoHP:TextView
                val ListItem: LinearLayout
                //menginisialisasi view yang terpasang pada layout RecyclerView
                init {
                    Nama = itemView.findViewById(R.id.namax)
                    alamat = itemView.findViewById(R.id.alamatx)
                    NoHP = itemView.findViewById(R.id.no_hpx)
                    ListItem = itemView.findViewById(R.id.list_item)

                }
            }
    //membuat view untuk menyiapkan dan memasang layout yang digunakan pada Recycler View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val V: View = LayoutInflater.from(parent.getContext()) . inflate(
            R.layout.view_design,parent,false)
        return ViewHolder(V)
    }

    @SuppressLint("SetText18n")
    //mengambil nilai atau value pada Recycler View berdasarkan posisi tertentu
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val Nama : String? = dataTeman.get(position).nama
        val Alamat : String? = dataTeman.get(position).alamat
        val NoHP: String? = dataTeman.get(position).no_hp

        //masukkan nilai atau value ke dalam view
        holder.Nama.text = "Nama: $Nama"
        holder.alamat.text = "Alamat: $Alamat"
        holder.NoHP.text = "no hp: $NoHP"
        holder.ListItem.setOnLongClickListener(object : View.OnLongClickListener{
                override fun onLongClick(v: View?): Boolean {
                    //Fungsi untuk edit dan delete
                    holder.ListItem.setOnLongClickListener {view ->
                        val action = arrayOf("Updet","Delete")
                        val alert: AlertDialog.Builder = AlertDialog.Builder(view.context)
                        alert.setItems(action,DialogInterface.OnClickListener { dialog, i ->
                            when (i) {
                                0 -> {
                                    //berpindah ke hal updete data untuk ambil data pada list data_teman
                                    val bundle = Bundle()
                                    bundle.putString("dataNama", dataTeman[position].nama)
                                    bundle.putString("dataAlamat", dataTeman[position].alamat)
                                    bundle.putString("dataNoHP", dataTeman[position].no_hp)
                                    bundle.putString("getPrimaryKey", dataTeman[position].key)
                                    val intent = Intent(view.context, UpdateData::class.java)
                                    intent.putExtras(bundle)
                                    context.startActivity(intent)
                                }
                                1 -> {
                                    //menggunakan interface untuk mengirim data teman yang akan di hapus
                                    listener?.onDeleteData(dataTeman.get(position),position)
                                }
                            }
                        })
                        alert.create()
                        alert.show()
                        true
                    }

                    return true
                }
            })
    }

    //menghitung ukuran jumlah data yang akan di tampilkan pada recyclerview
    override fun getItemCount(): Int {
        return dataTeman.size
    }
        //membuat interfece
    interface dataListener {
        fun onDeleteData(data: data_teman?,position: Int)
    }
    //deklarasi objek dari interfece
    var listener: dataListener? = null
    //membuat kontruktor,untuk menerima dari data base


    //membuat konstruksi, untuk menerima imput dari database
    init {
        this.context=context
        this.listener = context as MyListData
    }


    }
