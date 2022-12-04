package com.example.simplecake.model

data class Cart(
    var id:String?=null,
    var idUser:String?=null,
    var idCake:String?=null,
    var qualities:Int?=null,
    val totalcost:Long?=null
) {
}