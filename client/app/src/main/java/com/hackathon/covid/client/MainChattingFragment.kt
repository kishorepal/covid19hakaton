package com.hackathon.covid.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.covid.client.adapters.ChatRecyclerViewAdapter
import com.hackathon.covid.client.databinding.ChattingFragmentBinding

class MainChattingFragment : Fragment() {


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

    }


    override fun onDestroyView() {
        super.onDestroyView()
        chattingFragmentBinding = null // release
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainChattingFragment()

    }
}