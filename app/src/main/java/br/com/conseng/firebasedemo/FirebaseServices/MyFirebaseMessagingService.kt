package br.com.conseng.firebasedemo.FirebaseServices

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import br.com.conseng.firebasedemo.Config.Config
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val INFO_MESSAGE_TITLE = "titulo"
const val INFO_MESSAGE_TEXT = "message"
//const val INFO_MESSAGE_CLASS = "classe"

/**
 * Receive the notification from Firebase broadcast.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FIREBASE-MSG_SERVICE"

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        if (null != remoteMessage) {
            val body: String? = remoteMessage.notification!!.body
            val title: String? = remoteMessage.notification!!.title
            handleNotification(title, body)
        } else {
            Log.d(TAG, "Null remote message")
        }
    }

    private fun handleNotification(title: String?, body: String?) {
        val pushNotification = Intent(Config.STR_PUSH)
        pushNotification.putExtra(INFO_MESSAGE_TEXT, body ?: "Missing notification message.")
        pushNotification.putExtra(INFO_MESSAGE_TITLE, title ?: "Missing notification title")
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)
    }
}