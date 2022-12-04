package com.example.simplecake.service

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.example.simplecake.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FirebaseService {
    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    private val tableCategory = db.collection("categories")
    private val tableCakes = db.collection("cakes")
    private val tableCarts = db.collection("carts")
    private val tableLastId = db.collection("lastID")
    private val tableCheckout = db.collection("checkout")
    private val tableOrder = db.collection("order")
    private val tableFavorite = db.collection("favorite")
    private val tableReceive = db.collection("received")
    private val tableCancle = db.collection("cancle")
    private val tableComment = db.collection("comments")
    private val tableUser = db.collection("users")
    fun getLastId(nameDocument:String,callback: (status:Int) -> Unit){
        tableLastId.document(nameDocument).get().addOnSuccessListener { task ->
            callback.invoke(task.getString("lastId").toString().toInt())
        }

    }
    fun updateLastId(nameDocument: String,value:Int,callback: (status: Boolean) -> Unit){
        tableLastId.document(nameDocument).update("lastId","${value+1}").addOnSuccessListener {
            callback.invoke(true)
        }
    }

    fun insertCategory(
        category: category1, callback: (status:Boolean)->Unit
    ){
        tableCategory.document(category.id.toString()).set(category).addOnCompleteListener {
            callback.invoke(true)
        }.addOnFailureListener {
            callback.invoke(false)
        }
    }
    fun insertCake (cake: Cake1, callback: (status: Boolean) -> Unit){
        tableCakes.document(cake.id.toString()).set(cake).addOnCompleteListener {
            callback.invoke(true)
        }.addOnFailureListener {
            callback.invoke(false)
        }
    }

    fun getCategories(callback: (categoryList:ArrayList<category1>) -> Unit){
        db.collection("categories").get().addOnSuccessListener { result ->
            var arrayListCategories = ArrayList<category1>()
            for (document in result) {

                var cate = document.toObject(category1::class.java)
                arrayListCategories.add(cate)
            }
            callback.invoke(arrayListCategories)
        }
    }
    fun getCake(idCake: String?, callback: (cake: Cake1)->Unit){
        db.collection("cakes").whereEqualTo("id",idCake).get().addOnSuccessListener { result->
            val cake: Cake1
            for(document in result){

                val cake= document.toObject(Cake1::class.java)
                callback.invoke(cake)
            }

        }
    }
    fun getCate(idCate: String?, callback: (category1: category1)->Unit){
        db.collection("categories").whereEqualTo("id",idCate).get().addOnSuccessListener { result->
            val category1: category1
            for(document in result){

                val category1= document.toObject(com.example.simplecake.model.category1::class.java)
                callback.invoke(category1)
            }

        }
    }
    fun getListCake(idCategory: String?, callback: (cakeList:ArrayList<Cake1>)->Unit){
        db.collection("cakes").whereEqualTo("idCategory",idCategory).get().addOnSuccessListener { result->

            val arrayListCake1 = ArrayList<Cake1>()
            for(document in result){

                val cake= document.toObject(Cake1::class.java)
                arrayListCake1.add(cake)
            }
            callback.invoke(arrayListCake1)
        }
    }
    fun getFullListCake(callback: (cakeList:ArrayList<Cake1>)->Unit){
        db.collection("cakes").orderBy("idCategory").get().addOnSuccessListener { result->

            val arrayListCake1 = ArrayList<Cake1>()
            for(document in result){

                val cake= document.toObject(Cake1::class.java)
                arrayListCake1.add(cake)
            }
            callback.invoke(arrayListCake1)
        }
    }
    fun getOnlyImage(nameOfImage: String, callback: (uriOfImage: Uri) -> Unit) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        storageRef.child("image/${nameOfImage}.png").downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback.invoke(task.result)
            }
        }
    }
    // cart
    fun insertCart(
        cart: Cart, callback: (status:Boolean)->Unit
    ){
        tableCarts.document(cart.id.toString()).set(cart).addOnCompleteListener {
            callback.invoke(true)
        }.addOnFailureListener {
            callback.invoke(false)
        }
    }
    fun getCartList(idUser:String,callback:(cartList:ArrayList<Cart>)->Unit){
        db.collection("carts").whereEqualTo("idUser",idUser).get().addOnSuccessListener {
            val arrayListCart = ArrayList<Cart>()
            for(document in it){
                val cart = document.toObject(Cart::class.java)
                arrayListCart.add(cart)
            }
            callback.invoke(arrayListCart)
        }
    }
    fun getCart(idCart: String?, callback: (cart: Cart)->Unit){
        db.collection("carts").whereEqualTo("id",idCart).get().addOnSuccessListener { result->
            val cart: Cart
            for(document in result){

                val cart= document.toObject(Cart::class.java)
                callback.invoke(cart)
            }

        }
    }
    fun delCart(idCart: String?,callback: (status: Boolean) -> Unit){
        db.collection("carts").document(idCart!!).delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }
    fun updateQualitiesCart(idCart: String,qualities:Int,callback: (status: Boolean) -> Unit){
        tableCarts.document(idCart).update("qualities",qualities).addOnSuccessListener {
            callback.invoke(true)
        }
    }
    fun updatePriceCart(idCart: String,price:Long,callback: (status: Boolean) -> Unit){
        tableCarts.document(idCart).update("totalcost",price).addOnSuccessListener {
            callback.invoke(true)
        }
    }
    //order code
    fun getOrder(idOrder: String?, callback: (order: Order)->Unit){
        db.collection("order").whereEqualTo("id",idOrder).get().addOnSuccessListener { result->
            val order: Order
            for(document in result){

                val order= document.toObject(Order::class.java)
                callback.invoke(order)
            }

        }
    }
    fun insertOrder (order: Order, callback: (status: Boolean) -> Unit){
        tableOrder.document(order.id.toString()).set(order).addOnCompleteListener {
            callback.invoke(true)
        }.addOnFailureListener {
            callback.invoke(false)
        }
    }
    fun getListOrder(idUser: String,callback:(listOrder:ArrayList<Order>)->Unit){
        tableOrder.whereEqualTo("idUser",idUser).get().addOnSuccessListener {
            val listOrder = ArrayList<Order>()
            for(document in it){
                val order = document.toObject(Order::class.java)
                listOrder.add(order)
            }
            callback.invoke(listOrder)
        }
    }
    //favorite
    fun insertFavorite (favorite: favorite, callback: (status: Boolean) -> Unit){
        tableFavorite.document(favorite.id.toString()).set(favorite).addOnCompleteListener {
            callback.invoke(true)
        }.addOnFailureListener {
            callback.invoke(false)
        }
    }
    fun getListFavorite(idUser:String,callback: (listFavorite: ArrayList<favorite>) -> Unit){
       tableFavorite.whereEqualTo("idUser",idUser).get().addOnSuccessListener {
            val listFavorite = ArrayList<favorite>()
            for(document in it){
                val favorite = document.toObject(favorite::class.java)
                listFavorite.add(favorite)
            }
            callback.invoke(listFavorite)
        }
    }
    //receive
    fun getListReceive(idUser: String,callback:(listOrder:ArrayList<Order>)->Unit){
        tableReceive.whereEqualTo("idUser",idUser).get().addOnSuccessListener {
            val listOrder = ArrayList<Order>()
            for(document in it){
                val order = document.toObject(Order::class.java)
                listOrder.add(order)
            }
            callback.invoke(listOrder)
        }
    }
    fun insertReceive (receive: Order, callback: (status: Boolean) -> Unit){
        tableReceive.document(receive.id.toString()).set(receive).addOnCompleteListener {
            callback.invoke(true)
        }.addOnFailureListener {
            callback.invoke(false)
        }
    }
    fun delOrder(id: String?,callback: (status: Boolean) -> Unit){
        db.collection("order").document(id!!).delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }
    //cancle
    fun insertCancle (cancle: Order, callback: (status: Boolean) -> Unit){
        tableCancle.document(cancle.id.toString()).set(cancle).addOnCompleteListener {
            callback.invoke(true)
        }.addOnFailureListener {
            callback.invoke(false)
        }
    }
    fun delCancel(id: String?,callback: (status: Boolean) -> Unit){
        db.collection("cancle").document(id!!).delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }
    fun updateDateOrder(idOrder:String,dateReceive: String,callback: (status: Boolean) -> Unit){
        tableOrder.document(idOrder).update("dateOder",dateReceive).addOnSuccessListener {
            callback.invoke(true)
        }
    }
    fun getListCanceled(idUser: String,callback:(listCanceled:ArrayList<Order>)->Unit){
        tableCancle.whereEqualTo("idUser",idUser).get().addOnSuccessListener {
            val listCanceled = ArrayList<Order>()
            for(document in it){
                val order = document.toObject(Order::class.java)
                listCanceled.add(order)
            }
            callback.invoke(listCanceled)
        }
    }
    fun updateDateCancle(idOrder:String,dateReceive: String,callback: (status: Boolean) -> Unit){
        tableCancle.document(idOrder).update("dateOder",dateReceive).addOnSuccessListener {
            callback.invoke(true)
        }
    }
    fun getCanceled(idCanceled: String?, callback: (order: Order)->Unit){
        tableCancle.whereEqualTo("id",idCanceled).get().addOnSuccessListener { result->
            val order: Order
            for(document in result){

                val order= document.toObject(Order::class.java)
                callback.invoke(order)
            }

        }
    }
    //comment
    fun insertComment (comment: Comment, callback: (status: Boolean) -> Unit){
        tableComment.document(comment.id.toString()).set(comment).addOnCompleteListener {
            callback.invoke(true)
        }.addOnFailureListener {
            callback.invoke(false)
        }
    }
    fun getListComment(idCake: String,callback:(listComment:ArrayList<Comment>)->Unit){
        tableComment.whereEqualTo("idCake",idCake).get().addOnSuccessListener {
            val listComment = ArrayList<Comment>()
            for(document in it){
                val comment = document.toObject(Comment::class.java)
                listComment.add(comment)
            }
            callback.invoke(listComment)
        }
    }
    fun getFistComment(idCake: String,callback:(listComment:ArrayList<Comment>)->Unit){
        tableComment.whereEqualTo("idCake",idCake).limit(1).get().addOnSuccessListener {
            val listComment = ArrayList<Comment>()
            for(document in it){
                val comment = document.toObject(Comment::class.java)
                listComment.add(comment)
            }
            callback.invoke(listComment)
        }
    }
    //user
    fun insertUser (user: User, callback: (status: Boolean) -> Unit){
        tableUser.document(user.id.toString()).set(user).addOnCompleteListener {
            callback.invoke(true)
        }.addOnFailureListener {
            callback.invoke(false)
        }
    }
    fun getUser(id: String?, callback: (user: User)->Unit){
        tableUser.whereEqualTo("id",id).get().addOnSuccessListener { result->
            val user: User
            for(document in result){

                val user= document.toObject(User::class.java)
                callback.invoke(user)
            }

        }
    }
}