package br.com.conseng.firebasedemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.com.conseng.firebasedemo.Config.Config
import br.com.conseng.firebasedemo.FirebaseServices.INFO_MESSAGE_TEXT
import br.com.conseng.firebasedemo.FirebaseServices.INFO_MESSAGE_TITLE
import br.com.conseng.firebasedemo.NotificationGenerator.NotificationGenerator


/**
 * On this Demo App I will test the Google Firebase functionality, such as:
 * - push messages to the user.
 * To be able to implement the Firebase following changes were necessary on:
 * (1) Enable Google Sign-in on Project Sctructure/Authentication.
 * (2) Create the application key.
 * @see [https://developer.android.com/studio/publish/app-signing.html#manage-key]
 * (3) Sign the APK.
 * @see [https://developer.android.com/studio/publish/app-signing.html#sign-apk]
 * (4) Remove the APK key from Build.Gradle (App) using a proprietary.
 * @see [https://developer.android.com/studio/publish/app-signing.html#secure-shared-keystore]
 * (5) Get the APK SHA-1 to register the application on Google Firebase, running the signingReport
 *     task and get the SHA1 from RUN window text mode.
 * @see [https://stackoverflow.com/questions/39697905/generate-sha1-fingerprint-in-android-studio-2-2]
 * (6) Complete the appication registration on Google Firebase and save the downloaded file
 *     'google-services.json' on application root directory.
 * (7) Add on Build.Gradle (Project)/buildscript/dependencies:
 *     classpath 'com.google.gms:google-services:3.2.0'
 * (8a) Add to the bottom of the Build.Gradle (App):
 *      apply plugin: 'com.google.gms.google-services'
 * (8b) Add to Build.Gradle (App)/dependencies:
 *      compile 'com.google.firebase:firebase-core:11.8.0'
 * (9a) Add to AndroidManifest the following uses-permissions:
 *      - android.permission.INTERNET
 *      - android.permission.VIBRATE
 * (9b) Add to AndroidManifest the MyFirebaseIdService service and the action INSTANCE_ID_EVENT.
 * (9v) Add to AndroidManifest the MyFirebaseMessagingService service and the action MESSAGING_EVENT.
 * NOW THE APPLICATION IS READY TO WORK WITH FIREBASE FEATURES...
 * @see [https://www.youtube.com/watch?v=I485b7LzYkM]
 */
class MainActivity : AppCompatActivity() {

    private val TAG = "FIREBASE-Activity_MAIN"

    /**
     * Receive notifications from Firebase.
     */
    var mRegistrationBroadcastReceiver: BroadcastReceiver? = null

    /**
     * Show the Firebase received notification.
     */
    private var ng: NotificationGenerator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRegistrationBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (Config.STR_PUSH == intent.action) {
                    val message = intent.getStringExtra(INFO_MESSAGE_TEXT)
                    val title = intent.getStringExtra(INFO_MESSAGE_TITLE)
                    if (null == ng) ng = NotificationGenerator(MainActivity::class.java)
                    ng!!.showReceivedNotification(applicationContext, title, message)
                }
            }
        }
        printCurrentState("onCreate")
    }

    override fun onStart() {
        printCurrentState("onStart")
        super.onStart()
    }

    override fun onResume() {
        printCurrentState("onResume")
        super.onResume()
        if (null != mRegistrationBroadcastReceiver) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, IntentFilter("registrationComplete"))
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, IntentFilter(Config.STR_PUSH))
        }
    }

    override fun onPause() {
        printCurrentState("onPause")
        if (null != mRegistrationBroadcastReceiver) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver)
        }
        super.onPause()
    }

    override fun onStop() {
        printCurrentState("onStop")
        super.onStop()
    }

    override fun onRestart() {
        printCurrentState("onRestart")
        super.onRestart()
    }

    override fun onDestroy() {
        printCurrentState("onDestroy")
        super.onDestroy()
    }

    private fun printCurrentState(estado: String) {
        Log.d(TAG, "state=$estado")
    }
}
