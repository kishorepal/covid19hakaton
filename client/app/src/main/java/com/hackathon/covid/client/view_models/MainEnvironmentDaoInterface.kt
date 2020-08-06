package com.hackathon.covid.client.view_models

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hackathon.covid.client.data_model.EnvironmentModel
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface MainEnvironmentDaoInterface {

    @Query("SELECT * FROM environment_value_table ORDER BY timeStamp DESC limit 1")
    fun getLastEnvironmentData() : LiveData<EnvironmentModel>

    @Query("SELECT * FROM environment_value_table")
    fun getAllEnvironmentData() : LiveData<List<EnvironmentModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item : EnvironmentModel) : Completable

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insertAll(item : List<EnvironmentModel>) : Completable

    @Delete
    fun delete(item: EnvironmentModel) : Completable


}