package com.hackathon.covid.client.view_models

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hackathon.covid.client.data_model.CheckListModel
import io.reactivex.Completable

@Dao
interface CheckListDataInterface {

    @Query("SELECT * FROM user_data_table")
    fun getCheckPointInfo() : LiveData<List<CheckListModel>>

//    @Query("SELECT checkInInfo FROM user_data_table WHERE checkPointInfo = :checkPoint ")
//    fun getCheckInInfo(checkPoint : String) : LiveData<List<CheckListModel>>
//
//    @Query("SELECT checkOuInfo FROM user_data_table WHERE checkPointInfo = :checkPoint ")
//    fun getCheckOutInfo(checkPoint : String) : LiveData<List<CheckListModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item : CheckListModel) : Completable

    @Delete
    fun delete(item: CheckListModel) : Completable

}