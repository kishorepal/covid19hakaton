package com.hackathon.covid.client.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import com.hackathon.covid.client.data_model.CheckListModel
import com.hackathon.covid.client.data_model.EnvironmentModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class CheckListDataRepository (private val checkListDataDao : CheckListDataInterface) {

    private val TAG = javaClass.simpleName

    private lateinit var checkPointInfoData : LiveData<List<CheckListModel>>
    private lateinit var checkInInfoData : LiveData<List<CheckListModel>>
    private lateinit var checkOutInfoData : LiveData<List<CheckListModel>>

//    fun getCheckListData() : LiveData<List<CheckListModel>> {
//        return checkListDataDao.getCheckListData()
//
//    }

    fun getCheckPointInfo () : LiveData<List<CheckListModel>> {
        checkPointInfoData = checkListDataDao.getCheckPointInfo()
        return checkPointInfoData
    }


//    fun getCheckInInfo(checkPoint : String) :LiveData<List<CheckListModel>> {
//        checkInInfoData = checkListDataDao.getCheckInInfo(checkPoint)
//        return checkInInfoData
//    }

//    fun getCheckOutInfo(checkPoint : String) :LiveData<List<CheckListModel>> {
//        checkOutInfoData = checkListDataDao.getCheckOutInfo(checkPoint)
//        return checkOutInfoData
//    }


    fun insertItem(item : CheckListModel) {
        checkListDataDao.insert(item)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onComplete = { Log.d(TAG, "[insert] >> onComplete")},
                        onError = { Log.e(TAG, "[insert] >> onError")}
                )

    }

}