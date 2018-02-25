package br.com.conseng.firebasedemo

import android.app.PendingIntent
import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import br.com.conseng.firebasedemo.Config.Config
import com.google.firebase.messaging.RemoteMessage

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
 */
class MainActivity : AppCompatActivity() {

    private val TAG = "Activity MAIN"

    var mRegistrationBroadcastReceiver:BroadcastReceiver?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRegistrationBroadcastReceiver=object :BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if(Config.STR_PUSH == intent!!.action) {
                    val message = intent.getStringExtra("message")
                    showNotification("Conseng", message)
                }
            }
        }
        printCurrentState("onCreate")
    }

    private fun showNotification(title: String, message: String?) {
        val intent=Intent(applicationContext, MainActivity::class.java)
        val contentIntent=PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder=NotificationCompat.Builder(applicationContext)
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_stat_notify)
                .setContentText(title)
                .setContentText(message)
                .setContentIntent(contentIntent)
        val notificationManager=baseContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())
    }

    override fun onStart() {
        printCurrentState("onStart")
        super.onStart()
    }

    override fun onResume() {
        printCurrentState("onResume")
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, IntentFilter("registrationComplete"))
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, IntentFilter(Config.STR_PUSH))
    }

    override fun onPause() {
        printCurrentState("onPause")
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver)
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
