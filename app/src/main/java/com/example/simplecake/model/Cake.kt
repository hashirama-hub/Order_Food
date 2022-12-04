package com.example.simplecake.model

import com.example.simplecake.model.PhotoCakeDetails1
import java.io.Serializable


data class Cake1(var id:String?=null,
                 var name:String?=null,
                 var image:String?=null,
                 var listPhoto:ArrayList<PhotoCakeDetails1>?=null,
                 var idCategory: String?=null,
                 var price:Long?=null,
                 var desc:String?=null

):Serializable {
}