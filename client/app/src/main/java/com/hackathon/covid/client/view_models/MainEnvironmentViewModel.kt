package com.hackathon.covid.client.view_models

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessagesClient
import com.hackathon.covid.client.data_model.EnvironmentModel


class MainEnvironmentViewModel(context: Context) : ViewModel() {

    private val TAG = javaClass.simpleName

    private var mMessageClient : MessagesClient
    private var randNo = 0

    private var repository: MainEnvironmentRepository
    var environmentData : LiveData<EnvironmentModel>
    var environmentAllData : LiveData<List<EnvironmentModel>>


    init {

        val mainEnvironmentDaoInterface = MainDatabase.getDatabase(context).mainEnvironmentDao()
        repository = MainEnvironmentRepository(mainEnvironmentDaoInterface)
        environmentData = repository.environmentData
        environmentAllData = repository.environmentAllData

        mMessageClient = Nearby.getMessagesClient(context)


    }

}