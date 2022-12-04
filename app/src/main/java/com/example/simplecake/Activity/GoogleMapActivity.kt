package com.example.simplecake.Activity

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.simplecake.R
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.lang.Exception
import java.lang.IndexOutOfBoundsException
import java.util.*

class GoogleMapActivity : AppCompatActivity(),OnMapReadyCallback,LocationListener,GoogleMap.OnCameraMoveListener,GoogleMap.OnCameraMoveStartedListener,GoogleMap.OnCameraIdleListener {
    private var mMap:GoogleMap?=null
    lateinit var mapView:MapView
    private var MAP_VIEW_BUNDLE_KEY="MapViewBundleKey"
    private val DEFAULT_ZOOM =15f
    private var fusedLocationProviderClient:FusedLocationProviderClient?=null
    lateinit var tvCurrentAddress:TextView
    lateinit var btn_oke:Button
    override fun onMapReady(googleMap: GoogleMap?) {
        mapView.onResume()
        mMap=googleMap
        askGalleryPermisstionLocation()
        if(ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
        ) !=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        )!= PackageManager.PERMISSION_GRANTED

        ){
            return
        }
        mMap!!.isMyLocationEnabled=true
        mMap!!.setOnCameraMoveListener(this)
        mMap!!.setOnCameraMoveStartedListener(this)
        mMap!!.setOnCameraIdleListener(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_map)
        mapView=findViewById(R.id.map1)
        tvCurrentAddress =findViewById(R.id.tvAdd)
        btn_oke=findViewById(R.id.btn_oke)
        var mapViewBundle:Bundle?=null
        if(savedInstanceState!=null){
            mapViewBundle=savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)

        }
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        askGalleryPermisstionLocation()
        var mapViewBundle =outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if(mapViewBundle==null){
            mapViewBundle= Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY,mapViewBundle)
        }
        mapView.onSaveInstanceState(mapViewBundle)
    }

    private fun askGalleryPermisstionLocation() {
        askPermission(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ){
            getCurrntLocation()
        }.onDeclined{ e->
            if(e.hasDenied()){
                e.denied.forEach{

                }
                AlertDialog.Builder(this)
                    .setMessage("Please accept our permission...")
                    .setPositiveButton("Yes"){_,_ ->
                        e.askAgain()
                    }//ask Again
                    .setNegativeButton("No"){dialog,_->
                        dialog.dismiss()
                    }
                    .show()
            }
            if(e.hasForeverDenied()){
                //this list of foreber denied permisstion,user has check 'never ask again'
                e.foreverDenied.forEach{

                }
                e.goToSettings()
            }
        }
    }
    private fun getCurrntLocation(){
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this@GoogleMapActivity)
        try{
            @SuppressLint("MissingPermission") val location=fusedLocationProviderClient!!.lastLocation
            location.addOnCompleteListener(object : OnCompleteListener<Location> {
                override fun onComplete(p0: Task<Location>) {
                    if(p0.isSuccessful){
                        val currentLocation=p0.result as Location?
                        if(currentLocation!=null){
                          moveCamera(
                            com.google.android.gms.maps.model.LatLng(currentLocation.latitude,currentLocation.longitude),DEFAULT_ZOOM
                          )
                        }
                    }else{
                        askGalleryPermisstionLocation()

                    }
                }
            })
        } catch (se:Exception){
            Log.e("TAG","Security Exception")
        }
    }
    private fun moveCamera(latLng:com.google.android.gms.maps.model.LatLng,zoom:Float){
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom))
    }

    override fun onLocationChanged(location: Location) {
        val geocoder=Geocoder(this, Locale.getDefault())
        var addresss:List<Address>?=null
        try {
            addresss=geocoder.getFromLocation(location.latitude,location.longitude,1)
        }catch (e:IOException){
            e.printStackTrace()
        }
        setAddress(addresss!![0])
    }

    private fun setAddress(address: Address) {
        if(address!=null){
            if(address.getAddressLine(0)!=null){
                tvCurrentAddress!!.setText(address.getAddressLine(0))
            }
            if(address.getAddressLine(1)!=null){
                tvCurrentAddress.getText().toString()+ address.getAddressLine(0)
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
        val geocoder=Geocoder(this,Locale.getDefault())
        try{
            addresses = geocoder.getFromLocation(mMap!!.cameraPosition.target.latitude,mMap!!.cameraPosition.target.longitude,1)
            setAddress(addresses!![0])
        }catch (e:IndexOutOfBoundsException){
            e.printStackTrace()
        }catch (e:IOException){
            e.printStackTrace()
        }
    }

}