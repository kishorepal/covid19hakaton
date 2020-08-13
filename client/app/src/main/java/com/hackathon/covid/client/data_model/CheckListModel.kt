package com.hackathon.covid.client.data_model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "user_data_table")
data class CheckListModel (

        @ColumnInfo(name = "checkPointName") var checkPointName : String?,
        @ColumnInfo(name = "checkPointLatLng") var checkPointLatLng: String?,
        @ColumnInfo(name = "checkInInfo") var checkInInfo : String?,
        @ColumnInfo(name = "checkOuInfo") var checkOuInfo : String?

) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int = 0
}