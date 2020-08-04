package com.hackathon.covid.client.data_model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "environment_value_table")
data class EnvironmentModel(
    @ColumnInfo(name = "roomTemp") val roomTemp : Long,
    @ColumnInfo(name = "humidity") val humidity : Long,
    @ColumnInfo(name = "riskFactor") val riskFactor : String,
    @ColumnInfo(name = "infectedContacts") val infectedContacts : Long,
    @ColumnInfo(name = "shortDescription") val shortDescription : String,
    @ColumnInfo(name = "timeStamp") val timeStamp : Long
) {

    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id : Int = 0
}