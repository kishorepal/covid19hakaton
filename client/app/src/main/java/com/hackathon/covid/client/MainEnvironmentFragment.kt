package com.hackathon.covid.client

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hackathon.covid.client.common.UnitConversion
import com.hackathon.covid.client.databinding.FragmentMainEnvironmentBinding
import com.hackathon.covid.client.view_models.MainEnvironmentViewModel
import com.hackathon.covid.client.view_models.ViewModelFactory

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
    private val TAG = javaClass.simpleName


    private var param1: String? = null
    private var param2: String? = null

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
        addObservers()
        return binding.root;
    }


    private fun addObservers() {

        viewModel.environmentData.observe(this, Observer {
            Log.d(TAG, "[addObservers] >> environmentData : $it")
            // todo : update the view with this value
            binding.appStatus.text = getString(R.string.enable_status)
            binding.humidity.text = UnitConversion.getHumidityWithUnit(it.humidity)
            binding.infectedContacts.text = "${it.infectedContacts}"
            binding.riskFactor.text = it.riskFactor
            binding.roomTemp.text = UnitConversion.getTempWithUnit(isCelsius = true, temperature = it.roomTemp)
            binding.shortDescription.text = it.shortDescription
        })

        viewModel.environmentAllData.observe(this, Observer {
            Log.d(TAG, "[addObservers] >> environmentAllData : $it")
            //  testing purpose, later can be elaborate to graph for each datum based on the date
        })

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