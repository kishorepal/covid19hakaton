package com.hackathon.covid.client

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.hackathon.covid.client.databinding.ActivityCoronaMapBinding
import com.hackathon.covid.client.services.GeofenceBroadcastReceiver
import com.hackathon.covid.client.view_models.CheckListViewModel
import com.hackathon.covid.client.view_models.ViewModelFactory



class CoronaMapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener {

    private lateinit var binding: ActivityCoronaMapBinding
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: MyLocationCallBack
    private lateinit var locationRequest: LocationRequest
    private lateinit var marker: Marker
    private lateinit var circle: Circle
    private var markerMap = HashMap<String, Marker>()
    private var circleMap = HashMap<String, Circle>()
    private var pointListMap = HashMap<String, LatLng>()
    private var lastDestinationPoint = HashMap<String, LatLng>()
    private var address = String()
    private var clickedLocationName = String()
    private var currentPositionName = String ()
    private var currentPosition = 0

    private val geofencingClient: GeofencingClient by lazy {
        LocationServices.getGeofencingClient(this)
    }

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private val viewModelFactory by lazy { ViewModelFactory(this) }
    private val viewModel : CheckListViewModel by lazy {
        ViewModelProviders.of(this@CoronaMapActivity, viewModelFactory)[CheckListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoronaMapBinding.inflate(LayoutInflater.from(this))
        val view = binding.root
        setContentView(view)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.fm_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        pointListMap = checkPointList()
        map = googleMap

        for (item in pointListMap) {
            when (item.key) {
                "High Risk Zone 1" -> {
                    drawGeofence(item.key, item.value, "danger")
                }
                "High Risk Zone 2" -> {
                    drawGeofence(item.key, item.value, "danger")
                }

                "High Risk Zone 3" -> {
                    drawGeofence(item.key, item.value, "danger")
                }
            }
            drawGeofence(item.key, item.value, "safe")
        }

        map.setOnMapLongClickListener {
            onMapLongClick(it)
        }

        // Remove marker and circle from map
        map.setOnMapClickListener {
            binding.llSetPoint.visibility = View.INVISIBLE
            val newPoint = LatLng(String.format("%.4f",it.latitude).toDouble(), String.format("%.4f",it.longitude).toDouble())
            if (!pointListMap.values.contains(newPoint) && !pointListMap.keys.contains(currentPositionName)) {
                removeMarkerAndCircle(currentPositionName, "marker")
                markerMap.remove(currentPositionName)
                lastDestinationPoint.remove(currentPositionName)
            }
        }

        map.setOnMarkerClickListener {
            currentPositionName = it.title
            val latitude = String.format("%.4f", it.position.latitude).toDouble()
            val longitude = String.format("%.4f", it.position.longitude).toDouble()
            lastDestinationPoint[clickedLocationName] = LatLng(latitude, longitude)
            address = "$currentPositionName - >  $latitude / $longitude"
            binding.address.text = address
            binding.llSetPoint.visibility = View.VISIBLE
            binding.tvTitle.text = currentPositionName
            binding.tvLocationAddress.text = "$latitude / $longitude"
            if (LatLng(latitude, longitude) != lastDestinationPoint[currentPositionName]) {
                lastDestinationPoint.remove(currentPositionName)
                lastDestinationPoint[currentPositionName] = LatLng(latitude, longitude)
            }
            true
        }
        locationInit()
        addLocationListener()
        locateButton()
        addGeofences(pointListMap)
        makeCheckPointButton()
        searchMap()
    }

    override fun onMapLongClick(point: LatLng) {
        currentPosition += 1
        currentPositionName = "Destination $currentPosition"
        val previousPosition = currentPosition -1
        val previousPositionName = "Destination $previousPosition"
        val newPoint = LatLng(String.format("%.4f",point.latitude).toDouble(), String.format("%.4f",point.longitude).toDouble())
        lastDestinationPoint[currentPositionName] = newPoint
        var markerOptions = MarkerOptions().position(newPoint).title(currentPositionName)
        marker = map.addMarker(markerOptions)
        markerMap[currentPositionName] = marker
        if (!pointListMap.values.contains(newPoint) && currentPosition - 1 == previousPosition ) {
            removeMarkerAndCircle(previousPositionName, "marker")
            markerMap.remove(previousPositionName)
            lastDestinationPoint.remove(previousPositionName)
        }
        binding.llSetPoint.visibility = View.INVISIBLE
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        return true
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
    private fun drawGeofence(name: String, latLng: LatLng, type: String) {
        val circleOptions = CircleOptions()
        if (type == "safe") {
            circleOptions
                    .center(latLng)
                    .strokeColor(Color.argb(50, 70, 70, 70))
                    .fillColor(Color.argb(100, 150, 150, 150))
                    .radius(200.0)
        } else if (type == "danger") {
            circleOptions
                    .center(latLng)
                    .strokeColor(Color.argb(50, 250, 70, 70))
                    .fillColor(Color.argb(100, 200, 150, 150))
                    .radius(200.0)
        }


        circle = map.addCircle(circleOptions)
        marker = map.addMarker(MarkerOptions().position(latLng).title(name))
        markerMap[name] = marker
        circleMap[name] = circle
    }



    // Get saved check point by viewModel(get user data from database)
    private fun checkPointList(): HashMap<String, LatLng> {
        // Dummy data
        return hashMapOf(("Home" to LatLng(35.7839, 139.6818)), ("High Risk Zone 1" to LatLng(35.7851, 139.6890))
        , ("High Risk Zone 2" to LatLng(35.7891, 139.6880)), ("High Risk Zone 3" to LatLng(35.7881, 139.6800)))
    }

    private fun getGeofenceList(list: HashMap<String, LatLng>): MutableList<Geofence> {
        val geofenceList = mutableListOf<Geofence>()

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

    private fun addGeofences(map: HashMap<String, LatLng>) {
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
        geofencingClient.addGeofences(getGeofencingRequest(getGeofenceList(map)), geofencePendingIntent).run {
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
//        locationRequest.priority = LocationRequest.
        locationRequest.interval = 100
        locationRequest.numUpdates = 1
        locationRequest.fastestInterval = 5000
    }

    private fun locateButton() {
        var fab = binding.locateFab
        fab.setOnClickListener {
            addLocationListener()
        }
    }

    inner class MyLocationCallBack : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            markerMap["Device Location"]?.remove()
            super.onLocationResult(locationResult)
            val personMarker = resources.getDrawable(R.drawable.person_button, null)
            val location = locationResult?.lastLocation
            location?.run {
                val latLng = LatLng(latitude, longitude)
                // Move camera to user location
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                // Add marker
                marker = map.addMarker(MarkerOptions().position(latLng).title("Your Location"))
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(personMarker.toBitmap(personMarker.intrinsicWidth, personMarker.intrinsicHeight, null)))
                markerMap.put("Device Location", marker)
            }
        }
    }

    private fun removeMarkerAndCircle(location: String, type: String) {
        when (type) {
            "marker" -> {
                markerMap[location]?.remove()
            }
            "circle" -> {
                circleMap[location]?.remove()
            }
            "all" -> {
                markerMap[location]?.remove()
                circleMap[location]?.remove()
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

    private fun makeCheckPointButton() {
        binding.btnSetPoint.setOnClickListener {
            val lastLocationLatLng = lastDestinationPoint[currentPositionName]
            val pointList = pointListMap.values
            val lastPointList = lastDestinationPoint.values
            if (pointList.contains(lastLocationLatLng) && lastPointList.contains(lastLocationLatLng)) {
                Toast.makeText(this, "This place is already marked as check point", Toast.LENGTH_SHORT).show()
            } else {
                addGeofences(lastDestinationPoint)
                lastLocationLatLng?.let { it1 -> pointListMap.put(currentPositionName, it1) }
                lastLocationLatLng?.let { it1 -> drawGeofence(currentPositionName, it1, "safe") }
                if (lastLocationLatLng != null) {
                    insertCheckPointInfo(currentPositionName,lastLocationLatLng.toString())
                }
            }
        }
    }

    // Insert Check Point data to database
    private fun insertCheckPointInfo (checkPointName : String, checkPointLatLng: String) {
        viewModel.insertData(checkPointName = checkPointName, checkPointLatLng = checkPointLatLng, checkInInfo = "", checkOutInfo = "")
    }


}