package com.example.sulta.geofenceforiti

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), OnCompleteListener<Void> {

    // members
    private lateinit var mGeofence: Geofence
    private var mGeofenceList: ArrayList<Geofence>? = null
    lateinit var geofencingClient: GeofencingClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val TAG = MainActivity::class.java.simpleName
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34

    /**
     * Used when requesting to add or remove geofences.
     */
    private var mGeofencePendingIntent: PendingIntent? = null

    private enum class PendingGeofenceTask {
        ADD, REMOVE, NONE
    }

    private var mGeofencingClient: GeofencingClient? = null

    // Buttons for kicking off the process of adding or removing geofences.
    private var mAddGeofencesButton: Button? = null
    private var mRemoveGeofencesButton: Button? = null

    private var mPendingGeofenceTask = PendingGeofenceTask.NONE
    val BAY_AREA_LANDMARKS = HashMap<String, LatLng>()


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mGeofenceList = ArrayList()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        location_txt.text = "lat:" + location.latitude.toString() + "long:" + location.longitude.toString()
                        BAY_AREA_LANDMARKS.put("ITI", LatLng(location.latitude, location.longitude))

                        mGeofenceList?.add(Geofence.Builder()
                                .setRequestId("key")
                                .setCircularRegion(
                                        BAY_AREA_LANDMARKS.get("ITI")!!.latitude,
                                        BAY_AREA_LANDMARKS.get("ITI")!!.longitude,
                                        500.0f
                                )

                                .setExpirationDuration(Geofence.NEVER_EXPIRE)

                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)

                                .build())


                    }
                }
        mGeofenceList?.add(Geofence.Builder()
                .setRequestId("key")
                .setCircularRegion(
                        30.07012119,
                        31.0203031,
                        500.0f
                )

                .setExpirationDuration(Geofence.NEVER_EXPIRE)

                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)

                .build())




        mGeofencePendingIntent = null


        mGeofencingClient = LocationServices.getGeofencingClient(this)


        mPendingGeofenceTask = PendingGeofenceTask.ADD

    }

    @SuppressLint("MissingPermission")
    public override fun onStart() {
        super.onStart()

        mGeofencingClient?.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())?.addOnCompleteListener(this)

    }

    private fun getGeofencingRequest(): GeofencingRequest {
        val builder = GeofencingRequest.Builder()

        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)

        builder.addGeofences(mGeofenceList)
        return builder.build()
    }

    private fun getGeofencePendingIntent(): PendingIntent {
        val intent = Intent(this, GeofenceTransitionsIntentService::class.java)
        mGeofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return mGeofencePendingIntent as PendingIntent
    }

    override fun onComplete(task: Task<Void>) {
        mPendingGeofenceTask = PendingGeofenceTask.NONE
        if (task.isSuccessful) {
            val messageId = R.string.geofences_added
            Toast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show()
        } else {
            val errorMessage = "error"
            Log.w(TAG, errorMessage)
        }
    }


    /**
     * Adds geofences, which sets alerts to be notified when the device enters or exits one of the
     * specified geofences. Handles the success or failure results returned by addGeofences().
     */
//    fun addGeofencesButtonHandler(view: View) {
//        if (!checkPermissions()) {
//            mPendingGeofenceTask = PendingGeofenceTask.ADD
//            requestPermissions()
//            return
//        }
//        addGeofences()
//    }

    /**
     * Adds geofences. This method should be called after the user has granted the location
     * permission.
     */


//    fun removeGeofencesButtonHandler(view: View) {
//        if (!checkPermissions()) {
//            mPendingGeofenceTask = PendingGeofenceTask.REMOVE
//            requestPermissions()
//            return
//        }
//        removeGeofences()
//    }

    /**
     * Removes geofences. This method should be called after the user has granted the location
     * permission.
     */
//    private fun removeGeofences() {
//        if (!checkPermissions()) {
//            showSnackbar(getString(R.string.insufficient_permissions))
//            return
//        }
//
//        mGeofencingClient?.removeGeofences(getGeofencePendingIntent())?.addOnCompleteListener(this)
//    }

    /**
     * Runs when the result of calling [.addGeofences] and/or [.removeGeofences]
     * is available.
     * @param task the resulting Task, containing either a result or error.
     */


