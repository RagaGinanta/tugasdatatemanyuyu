package com.example.datatemanyuyu

class data_teman {

    var nama: String? = null
    var alamat: String? = null
    var no_hp: String? = null
    var key: String? = null

    //membuat kontrusi kosong untuk membaca data snapshot
    constructor() {}

    constructor(nama: String?,alamat:String?,no_hp:String) {
        this.nama = nama
        this.alamat = alamat
        this.no_hp = no_hp
    }
}