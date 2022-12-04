package com.example.simplecake.model

import java.sql.Timestamp

data class Comment(var id:String?=null,
                   var idUser:String?=null ,
                   var idCake:String?=null,
                   var star:Int=0 ,
                   var comment:String?=null ,
                   var dateComment: String?=null ,
                   ) {
}