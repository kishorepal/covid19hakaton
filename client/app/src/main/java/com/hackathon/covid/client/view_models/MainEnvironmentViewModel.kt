package com.hackathon.covid.client.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hackathon.covid.client.data_model.EnvironmentModel

class MainEnvironmentViewModel(context: Context) : ViewModel() {

    private val TAG = javaClass.simpleName

    private var repository: MainEnvironmentRepository
    var environmentData : LiveData<EnvironmentModel>
    var environmentAllData : LiveData<List<EnvironmentModel>>


    init {

        val mainEnvironmentDaoInterface = MainDatabase.getDatabase(context).mainEnvironmentDao()
        repository = MainEnvironmentRepository(mainEnvironmentDaoInterface)
        environmentData = repository.environmentData
        environmentAllData = repository.environmentAllData



    }



}