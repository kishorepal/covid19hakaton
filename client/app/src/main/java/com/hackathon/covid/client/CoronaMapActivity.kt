package com.hackathon.covid.client

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hackathon.covid.client.services.GeofenceBroadcastReceiver


private const val MY_PERMISSIONS_REQ_ACCESS_FINE_LOCATION = 100
private const val MY_PERMISSIONS_REQ_ACCESS_BACKGROUND_LOCATION = 101


class CoronaMapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: MyLocationCallBack
    private lateinit var locationRequest: LocationRequest
    private lateinit var removeList: List<String>
    private lateinit var marker: Marker
    private lateinit var circle: Circle
    private var markerMap = HashMap<String, Marker>()
    private var circleMap = HashMap<String, Circle>()

    private val geofencingClient: GeofencingClient by lazy {
        LocationServices.getGeofencingClient(this)
    }

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_corona_map)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val fab: FloatingActionButton = findViewById<FloatingActionButton>(R.id.addPoint)
        fab.setOnClickListener(View.OnClickListener {

        })

        checkPermission()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val list = checkPointList()
        map = googleMap

        for (item in list) {
            drawGeofence(item.key, item.value)
        }

        map.setOnMapLongClickListener {
            onMapLongClick(it)
        }

        // test remove marker and circle from map
        map.setOnMapClickListener {
            removeList = listOf("Clicked")

            for (item in removeList) {
                removeMarkerAndCircle(item)
            }
        }

        //        map.setOnMarkerClickListener {
//            if (marker.isInfoWindowShown){
//                marker.hideInfoWindow()
//            }else{
//                marker.showInfoWindow()
//            }
//            true
//        }

        locationInit()
        addLocationListener()
        addGeofences()
        locateButton()
        searchMap()
    }

    override fun onMapLongClick(point: LatLng) {
        val titleName = "Clicked"
        var markerOptions = MarkerOptions().position(point).title("Destination")
        if (markerMap[titleName] != null) {
            removeMarkerAndCircle(titleName)
        }
        marker = map.addMarker(markerOptions)
        markerMap[titleName] = marker
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val center: CameraUpdate = CameraUpdateFactory.newLatLng(marker.position)
        map.animateCamera(center)
        return true
    }

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

    override fun onMapClick(p0: LatLng) {
    }

    override fun onDestroy() {
        geofencingClient?.removeGeofences(geofencePendingIntent)?.run {
            addOnSuccessListener {
                Toast.makeText(this@CoronaMapActivity, "remove geofence Success", Toast.LENGTH_SHORT).show()
            }
            addOnFailureListener {
                Toast.makeText(this@CoronaMapActivity, "remove geofence Fail", Toast.LENGTH_SHORT).show()
            }
        }
        super.onDestroy()
    }

    // Draw Geofence range
    private fun drawGeofence(name: String, latLng: LatLng) {
        val circleOptions = CircleOptions()
                .center(latLng)
                .strokeColor(Color.argb(50, 70, 70, 70))
                .fillColor(Color.argb(100, 150, 150, 150))
                .radius(200.0)

        circle = map.addCircle(circleOptions)
        marker = map.addMarker(MarkerOptions().position(latLng).title(name))
        markerMap[name] = marker
        circleMap[name] = circle
    }

    // Get checkpoint
    private fun getCheckPoint(): Pair<Double, Double> {
        val latitude = 35.7839
        val longitude = 139.6818
        return Pair(latitude, longitude)
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

    // Get saved check point by viewModel(get user data from database)
    private fun checkPointList(): Map<String, LatLng> {
        // Dummy data
        return mapOf(("Home" to LatLng(35.7839, 139.6818)), ("Office" to LatLng(35.7851, 139.6890)))
    }

    private fun getGeofenceList(): MutableList<Geofence> {
        val geofenceList = mutableListOf<Geofence>()
        val list = checkPointList()

        for (point in list) {
            geofenceList.add(getGeofence(point.key, Pair(point.value.latitude, point.value.longitude)))
        }
        return geofenceList
    }

    private fun getGeofence(reqId: String, geo: Pair<Double, Double>, radius: Float = 200.0f): Geofence {
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
        geofencingClient.addGeofences(getGeofencingRequest(getGeofenceList()), geofencePendingIntent).run {
            addOnSuccessListener {
                Toast.makeText(this@CoronaMapActivity, "Add geofence success", Toast.LENGTH_LONG).show()
            }
            addOnFailureListener {
                Toast.makeText(this@CoronaMapActivity, "Add geofence fail", Toast.LENGTH_LONG).show()
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
        val imgMyLocation = findViewById<ImageView>(R.id.myPlace)
        imgMyLocation.setOnClickListener {
            addLocationListener()
        }
    }

    inner class MyLocationCallBack : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            val personMarker = resources.getDrawable(R.drawable.person_button, null)
            val location = locationResult?.lastLocation
            location?.run {
                val latLng = LatLng(latitude, longitude)
                // Move camera to user location
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                // Add marker
                map.addMarker(MarkerOptions().position(latLng).title("Your location"))
                        .setIcon(BitmapDescriptorFactory.fromBitmap(personMarker.toBitmap(personMarker.intrinsicWidth, personMarker.intrinsicHeight, null)))
            }
        }
    }

    private fun removeMarkerAndCircle(location: String) {
        markerMap[location]?.remove()
        circleMap[location]?.remove()
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

    private fun searchMap() {
        (fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as? PlaceAutocompleteFragment)?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place?) {

                if (place != null) {
                    map.run {
                        addMarker(MarkerOptions().position(place.latLng).title(place.name as String?))
                        map.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 15.0f))
                    }
                }
                TODO("Not yet implemented")
            }

            override fun onError(place: Status?) {
                Log.d("Error", "Failed")
            }
        })
    }


}