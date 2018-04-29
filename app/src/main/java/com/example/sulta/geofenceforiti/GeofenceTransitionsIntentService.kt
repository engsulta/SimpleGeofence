package com.example.sulta.geofenceforiti

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 *
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class GeofenceTransitionsIntentService : IntentService("GeofenceTransitionsIntentService") {

        // ...
        override fun onHandleIntent(intent: Intent?) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)
            if (geofencingEvent.hasError()) {
                    Log.i("GeofenceTransitionsIS",geofencingEvent.toString())
                return
            }

            // Get the transition type.
            val geofenceTransition = geofencingEvent.geofenceTransition

            // Test that the reported transition was of interest.
            if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ) {
                val audio = getSystemService(Context.AUDIO_SERVICE) as AudioManager
                audio.ringerMode = 1
                // Get the geofences that were triggered. A single event can trigger
                // multiple geofences.
                val triggeringGeofences = geofencingEvent.triggeringGeofences

                // Get the transition details as a String.
//                val geofenceTransitionDetails = getGeofenceTransitionDetails(
//                        this,
//                        geofenceTransition,
//                        triggeringGeofences
//                )

                // Send notification and log the transition details.
               // sendNotification(geofenceTransitionDetails)
                Log.i("GeofenceTransitionsIS", "done")
            } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
                val audio = getSystemService(Context.AUDIO_SERVICE) as AudioManager
                audio.ringerMode = 2
            }
            else {
                // Log the error.
                Log.e("GeofenceTransitionsIS", getString(R.string.geofence_transition_invalid_type,
                        geofenceTransition))
            }
        }
    }

