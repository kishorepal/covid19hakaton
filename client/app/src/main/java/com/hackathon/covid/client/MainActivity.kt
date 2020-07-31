package com.hackathon.covid.client

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hackathon.covid.client.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

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
    }



    private fun initView() {
        supportActionBar?.title = getString(R.string.app_name)
        binding.bottomNavView.apply {
            setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
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

}