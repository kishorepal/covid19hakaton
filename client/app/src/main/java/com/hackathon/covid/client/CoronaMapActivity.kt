package com.hackathon.covid.client

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hackathon.covid.client.databinding.ActivityCoronaMapBinding.inflate
import com.hackathon.covid.client.databinding.ActivityMainBinding
import com.hackathon.covid.client.services.GeofenceBroadcastReceiver
import kotlinx.android.synthetic.main.activity_corona_map.*


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
            slideDownAddressFragment()
            removeList = listOf("Clicked")
            for (item in removeList) {
                removeMarkerAndCircle(item)
            }
        }
                map.setOnMarkerClickListener {
                    slideUpAddressFragment()
            true
        }

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
        var fab = findViewById<FloatingActionButton>(R.id.locateFab)
        fab.setOnClickListener { addLocationListener() }
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
        // todo : Google places api for search automation should cost money
        Places.initialize(applicationContext, getString(R.string.google_places_api_key));

        val autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
            }

            override fun onError(status: Status) {
//                if (status.statusCode != Status.RESULT_CANCELED.statusCode)
//                    throw Exception("Autocomplete error occurred: $status")
            }
        })
    }

    private fun slideDownAddressFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_out_down, R.anim.slide_in_down)
        transaction.addToBackStack(null)
        transaction.replace(R.id.address_fragment, AddressFragment());
        transaction.commit()
    }

    private fun slideUpAddressFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
        transaction.addToBackStack(null)
        transaction.replace(R.id.address_fragment, AddressFragment());
        transaction.commit()

    }


}