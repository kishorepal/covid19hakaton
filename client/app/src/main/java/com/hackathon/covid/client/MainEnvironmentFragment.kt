package com.hackathon.covid.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        return binding.root;
    }


    private fun initView(status: String) {
        this.status = status
        binding.btnEnvironmentEnable.text = status
        val buttonStatus: String = binding.btnEnvironmentEnable.text.toString()
        val enable = "Enable"

        // Get all data
        binding.appStatus.text = viewModel.getAppStatus(status).toString()
        binding.humidity.text = viewModel.getRoomHumidity().toString()
        binding.infectedContacts.text = viewModel.getInfectedContacts().toString()
        binding.riskFactor.text = viewModel.getRiskFactor().toString()
        binding.roomTemp.text = viewModel.getRoomTemp().toString()
        binding.shortDescription.text = viewModel.getShortDescription().toString()

        // Set visibility to display data
        if (status != null && buttonStatus == enable) {
            binding.appStatus.text = "Stop"
            binding.humidity.visibility = View.GONE
            binding.infectedContacts.visibility = View.GONE
            binding.riskFactor.visibility = View.GONE
            binding.roomTemp.visibility = View.GONE
            binding.shortDescription.visibility = View.GONE
            Toast.makeText(context, "Button Disabled", Toast.LENGTH_SHORT).show()
        } else {
            binding.appStatus.text = "Running"
            binding.humidity.visibility = View.VISIBLE
            binding.infectedContacts.visibility = View.VISIBLE
            binding.riskFactor.visibility = View.VISIBLE
            binding.roomTemp.visibility = View.VISIBLE
            binding.shortDescription.visibility = View.VISIBLE
            Toast.makeText(context, "Button Enabled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun enableButton() {
        binding.btnEnvironmentEnable.setOnClickListener {
            val buttonStatus: String = binding.btnEnvironmentEnable.text.toString()
            val disable = "Disable"
            val enable = "Enable"
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