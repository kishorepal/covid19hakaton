package com.hackathon.covid.client.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.hackathon.covid.client.data_model.EnvironmentModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainEnvironmentRepository(val mainEnvironmentDaoInterface: MainEnvironmentDaoInterface) {


    private val TAG = javaClass.simpleName
    // todo : needs to set up with linking with Firebase
    private val myRef = Firebase.database.getReference("room_environment")
    var environmentData : LiveData<EnvironmentModel>
    var environmentAllData : LiveData<List<EnvironmentModel>>  // testing purpose


    init {
        readDbFromFirebase()
        environmentData = mainEnvironmentDaoInterface.getLastEnvironmentData()
        environmentAllData = mainEnvironmentDaoInterface.getAllEnvironmentData()
    }

    private fun readDbFromFirebase(){
        myRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "[readDbFromFirebase] >> onCancelled")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "[readDbFromFirebase] >> onDataChanged")

                val itemList : List<HashMap<String,String>> = snapshot.value as List<HashMap<String,String>>
                for (item in itemList) {
                    Log.d(TAG, "[onDataChange] >> item : $item")
                    insertItem(
                        EnvironmentModel(
                            shortDescription = item["short_description"] as String,
                            timeStamp = item["time_stamp"] as Long,
                            roomTemp = item["room_temp"] as Long,
                            humidity = item["humidity"] as Long,
                            riskFactor = item["risk_factor"] as String,
                            infectedContacts = item["infected_contact"] as Long
                    ))
                }


            }

        })
    }


    private fun insertItem(item : EnvironmentModel) {
        mainEnvironmentDaoInterface.insert(item)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = {Log.d(TAG, "[insertItem] >> onComplete")},
                onError = {Log.e(TAG, "[insertItem] >> onError")}
            )
    }
}
