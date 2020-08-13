package com.hackathon.covid.client

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hackathon.covid.client.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){



    private val MY_PERMISSIONS_REQ_ACCESS_FINE_LOCATION = 100
    private val MY_PERMISSIONS_REQ_ACCESS_BACKGROUND_LOCATION = 101

    private lateinit var mMessage : Message

    private val TAG = javaClass.simpleName

    private lateinit var binding: ActivityMainBinding

    private val mainEnvironmentFragment : MainEnvironmentFragment by lazy { MainEnvironmentFragment.newInstance() }
    private val mainChattingFragment : MainChattingFragment by lazy { MainChattingFragment.newInstance() }
    private val mainHealthFragment : MainHealthFragment by lazy { MainHealthFragment.newInstance() }
    private val mainSettingFragment : MainSettingFragment by lazy { MainSettingFragment.newInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        val view = binding.root
        setContentView(view)

        if (supportFragmentManager.findFragmentById(R.id.fcv_container) != null) {
            Log.d(TAG, "[onCreate] >> activity creating")
        }
        else {
            initView()
            switchFragment(mainEnvironmentFragment)
            Log.d(TAG, "[onCreate] >> activity initially created")

        }

        checkPermission()
        mMessage = Message("Hello World".toByteArray())
    }



    private fun initView() {
        supportActionBar?.title = getString(R.string.app_name)
        binding.bottomNavView.apply {
            setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        }
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


    private fun switchFragment(fragment : Fragment) {
        Log.d(TAG, "[switchFragment] >> IN")
        supportFragmentManager.beginTransaction().replace(R.id.fcv_container, fragment).commit()
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            Log.d(TAG, "[mOnNavigationItemSelectedListener] >> itemId : ${item.title}")
        when (item.itemId) {
            R.id.menu_item_environment -> {
                Log.d(TAG, "[mOnNavigationItemSelectedListener] >> menu_item_environment")
                switchFragment(mainEnvironmentFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_item_chat -> {
                Log.d(TAG, "[mOnNavigationItemSelectedListener] >> menu_item_chat")
                switchFragment(mainChattingFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_item_health -> {
                Log.d(TAG, "[mOnNavigationItemSelectedListener] >> menu_item_health")
                switchFragment(mainHealthFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_item_settings -> {
                Log.d(TAG, "[mOnNavigationItemSelectedListener] >> menu_item_settings")
                switchFragment(mainSettingFragment)
                return@OnNavigationItemSelectedListener true
            }
            else -> {
                switchFragment(mainEnvironmentFragment)
                return@OnNavigationItemSelectedListener false
            }
        }

    }


    override fun onStart() {
        super.onStart()
        publish()
    }

    override fun onStop() {
        Nearby.getMessagesClient(this).unpublish(mMessage)
        Nearby.getMessagesClient(this).unsubscribe(mMessageListener)
        super.onStop()
    }


    fun publish() {
        Nearby.getMessagesClient(this).publish(mMessage)
        Nearby.getMessagesClient(this).subscribe(mMessageListener)
    }

    fun refresh() {
        mMessage = if (String(mMessage.content) == "Hello World") {
            Message("Hello".toByteArray())
        } else {
            Message("Hello World".toByteArray())
        }
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


    private val mMessageListener = object : MessageListener() {
        override fun onLost(p0: Message?) {
            super.onLost(p0)
            Log.d(TAG, "[onLost] >> ${String(p0!!.content)}")
        }

        override fun onFound(p0: Message?) {
            super.onFound(p0)
            Log.d(TAG, "[onFound] >> ${String(p0!!.content)}")
            Toast.makeText(this@MainActivity, "Message Received!! Token : ${String(p0.content)}", Toast.LENGTH_SHORT).show()
        }
    }

}
