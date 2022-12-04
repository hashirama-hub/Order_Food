package com.example.simplecake.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simplecake.Activity.CartActivity
import com.example.simplecake.R
import com.example.simplecake.model.Cart
import com.example.simplecake.service.FirebaseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class CartAdapter(var context: Context,var listCart:ArrayList<Cart>):RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    var firebaseService= FirebaseService()
    var quality:Int = 0
    var price_cart:Long=0
    var cartActivity = CartActivity()
    var OnChange:((Boolean)->Unit)?=null
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val img_cake_cart=view.findViewById<ImageView>(R.id.img_cake_cart)
        val tv_name_cake_cart=view.findViewById<TextView>(R.id.tv_name_cake_cart)
        val tv_price_cart  = view.findViewById<TextView>(R.id.tv_price_cart)
        val btn_plus = view.findViewById<Button>(R.id.btn_plus)
        val btn_minus = view.findViewById<Button>(R.id.btn_minus)
        val et_qualities = view.findViewById<EditText>(R.id.et_qualities)
        val foreground = view.findViewById<RelativeLayout>(R.id.foreground)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseService.getCake(listCart[position].idCake){cake->
            firebaseService.getOnlyImage(cake.image!!) {
                Glide.with(context).load(it).into(holder.img_cake_cart)
            }
            val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
            val iduser:String = user!!.uid
            quality= listCart[position].qualities!!
            holder.et_qualities.setText(quality.toString(),TextView.BufferType.EDITABLE)
           holder.tv_name_cake_cart.text=cake.name
            val dcf = DecimalFormat("#,###")
            val dcfs = DecimalFormatSymbols(Locale.getDefault())
            dcfs.groupingSeparator='.'
            dcf.decimalFormatSymbols=dcfs
            holder.tv_price_cart.text =dcf.format(listCart[position].totalcost) + " đ"
            holder.et_qualities.doAfterTextChanged {
               var qualityLenght =  holder.et_qualities.text.toString().length
               if(qualityLenght>0){
                  quality=holder.et_qualities.text.toString().toInt()
                   price_cart = cake.price!!
                   price_cart *= quality
                   holder.tv_price_cart.text = dcf.format(price_cart) + " đ"
                   var idCart = listCart[position].id
                   if (idCart != null) {
                       firebaseService.updateQualitiesCart(idCart, quality) {
                       }
                       firebaseService.updatePriceCart(idCart, price_cart) {
                       }

                   }
                   OnChange?.invoke(true)
               }
                else{
                   quality=1
                   holder.et_qualities.setText(
                       quality.toString(),
                       TextView.BufferType.EDITABLE
                   )
                   price_cart = cake.price!!
                   price_cart *= quality
                   holder.tv_price_cart.text = dcf.format(price_cart) + " đ"
                   var idCart = listCart[position].id
                   if (idCart != null) {
                       firebaseService.updateQualitiesCart(idCart, quality) {
                       }
                       firebaseService.updatePriceCart(idCart, price_cart) {
                       }

                   }
               }
            }
            holder.btn_plus.setOnClickListener {
                quality++
                holder.et_qualities.setText(
                    quality.toString(),
                    TextView.BufferType.EDITABLE
                )
                price_cart = cake.price!!
                price_cart *= quality
                holder.tv_price_cart.text = dcf.format(price_cart) + " đ"
                var idCart = listCart[position].id
                if (idCart != null) {
                    firebaseService.updateQualitiesCart(idCart, quality) {
                    }
                    firebaseService.updatePriceCart(idCart, price_cart) {
                    }

                }
                OnChange?.invoke(true)


            }
            holder.btn_minus.setOnClickListener {
                firebaseService.getCart(listCart[position].id) {
                    quality = it.qualities!!
                    if (quality > 1) {
                        quality--
                        holder.et_qualities.setText(
                            quality.toString(),
                            TextView.BufferType.EDITABLE
                        )
                        price_cart = cake.price!!
                        price_cart *= quality
                        holder.tv_price_cart.text = dcf.format(price_cart) + " đ"
                        var idCart = listCart[position].id
                        if (idCart != null) {
                            firebaseService.updateQualitiesCart(idCart, quality) {
                            }
                            firebaseService.updatePriceCart(idCart, price_cart) {
                            }

                        }
                        OnChange?.invoke(true)


                    } else {
                        Toast.makeText(context, "The qualities > 0", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

    }
    fun removeItem(index:Int){
        firebaseService.delCart(listCart[index].id){
            if(it==true){
                Toast.makeText(context,"Delete Cart Successfully",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context,"Delete Cart Failed",Toast.LENGTH_SHORT).show()
            }
        }
        OnChange?.invoke(true)
        listCart.removeAt(index)
        notifyItemRemoved(index)
    }
    override fun getItemCount(): Int {
        return listCart.size
    }
}