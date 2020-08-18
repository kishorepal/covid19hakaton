package com.hackathon.covid.client.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.hackathon.covid.client.R


class GeofenceBroadcastReceiver : BroadcastReceiver() {
    var mp : MediaPlayer = MediaPlayer()

    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.e("GeofenceBR", errorMessage)
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            val triggeringGeofences = geofencingEvent.triggeringGeofences

            val transitionMsg = when(geofenceTransition) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> "Enter"
                Geofence.GEOFENCE_TRANSITION_EXIT -> "Exit"
                else -> "-"
            }
            triggeringGeofences.forEach {
                Toast.makeText(context, "${it.requestId} - $transitionMsg", Toast.LENGTH_LONG).show()
                setAudioNotification(it.requestId, transitionMsg, context)
            }

        } else {
            Toast.makeText(context, "Unknown", Toast.LENGTH_LONG).show()
        }
    }

    private fun setAudioNotification (requestId: String, transitionMsg: String, context: Context?) {
        when (requestId) {
            "Home" -> {
                mp = if (transitionMsg == "Enter") {
                    MediaPlayer.create(context, R.raw.wash_hands)
                } else {
                    MediaPlayer.create(context, R.raw.wear_mask)
                }
            }
            "High Risk Zone 1" -> {
                mp = if (transitionMsg == "Enter") {
                    MediaPlayer.create(context, R.raw.high_risk_zone_in)
                } else {
                    MediaPlayer.create(context, R.raw.high_risk_zone_out)
                }
            }

            "High Risk Zone 2" -> {
                mp = if (transitionMsg == "Enter") {
                    MediaPlayer.create(context, R.raw.high_risk_zone_in)
                } else {
                    MediaPlayer.create(context, R.raw.high_risk_zone_out)
                }
            }

            "High Risk Zone 3" -> {
                mp = if (transitionMsg == "Enter") {
                    MediaPlayer.create(context, R.raw.high_risk_zone_in)
                } else {
                    MediaPlayer.create(context, R.raw.high_risk_zone_out)
                }
            }
        }
        mp.start()

    }

}