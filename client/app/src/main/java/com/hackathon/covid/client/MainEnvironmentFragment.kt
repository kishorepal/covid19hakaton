package com.hackathon.covid.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hackathon.covid.client.databinding.FragmentMainEnvironmentBinding
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

    private var environmentBinding: FragmentMainEnvironmentBinding? = null
    private val binding get() = environmentBinding!!


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
        return binding.root;
    }


    private fun initView() {
        val buttonStatus: String = binding.btnEnvironmentEnable.text.toString()
        val enable = "Enable"
        if (buttonStatus == enable) {
            binding.appStatus.text = "Stop"
            binding.humidity.text = "Stop"
            binding.infectedContacts.text = "Stop"
            binding.riskFactor.text = "Stop"
            binding.roomTemp.text = "Stop"
            binding.shortDescription.text = "Stop"

            Toast.makeText(context, "Button Disabled", Toast.LENGTH_SHORT).show()
        } else {
            // Display all data
            binding.appStatus.text = "Running"
            binding.humidity.text = "Running"
            binding.infectedContacts.text = "Running"
            binding.riskFactor.text = "Running"
            binding.roomTemp.text = "Running"
            binding.shortDescription.text = "Running"
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
                initView()
            } else {
                binding.btnEnvironmentEnable.text = enable
                initView()
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