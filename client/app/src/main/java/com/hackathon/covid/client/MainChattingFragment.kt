package com.hackathon.covid.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.covid.client.adapters.ChatRecyclerViewAdapter
import com.hackathon.covid.client.databinding.ChattingFragmentBinding
import com.hackathon.covid.client.view_models.MainChatViewModel
import com.hackathon.covid.client.view_models.ViewModelFactory

class MainChattingFragment : Fragment() {


    private var chattingFragmentBinding : ChattingFragmentBinding? = null
    // this property only valid between onCreateView and onDestroyView
    private val binding get() = chattingFragmentBinding!!

    private val viewModelFactory by lazy {ViewModelFactory(activity!!.applicationContext)}
    private val viewModel : MainChatViewModel by lazy {
        ViewModelProviders.of(this@MainChattingFragment, viewModelFactory)[MainChatViewModel::class.java]
    }


    private lateinit var listAdapter: ChatRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        chattingFragmentBinding = ChattingFragmentBinding.inflate(inflater, container, false)
        initView()
        addObservers()
        return binding.root;
    }

    private fun initView() {

        listAdapter = ChatRecyclerViewAdapter()
        binding.rvChat.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = listAdapter
        }
        binding.btnSend.setOnClickListener {
            sendMessage(binding.etInput.text.toString())
            binding.etInput.setText("")
        }
    }

    private fun addObservers() {

        viewModel.chatList.observe(this, Observer {
            listAdapter.updateList(it)
            if (it.isNotEmpty())
                binding.rvChat.smoothScrollToPosition(it.lastIndex)
        })
    }

    override fun onResume() {
        super.onResume()
        // sendMessage("Corona") // test purpose
    }

    private fun sendMessage(message : String) {
        if (message.isNotEmpty()) {
            viewModel.sendMessage(message)
        }
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