package com.hackathon.covid.client

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hackathon.covid.client.databinding.FragmentMainSettingBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


private var settingBinding: FragmentMainSettingBinding? = null
private val binding get() = settingBinding!!

/**
 * A simple [Fragment] subclass.
 * Use the [MainSettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainSettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        settingBinding = FragmentMainSettingBinding.inflate(inflater, container, false)
        coronaMapButton()
        checkPointButton()
        checkListButton()
        return binding.root
    }


    private fun coronaMapButton() {
        binding.optCoronaMap.setOnClickListener {
            // move to corona map page
            activity?.let{
                val intent = Intent (it, CoronaMapActivity::class.java)
                it.startActivity(intent)
            }
        }
    }

    private fun checkPointButton() {
        binding.optCheckPoint.setOnClickListener {
            // move to corona map page
            activity?.let{
                val intent = Intent (it, SettingsCheckPointActivity::class.java)
                it.startActivity(intent)
            }
        }
    }

    private fun checkListButton() {
        binding.optCheckList.setOnClickListener {
            // move to corona map page
            activity?.let{
                val intent = Intent (it, SettingsCheckListActivity::class.java)
                it.startActivity(intent)
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
         * @return A new instance of fragment MainSettingFragment.
         */
        @JvmStatic
        fun newInstance() = MainSettingFragment()
    }
}