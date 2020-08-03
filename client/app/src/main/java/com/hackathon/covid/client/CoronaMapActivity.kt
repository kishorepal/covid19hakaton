package com.hackathon.covid.client

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.hackathon.covid.client.services.GeofenceBroadcastReceiver


private const val MY_PERMISSIONS_REQ_ACCESS_FINE_LOCATION = 100
private const val MY_PERMISSIONS_REQ_ACCESS_BACKGROUND_LOCATION = 101


class CoronaMapActivity : AppCompatActivity(), OnMapReadyCallback,GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: MyLocationCallBack
    private lateinit var locationRequest: LocationRequest

    private val geofencingClient: GeofencingClient by lazy {
        LocationServices.getGeofencingClient(this)
    }

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    val geofenceList: MutableList<Geofence> by lazy {
        mutableListOf(
                //check point list by geofencing
                getGeofence("Check point 1", Pair(getCheckPoint().first, getCheckPoint().second))
//                getGeofence("Check point 2", Pair(35.6804, 137.7690)),
//                getGeofence("Check point 3", Pair(35.6804, 135.7690)),
//                getGeofence("Check point 4", Pair(35.6804, 133.7690)),
//                getGeofence("Check point 5", Pair(35.6804, 131.7690))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_corona_map)
        checkPermission()

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationInit()
        addLocationListener()
        checkPermission()
        addGeofences()
        locateButton()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        drawGeofence()
    }

    override fun onMarkerClick(p0: Marker?) = false

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQ_ACCESS_FINE_LOCATION,
            MY_PERMISSIONS_REQ_ACCESS_BACKGROUND_LOCATION -> {
                grantResults.apply {
                    if (this.isNotEmpty()) {
                        this.forEach {
                            if (it != PackageManager.PERMISSION_GRANTED) {
                                checkPermission()
                                return
                            }
                        }
                    } else {
                        checkPermission()
                    }
                }
            }
        }
    }

    override fun onMapClick(p0: LatLng?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        geofencingClient?.removeGeofences(geofencePendingIntent)?.run {
            addOnSuccessListener {
                Toast.makeText(this@CoronaMapActivity, "remove Success", Toast.LENGTH_SHORT).show()
            }
            addOnFailureListener {
                Toast.makeText(this@CoronaMapActivity, "remove Fail", Toast.LENGTH_SHORT).show()
            }
        }
        super.onDestroy()
    }

    private fun drawGeofence () {
        val circleOptions = CircleOptions()
                        .center(LatLng(getCheckPoint().first, getCheckPoint().second))
                        .strokeColor(Color.argb(50,70,70,70))
                        .fillColor(Color.argb(100,150,150,150))
                        .radius(500.0)
                map.addCircle(circleOptions)
    }

    // Get checkPoint location by GPS
    private fun getCheckPoint(): Pair<Double, Double> {
        // todo : Test data
        val latitude = 35.7839
        val longitude = 139.6818
        return Pair(latitude,longitude)
    }

    // Check permission for access device location
    private fun checkPermission() {
        val permissionAccessFineLocationApproved = ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

        if (permissionAccessFineLocationApproved) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val backgroundLocationPermissionApproved = ActivityCompat
                        .checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED

                if (!backgroundLocationPermissionApproved) {
                    ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                            MY_PERMISSIONS_REQ_ACCESS_BACKGROUND_LOCATION
                    )
                }
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQ_ACCESS_FINE_LOCATION
            )
        }
    }



    private fun getGeofence(reqId: String, geo: Pair<Double, Double>, radius: Float = 500.0f): Geofence {
        return Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(reqId)
                // Set the circular region of this geofence.
                .setCircularRegion(geo.first, geo.second, radius)
                // Set the expiration duration of the geofence. This geofence gets automatically
                // removed after this period of time.
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setLoiteringDelay(100)
                // Set the transition types of interest. Alerts are only generated for these
                // transition.
                .setTransitionTypes(
                        Geofence.GEOFENCE_TRANSITION_ENTER
                                or Geofence.GEOFENCE_TRANSITION_EXIT
                                or Geofence.GEOFENCE_TRANSITION_DWELL
                                )
                .build()

    }

    private fun getGeofencingRequest(list: List<Geofence>): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            // Start to follow from Enter to region
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            // Add Geofence list
            addGeofences(list)
        }.build()
    }

    private fun addGeofences() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        geofencingClient.addGeofences(getGeofencingRequest(geofenceList), geofencePendingIntent).run {
            addOnSuccessListener {
                Toast.makeText(this@CoronaMapActivity, "add Success", Toast.LENGTH_LONG).show()
            }
            addOnFailureListener {
                Toast.makeText(this@CoronaMapActivity, "add Fail", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun locationInit() {
        locationCallback = MyLocationCallBack()
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 1000
        locationRequest.numUpdates = 1
        locationRequest.fastestInterval = 5000
    }

    private fun locateButton() {
        val imgMyLocation = findViewById<ImageView>(R.id.imgMyLocation)
        imgMyLocation.setOnClickListener {
            addLocationListener()
        }
    }

    inner class MyLocationCallBack : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            val location = locationResult?.lastLocation
            location?.run {
                val latLng = LatLng(latitude, longitude)
                // Move camera to user location
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                // Add marker
                map.addMarker(MarkerOptions().position(latLng).title("Your location"))
            }
        }
    }

    private fun addLocationListener() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }



}