package com.hackathon.covid.client

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.covid.client.adapters.ChatRecyclerViewAdapter
import com.hackathon.covid.client.databinding.ChattingFragmentBinding

class ChattingFragment : Fragment() {

    private lateinit var viewModel: ChattingViewModel
    private var chattingFragmentBinding : ChattingFragmentBinding? = null
    // this property only valid between onCreateView and onDestroyView
    private val binding get() = chattingFragmentBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        chattingFragmentBinding = ChattingFragmentBinding.inflate(inflater, container, false)
        initView()
        return binding.root;
    }

    private fun initView() {
        binding.rvChat.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = ChatRecyclerViewAdapter()
        }
    }

    private fun addObservers() {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChattingViewModel::class.java)

    }

    fun newInstance() : ChattingFragment{
        return ChattingFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        chattingFragmentBinding = null // release
    }
}