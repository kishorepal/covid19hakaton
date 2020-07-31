package com.hackathon.covid.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hackathon.covid.client.databinding.FragmentMainEnvironmentBinding
import com.hackathon.covid.client.view_models.MainChatViewModel
import com.hackathon.covid.client.view_models.MainEnvironmentViewModel
import com.hackathon.covid.client.view_models.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_main_environment.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainEnvironmentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainEnvironmentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var status: String? = null

    private var environmentBinding: FragmentMainEnvironmentBinding? = null
    private val binding get() = environmentBinding!!

    private val viewModelFactory by lazy { ViewModelFactory(activity!!.applicationContext) }
    private val viewModel: MainEnvironmentViewModel by lazy {
        ViewModelProviders.of(this@MainEnvironmentFragment, viewModelFactory)[MainEnvironmentViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        environmentBinding = FragmentMainEnvironmentBinding.inflate(inflater, container, false)
        enableButton()
        status?.let { initView(it) }
        updateButton()
        return binding.root;
    }


    private fun initView(status: String) {
        this.status = status
        binding.btnEnvironmentEnable.text = status
        val buttonStatus: String = binding.btnEnvironmentEnable.text.toString()
        val enable = getString(R.string.enable_status)
        if (status != null && buttonStatus == enable) {
            binding.appStatus.text = getString(R.string.enable_status)
            binding.humidity.text = ""
            binding.infectedContacts.text = ""
            binding.riskFactor.text = ""
            binding.roomTemp.text = ""
            binding.shortDescription.text = ""
            Toast.makeText(context, "Button Disabled", Toast.LENGTH_SHORT).show()
        } else {
            addObservers()
            Toast.makeText(context, "Button Enabled", Toast.LENGTH_SHORT).show()
        }
    }


    private fun addObservers() {
        viewModel.roomTempLiveData.observe(this, Observer {
            binding.roomTemp.text = it.toString()
        })

        viewModel.humidityLiveData.observe(this, Observer {
            binding.humidity.text = it.toString()
        })

        viewModel.infectedContactsLiveData.observe(this, Observer {
            binding.infectedContacts.text = it.toString()
        })

        viewModel.shortDescriptionLiveData.observe(this, Observer {
            binding.shortDescription.text = it.toString()
        })

        viewModel.riskFactorLiveData.observe(this, Observer {
            binding.riskFactor.text = it.toString()
        })

        viewModel.appStatusLiveData.observe(this, Observer {
            binding.appStatus.text = it.toString()
        })
    }

    private fun enableButton() {
        binding.btnEnvironmentEnable.setOnClickListener {
            val buttonStatus: String = binding.btnEnvironmentEnable.text.toString()
            val disable = getString(R.string.disable_status)
            val enable = getString(R.string.disable_status)
            if (buttonStatus == enable) {
                binding.btnEnvironmentEnable.text = disable
                status = disable
            } else {
                binding.btnEnvironmentEnable.text = enable
                status = enable
            }
            initView(status!!)
        }
    }

    private fun updateButton() {
        binding.btnRefresh.setOnClickListener {
            if (binding.btnEnvironmentEnable.text.toString() == getString(R.string.disable_status)) {
                addObservers()
            } else {
                Toast.makeText(context, "Please click Enable button first ", Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainEnvironmentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = MainEnvironmentFragment()
    }
}