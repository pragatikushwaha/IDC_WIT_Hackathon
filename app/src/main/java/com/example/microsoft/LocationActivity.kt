package com.example.microsoft

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TypefaceSpan
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.microsoft.MainActivity.Companion.GroupID
import com.example.microsoft.room.database
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.activity_location.*
import java.util.*
import kotlin.collections.ArrayList
import androidx.lifecycle.Observer
import com.example.microsoft.MainActivity.Companion.UserName
import com.example.microsoft.room.entites.LocationData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class LocationActivity : AppCompatActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    val PERMISSION_ID = 1010
    companion object
    {
        var User_lat=0.0
        var User_long=0.0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val s = SpannableString("Family Locatios")
        s.setSpan(TypefaceSpan("avenir_ltstd_book.otf"), 0, s.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        supportActionBar!!.title = s

        setContentView(R.layout.activity_location)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        share_location.setOnClickListener {
            Log.d("Debug:",CheckPermission().toString())
            Log.d("Debug:",isLocationEnabled().toString())
            RequestPermission()
            /* fusedLocationProviderClient.lastLocation.addOnSuccessListener{location: Location? ->
                 textView.text = location?.latitude.toString() + "," + location?.longitude.toString()
             }*/
            getLastLocation()
        }
        database(this).getDao().getLocationData(GroupID, UserName)
            .observe(this, Observer { listdata->
                Log.d("L","LocationData Size"+listdata)
                group_recyclerView.also {
                    it.layoutManager = LinearLayoutManager(this)
                    it.setHasFixedSize(true)
                    it.adapter = LocationAdapter(listdata)
                }
            })

    }


    fun getLastLocation(){
        if(CheckPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
                    var location: Location? = task.result
                    if(location == null){
                        NewLocationData()
                    }else{
                        Log.d("Debug:" ,"Your Location:"+ location.longitude)
                        User_lat=location.latitude
                        User_long=location.longitude

                        var address=getCityCountryName(location.latitude,location.longitude)
                        your_location.text = "You Current Location is : Long: "+ location.longitude + " , Lat: " + location.latitude + "\n" +address[0]+" "+address[1]+" "+address[2]+
                                " "+address[3];
                        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
                        val currentDate = sdf.format(Date())
                        Log.d("date"," C DATE is  "+currentDate)
                        CoroutineScope(Dispatchers.IO).launch {

                            val loc_data=LocationData(UserName,
                                GroupID, User_lat, User_long,currentDate,address[0],address[1],address[2],address[3])
                            database(applicationContext).getDao().addLocationData(loc_data)

                        }
                    }
                }
            }else{
                Toast.makeText(this,"Please Turn on Your device Location", Toast.LENGTH_SHORT).show()
            }
        }else{
            RequestPermission()
        }
    }


    fun NewLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )
    }


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            User_lat=lastLocation.latitude
            User_long=lastLocation.longitude
            Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
            var address=getCityCountryName(lastLocation.latitude,lastLocation.longitude)
            your_location.text = "You Last Location is : Long: "+ lastLocation.longitude + " , Lat: " + lastLocation.latitude + "\n" + address[0]+" "+address[1]+" "+address[2]+" "+address[3];
            val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
            Log.d("date"," C DATE is  "+currentDate)
            CoroutineScope(Dispatchers.IO).launch {

                val loc_data=LocationData(UserName,
                    GroupID, User_lat, User_long,currentDate,address[0],address[1],address[2],address[3])
                database(applicationContext).getDao().addLocationData(loc_data)

            }
        }
    }

    private fun CheckPermission():Boolean{
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false

    }

    fun RequestPermission(){
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    fun isLocationEnabled():Boolean{
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug:","You have the Permission")
            }
        }
    }

    private fun getCityCountryName(lat: Double,long: Double):ArrayList<String>{
        var cityName:String = ""
        var countryName = ""
        var subAdminArea="";
        var geoCoder = Geocoder(this, Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,long,3)

        cityName = Adress.get(0).locality
        countryName = Adress.get(0).countryName
        subAdminArea=Adress.get(0).subAdminArea
        var locality=Adress.get(0).locality
        var locale= Adress.get(0).locale
        var adminArea= Adress.get(0).adminArea
        var premises= Adress.get(0).premises
        var sublocality=Adress.get(0).subLocality
        var location = ArrayList<String>();
        Log.d("Debug:","Your City: " + cityName + " ; your Country " + countryName+" AdminArea"+adminArea+" subAdminArea"+subAdminArea
        +" locale"+locale+" premises"+premises+" sublocality"+sublocality+" locality"+locality)
        location.add(cityName)
        location.add(subAdminArea)
        location.add(adminArea)
        location.add(countryName)
        return location
    }
    }


