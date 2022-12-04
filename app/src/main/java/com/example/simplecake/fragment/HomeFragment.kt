package com.example.simplecake.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.simplecake.*
import com.example.simplecake.Activity.ListCakeAtivity
import com.example.simplecake.Activity.MainActivity
import com.example.simplecake.adapter.CategoryAdapter
import com.example.simplecake.adapter.ListCategoryApdapter
import com.example.simplecake.adapter.PhotoAdapter
import com.example.simplecake.adapter.ReviewAdapter
import com.example.simplecake.model.Photo
import com.example.simplecake.model.Review
import com.example.simplecake.model.category1
import com.example.simplecake.service.FirebaseService
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(), OnMapReadyCallback, LocationListener,
    GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener,
    GoogleMap.OnCameraIdleListener {
    lateinit var viewpager: ViewPager
    lateinit var mView:View
    lateinit var mMainActivity: MainActivity
    lateinit var mViewPager1: ViewPager
    lateinit var mapView: MapView
    lateinit var diaLog: Dialog
    private var MAP_VIEW_BUNDLE_KEY="MapViewBundleKey"
    private val DEFAULT_ZOOM =15f
    private var fusedLocationProviderClient: FusedLocationProviderClient?=null
    var timer:Timer?=null
    private var mMap: GoogleMap?=null
    private var param1: String? = null
    private var param2: String? = null
    override fun onMapReady(googleMap: GoogleMap?) {
        mapView.onResume()
        mMap=googleMap
        askGalleryPermisstionLocation()
        if(ActivityCompat.checkSelfPermission(
                mMainActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mMainActivity,
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

    private fun askGalleryPermisstionLocation() {
        askPermission(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ){
            getCurrentLocation()
        }.onDeclined{ e->
            if(e.hasDenied()){
                e.denied.forEach{

                }
                AlertDialog.Builder(mMainActivity)
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
    private fun getCurrentLocation(){
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(mMainActivity)
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
        } catch (se: Exception){
            Log.e("TAG","Security Exception")
        }
    }
    private fun moveCamera(latLng:com.google.android.gms.maps.model.LatLng,zoom:Float){
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom))
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val firebaseService = FirebaseService()
        mView = inflater.inflate(R.layout.fragment_home, container, false)
        mMainActivity = activity as MainActivity
        viewpager=mView.findViewById(R.id.viewpager)
        Log.i("test","Voo rooif dm")
//        val circleindicator:CircleIndicator=findViewById(R.id.circle_indicator)
        val photoAdapter: PhotoAdapter = PhotoAdapter(mMainActivity,getListPhoto())
        viewpager.adapter=photoAdapter
//        circleindicator.setViewPager(viewpager)
//        photoAdapter.registerDataSetObserver(circleindicator.dataSetObserver)

        val scrollView=mView.findViewById<ScrollView>(R.id.scrollview)
        scrollView.isSmoothScrollingEnabled = true
        scrollView.setOnTouchListener(TranslateAnimationUtil(mMainActivity, mMainActivity.bottomNavigation))

        val rcv_cate = mView.findViewById<RecyclerView>(R.id.rcv_categories)

        rcv_cate.isNestedScrollingEnabled = false

        val linearLayoutManager:LinearLayoutManager= LinearLayoutManager(mMainActivity,RecyclerView.VERTICAL,false)
        rcv_cate.layoutManager = linearLayoutManager
        firebaseService.getCategories {
            val categoryAdapter = CategoryAdapter(mMainActivity, it)
            rcv_cate.adapter = categoryAdapter
        }
        val rcv_catelist = mView.findViewById<RecyclerView>(R.id.rcv_catelist)
        val linearLayoutManager2:LinearLayoutManager= LinearLayoutManager(mMainActivity,RecyclerView.HORIZONTAL,false)
        rcv_catelist.layoutManager=linearLayoutManager2


        val allCategory: ArrayList<category1> = ArrayList()
        firebaseService.getCategories{ arrayListStopPointInfo->
            arrayListStopPointInfo

            val listCategoryApdapter = ListCategoryApdapter(mMainActivity,arrayListStopPointInfo)
            rcv_catelist.adapter = listCategoryApdapter
            listCategoryApdapter.onItemClick={
                val intent = Intent(mMainActivity, ListCakeAtivity::class.java)
                val gson = Gson()
                val json = gson.toJson(it)
                intent.putExtra("json_name_category",json)
                startActivity(intent)
            }
        }

        fun autoSlideImage(viewpager:ViewPager){
//            if(getListPhoto()==null || getListPhoto().isEmpty() || viewpager==null){
//                return
//            }
            if (timer==null){
                timer = Timer()
            }
            timer!!.schedule(object : TimerTask(){
                override fun run() {
                    Handler(Looper.getMainLooper()).post {
                        var currentItem: Int = viewpager.currentItem
                        var totalItem: Int = getListPhoto().size - 1
                        if (currentItem < totalItem) {
                            currentItem++
                            viewpager.currentItem = currentItem
                        } else {
                            viewpager.currentItem = 0
                        }
                    }
                }
            },500,3000 )
        }
        autoSlideImage(viewpager)


        var listReview = arrayListOf<Review>(
            Review("dtdat.20it1@vku.udn.vn","Duong Tuan Dat","user1","I like this app very much. It's so comfortable for me and my friends. We like it's cake and it's theme")
            , Review("dtdat.20it1@vku.udn.vn","Duong Tuan Dat","user1","I like this app very much. It's so comfortable for me and my friends. We like it's cake and it's theme")
            , Review("dtdat.20it1@vku.udn.vn","Duong Tuan Dat","user1","I like this app very much. It's so comfortable for me and my friends. We like it's cake and it's theme")
        )


        mViewPager1 = mView.findViewById(R.id.viewpager2)

        val reviewAdapter = ReviewAdapter(mMainActivity,listReview)
        mViewPager1.adapter = reviewAdapter


        mapView=mView.findViewById(R.id.map1)
        var mapViewBundle:Bundle?=null
        autoSlideImage(mViewPager1)
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)
        return mView
    }

    override fun onDestroy() {
        super.onDestroy()
        if(timer !=null){
            timer!!.cancel()
            timer==null
        }
    }

    private fun getListPhoto(): ArrayList<Photo> {
        var listPhoto= arrayListOf<Photo>(
            Photo(R.drawable.slide1,"Bakery Favourite",
                "Add a little sweetness to any day with comforting \nand delicious baked goods straight from the bakery."),
            Photo(R.drawable.slide3,"Chocolates & Sweets",
                "This is what heaven tastes like—rich, chocolaty confections and \n" +
                        "tantalizing treats with delicious perfection in every bite."),
            Photo(R.drawable.slide2, "The Perfect Gifts for \nAny Occasion",
                "From birthday parties to holiday gatherings and everything in \n between, discover heartfelt gifts to make the day amazing."),
        )
        return listPhoto
    }

    override fun onLocationChanged(p0: Location) {

    }

    override fun onCameraMove() {

    }

    override fun onCameraMoveStarted(p0: Int) {

    }

    override fun onCameraIdle() {

    }


}