//    private fun populateGeofenceList() {
//        for (entry in Constants.BAY_AREA_LANDMARKS) {
//
//            mGeofenceList?.add(Geofence.Builder()
//                    // Set the request ID of the geofence. This is a string to identify this
//                    // geofence.
//                    .setRequestId(entry.key)
//
//                    // Set the circular region of this geofence.
//                    .setCircularRegion(
//                            entry.value.latitude,
//                            entry.value.longitude,
//                            Constants.GEOFENCE_RADIUS_IN_METERS
//                    )
//
//                    // Set the expiration duration of the geofence. This geofence gets automatically
//                    // removed after this period of time.
//                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
//
//                    // Set the transition types of interest. Alerts are only generated for these
//                    // transition. We track entry and exit transitions in this sample.
//                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
//
//                    // Create the geofence.
//                    .build())
//        }
//    }

    /**
     * Ensures that only one button is enabled at any time. The Add Geofences button is enabled
     * if the user hasn't yet added geofences. The Remove Geofences button is enabled if the
     * user has added geofences.
     */
//    private fun setButtonsEnabledState() {
//        if (getGeofencesAdded()) {
//
//            mRemoveGeofencesButton.setEnabled(true)
//        } else {
//            mAddGeofencesButton.setEnabled(true)
//            mRemoveGeofencesButton.setEnabled(false)
//        }
//    }

    /**
     * Shows a [Snackbar] using `text`.
     *
     * @param text The Snackbar text.
     */
//    private fun showSnackbar(text: String) {
//        val container = findViewById<View>(android.R.id.content)
//        if (container != null) {
//            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show()
//        }
//    }

    /**
     * Shows a [Snackbar].
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
//    private fun showSnackbar(mainTextStringId: Int, actionStringId: Int,
//                             listener: View.OnClickListener) {
//        Snackbar.make(
//                findViewById<View>(android.R.id.content),
//                getString(mainTextStringId),
//                Snackbar.LENGTH_INDEFINITE)
//                .setAction(getString(actionStringId), listener).show()
//    }

    /**
     * Returns true if geofences were added, otherwise false.
     */
//    private fun getGeofencesAdded(): Boolean {
//        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
//                Constants.GEOFENCES_ADDED_KEY, false)
//    }

    /**
     * Stores whether geofences were added ore removed in [SharedPreferences];
     *
     * @param added Whether geofences were added or removed.
     */
//    private fun updateGeofencesAdded(added: Boolean) {
//        PreferenceManager.getDefaultSharedPreferences(this)
//                .edit()
//                .putBoolean(Constants.GEOFENCES_ADDED_KEY, added)
//                .apply()
//    }


//    private fun checkPermissions(): Boolean {
//        val permissionState = ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//        return permissionState == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun requestPermissions() {
//        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//
//        // Provide an additional rationale to the user. This would happen if the user denied the
//        // request previously, but didn't check the "Don't ask again" checkbox.
//        if (shouldProvideRationale) {
//            Log.i(TAG, "Displaying permission rationale to provide additional context.")
//            showSnackbar(R.string.permission_rationale, android.R.string.ok,
//                    View.OnClickListener {
//                        // Request permission
//                        ActivityCompat.requestPermissions(this@MainActivity,
//                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                                REQUEST_PERMISSIONS_REQUEST_CODE)
//                    })
//        } else {
//            Log.i(TAG, "Requesting permission")
//            // Request permission. It's possible this can be auto answered if device policy
//            // sets the permission in a given state or the user denied the permission
//            // previously and checked "Never ask again".
//            ActivityCompat.requestPermissions(this@MainActivity,
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                    REQUEST_PERMISSIONS_REQUEST_CODE)
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
//                                            grantResults: IntArray) {
//        Log.i(TAG, "onRequestPermissionResult")
//        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
//            if (grantResults.size <= 0) {
//                // If user interaction was interrupted, the permission request is cancelled and you
//                // receive empty arrays.
//                Log.i(TAG, "User interaction was cancelled.")
//            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Log.i(TAG, "Permission granted.")
//                performPendingGeofenceTask()
//            } else {
//                // Permission denied.
//
//                // Notify the user via a SnackBar that they have rejected a core permission for the
//                // app, which makes the Activity useless. In a real app, core permissions would
//                // typically be best requested during a welcome-screen flow.
//
//                // Additionally, it is important to remember that a permission might have been
//                // rejected without asking the user for permission (device policy or "Never ask
//                // again" prompts). Therefore, a user interface affordance is typically implemented
//                // when permissions are denied. Otherwise, your app could appear unresponsive to
//                // touches or interactions which have required permissions.
//                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
//                        View.OnClickListener {
//                            // Build intent that displays the App settings screen.
//                            val intent = Intent()
//                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                            val uri = Uri.fromParts("package",
//                                    BuildConfig.APPLICATION_ID, null)
//                            intent.data = uri
//                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                            startActivity(intent)
//                        })
//                mPendingGeofenceTask = PendingGeofenceTask.NONE
//            }
//        }
//    }
//

}


//    private val geofencePendingIntent: PendingIntent by lazy {
//        val intent = Intent(this, GeofenceTransitionsIntentService::class.java)
//        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
//        // addGeofences() and removeGeofences().
//        PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//    }



