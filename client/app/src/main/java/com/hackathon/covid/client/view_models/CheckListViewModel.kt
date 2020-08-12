package com.hackathon.covid.client.view_models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hackathon.covid.client.data_model.CheckListModel

class CheckListViewModel(context: Context) : ViewModel() {

    private var repository: CheckListDataRepository
    var checkPointInfoData: LiveData<List<CheckListModel>>

    init {
        val checkListDaoInterface = MainChatDatabase.getDatabase(context).checkListDao()
        repository = CheckListDataRepository(checkListDaoInterface)
        checkPointInfoData = repository.getCheckPointInfo()
    }

    fun insertData(checkPointInfo: String?, checkInInfo: String?, checkOutInfo: String?) {
        repository.insertItem(
                CheckListModel(
                        checkPointInfo = checkPointInfo!!,
                        checkInInfo = checkInInfo!!,
                        checkOuInfo = checkOutInfo!!
                ))
    }

//    fun getCheckInInfoData (checkPoint : String) : LiveData<List<CheckListModel>> {
//        return repository.getCheckInInfo(checkPoint)
//    }
//
//    fun getCheckOutInfoData (checkPoint : String) : LiveData<List<CheckListModel>> {
//        return repository.getCheckOutInfo(checkPoint)
//    }

}