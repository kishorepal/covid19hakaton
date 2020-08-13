package com.hackathon.covid.client.view_models

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory (private val context : Context) :ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainEnvironmentViewModel::class.java)) {
            return MainEnvironmentViewModel(context) as T
        }
        else if ( modelClass.isAssignableFrom(MainChatViewModel::class.java)) {
            return MainChatViewModel(context) as T
        }
        else if (modelClass.isAssignableFrom(CheckListViewModel::class.java)) {
            return CheckListViewModel(context) as T
        }
        // todo : settings and health needs to be done\

        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}