package com.example.simplecake.Activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecake.adapter.CheckoutAdapter
import com.example.simplecake.R
import com.example.simplecake.model.Cart
import com.example.simplecake.model.Order
import com.example.simplecake.service.FirebaseService
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.Exception
import java.lang.IndexOutOfBoundsException
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckoutActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener,GoogleMap.OnCameraMoveListener,GoogleMap.OnCameraMoveStartedListener,GoogleMap.OnCameraIdleListener  {
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    var firebaseService= FirebaseService()
    lateinit var rcv_checkout:RecyclerView
    lateinit var tv_total_price:TextView
    lateinit var tv_total_price_order:TextView
    lateinit var tv_total:TextView
    lateinit var et_address:EditText
    lateinit var btn_oder_now:Button
    lateinit var et_phone_number:EditText
    lateinit var scrollView: ScrollView
    val transport_fees=30000
    private var mMap: GoogleMap?=null
    var total_price:Long = 0
    lateinit var mapView: MapView
    lateinit var diaLog:Dialog
    private var MAP_VIEW_BUNDLE_KEY="MapViewBundleKey"
    private val DEFAULT_ZOOM =15f
    private var fusedLocationProviderClient: FusedLocationProviderClient?=null
    lateinit var arraylistCart:ArrayList<Cart>
    override fun onMapReady(googleMap: GoogleMap?) {

        mapView.onResume()
        mMap=googleMap
        moveCamera(
            com.google.android.gms.maps.model.LatLng(15.973791, 108.253041),DEFAULT_ZOOM
        )

        mMap!!.setOnCameraMoveListener(this)
        mMap!!.setOnCameraMoveStartedListener(this)
        mMap!!.setOnCameraIdleListener(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        initUI()
//        val gson = Gson()
        val jsonCart: String? = intent.getStringExtra("json_cart_list")
        val gson = GsonBuilder().create()
        val listCart = gson.fromJson<ArrayList<Cart>>(jsonCart,object :TypeToken<ArrayList<Cart>>(){}.type)
        this.arraylistCart=listCart
        diaLog= Dialog(this)



        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val iduser:String = user!!.uid
        btn_oder_now=findViewById(R.id.btn_order)
        val checkoutAdapter= CheckoutAdapter(this,listCart)
        rcv_checkout.adapter = checkoutAdapter
        calTotalPrice(listCart)
        rcv_checkout.isNestedScrollingEnabled=false
        val linearLayoutManager:LinearLayoutManager  = LinearLayoutManager(this)
        rcv_checkout.layoutManager = linearLayoutManager
        setSupportActionBar(toolbar)
        toolbar.title="Check Out"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mapView=findViewById(R.id.map1)
        var mapViewBundle:Bundle?=null
        if(savedInstanceState!=null){
            mapViewBundle=savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)

        }
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)
        //et_addresss
//        et_address.setOnClickListener {
//            val intent=Intent(this,GoogleMapActivity::class.java)
//            startActivity(intent)
//        }
        val sdf = SimpleDateFormat("yyyy.MM.dd")
        val timestamp = Timestamp(System.currentTimeMillis())
        var today=sdf.format(timestamp)

        firebaseService.getUser(iduser){
            et_phone_number.setText(it.phone,TextView.BufferType.EDITABLE)
        }
        btn_oder_now.setOnClickListener {

         var phone_number:String = et_phone_number.text.toString()
         var lenghtPhone_number:Int = phone_number.length
        if(phone_number !=null && lenghtPhone_number > 0 ) {
            firebaseService.getLastId("lastIdOrder") { lastId ->

                val order = Order(
                    "order${lastId}",
                    iduser,
                    listCart,
                    et_address.text.toString(),
                    phone_number,
                    total_price,
                    today.toString()
                )
                firebaseService.updateLastId("lastIdOrder", lastId) {

                }
                firebaseService.insertOrder(order) {

                }


            }

            openSuccessDialog()
        }
            else{
                Toast.makeText(this,"You must fill Your Phone Number !",Toast.LENGTH_SHORT).show()
        }
        }
    }

    private fun initUI() {
        toolbar = findViewById(R.id.tool_bar_checkout)
        rcv_checkout = findViewById(R.id.rcv_checkout)
        tv_total_price=findViewById(R.id.tv_total_price)
        tv_total_price_order=findViewById(R.id.tv_total_price_order)
        tv_total=findViewById(R.id.tv_total)
        et_address=findViewById(R.id.et_address)
        et_phone_number=findViewById(R.id.et_phone_number)
        scrollView=findViewById(R.id.scrollview_checkout)
    }
    private fun openSuccessDialog(){
        diaLog.setContentView(R.layout.layout_checkout_successfully)
        diaLog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val imageViewClose:ImageView=diaLog.findViewById(R.id.image_close)
        val btnConfirm:Button=diaLog.findViewById(R.id.btn_oke_checkout)
        imageViewClose.setOnClickListener {
            diaLog.dismiss()
        }
        btnConfirm.setOnClickListener {
            diaLog.dismiss()
            val intent=Intent(this, OrderActivity::class.java)
            startActivity(intent)


        }
        diaLog.show()
    }
    fun calTotalPrice(listCart:ArrayList<Cart>){
        for (i in 0 until listCart.size){
                total_price += listCart[i].totalcost!!


            }

            val dcf = DecimalFormat("#,###")
            val dcfs = DecimalFormatSymbols(Locale.getDefault())
            dcfs.groupingSeparator='.'
            dcf.decimalFormatSymbols=dcfs
            tv_total_price_order.text=dcf.format(total_price) + " đ"
            total_price+=30000
            tv_total.text =dcf.format(total_price) + " đ"
            tv_total_price.text =dcf.format(total_price) + " đ"

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun moveCamera(latLng:com.google.android.gms.maps.model.LatLng,zoom:Float){
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom))
        scrollView.isNestedScrollingEnabled=false
        findViewById<LinearLayout>(R.id.ln_checkout).isNestedScrollingEnabled=false

    }

    override fun onLocationChanged(location: Location) {
        val geocoder= Geocoder(this, Locale.getDefault())
        var addresss:List<Address>?=null
        try {
            addresss=geocoder.getFromLocation(location.latitude,location.longitude,1)
        }catch (e: IOException){
            e.printStackTrace()
        }
        setAddress(addresss!![0])
    }

    private fun setAddress(address: Address) {
        if(address!=null){
            if(address.getAddressLine(0)!=null){
                et_address!!.setText(address.getAddressLine(0))
            }
            if(address.getAddressLine(1)!=null){
                et_address.getText().toString()+ address.getAddressLine(0)
            }
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        super.onStatusChanged(provider, status, extras)
    }

    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider)
    }

    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)
    }

    override fun onCameraMove() {

    }

    override fun onCameraMoveStarted(p0: Int) {

    }

    override fun onCameraIdle() {
        var addresses:List<Address>?=null
        val geocoder= Geocoder(this,Locale.getDefault())

        try{
            addresses = geocoder.getFromLocation(mMap!!.cameraPosition.target.latitude,mMap!!.cameraPosition.target.longitude,1)
            setAddress(addresses!![0])
        }catch (e: IndexOutOfBoundsException){
            e.printStackTrace()
        }catch (e: IOException){
            e.printStackTrace()
        }
    }
}