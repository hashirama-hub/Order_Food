package com.example.simplecake.model

data class Order(
    var id:String?=null,
    var idUser:String?=null,
    var listIdCart:ArrayList<Cart>?=null,
    var address:String?=null,
    var phoneNumber:String?=null,
    var totalPrice:Long?=null,
    var dateOder:String?=null
) {
}