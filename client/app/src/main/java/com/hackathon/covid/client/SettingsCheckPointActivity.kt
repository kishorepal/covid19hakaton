package com.hackathon.covid.client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.covid.client.adapters.CheckListRecyclerViewAdapter
import com.hackathon.covid.client.databinding.ActivityCheckPointBinding
import com.hackathon.covid.client.view_models.CheckListViewModel
import com.hackathon.covid.client.view_models.ViewModelFactory

class SettingsCheckPointActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckPointBinding
    private lateinit var checkListAdapter: CheckListRecyclerViewAdapter

    private val viewModelFactory by lazy { ViewModelFactory(this) }

    private val viewModel : CheckListViewModel by lazy {
        ViewModelProviders.of(this@SettingsCheckPointActivity, viewModelFactory)[CheckListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckPointBinding.inflate(LayoutInflater.from(this))
        val view = binding.root
        setContentView(view)

        initView()
        addObservers()
    }

    private fun initView() {
        checkListAdapter = CheckListRecyclerViewAdapter()
        binding.rcvCheckPoint.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = checkListAdapter
        }
    }

    private fun addObservers() {
        viewModel.checkPointInfoData.observe(this, Observer {
            checkListAdapter.updateList(it)
            if (it.isNotEmpty())
                binding.rcvCheckPoint.smoothScrollToPosition(it.lastIndex)
        })
    }

}