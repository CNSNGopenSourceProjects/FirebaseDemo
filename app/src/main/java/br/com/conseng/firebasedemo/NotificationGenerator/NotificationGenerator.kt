package br.com.conseng.firebasedemo.NotificationGenerator

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import br.com.conseng.firebasedemo.MainActivity
import br.com.conseng.firebasedemo.R

const val NOTIFICATION_ID_REGULAR = 9

/**
 * Class to generate the notification to open the Activity.
 * @property [notificationIntentClass] The component class that is to be used for the notification intent.
 *           The default class, for this example, is [NotificationActivity].
 */
class NotificationGenerator(var notificationIntentClass: Class<*> = MainActivity::class.java) {

    private var notificationManager: NotificationManager? = null
    private var notificationChannel: NotificationChannel? = null
    private val channelId = "br.com.conseng.firebasedemo"
    private val channelDescription = "Test notification"

    /**
     * Show the notification received from Firebase.
     * @param [context] application context for associate the notification with.
     * @param [title] Firebase notification title.
     * @param [message] Firebase notification message.
     */
    fun showReceivedNotification(context: Context, title: String, message: String) {
        // Build the content of the notification
        val nBuilder = getNotificationBuilder(context,
                title, message, R.drawable.ic_stat_notify,
                "Showing the notification received from Firebase.")

        // Notification through notification manager
        notificationManager?.notify(NOTIFICATION_ID_REGULAR, nBuilder.build())
    }

    /**
     * Initialize the notification manager and channel Id.
     * The notification builder has the basic initialization:
     *     - AutoCancel=true
     *     - LargeIcon = SmallIcon
     * @param [context] application context for associate the notification with.
     * @param [notificationTitle] notification title.
     * @param [notificationText] notification text.
     * @param [notificationIconId] notification icon id from application resource.
     * @param [notificationTicker] notification ticker text for accessibility.
     * @return the PendingIntent to be used on this notification.
     */
    private fun getNotificationBuilder(context: Context,
                                       notificationTitle: String,
                                       notificationText: String,
                                       notificationIconId: Int,
                                       notificationTicker: String): Notification.Builder {
        // Define the notification channel for newest Android versions
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent = getPendingIntent(context)
        lateinit var builder: Notification.Builder

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (null == notificationChannel) {
                notificationChannel = NotificationChannel(channelId, channelDescription, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel?.enableLights(true)
                notificationChannel?.lightColor = Color.GREEN
                notificationChannel?.enableVibration(false)
                notificationManager?.createNotificationChannel(notificationChannel)
            }
            builder = Notification.Builder(context, channelId)
        } else {
            builder = Notification.Builder(context)
        }

        // Build the content of the notification
        builder.setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setSmallIcon(notificationIconId)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, notificationIconId))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setTicker(notificationTicker)
        // Restricts the notification information when the screen is blocked.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setVisibility(Notification.VISIBILITY_PRIVATE)
        }

        return builder
    }

    /**
     * Retorna a Intent que será utilizada nesta notificação.
     * Para poder recompor a estruturas das activities, é necessário declarar o parentesco no manifesto
     * e incluir os atributos:
     *          + launchMode="singleTask"
     *          + taskAffinity=""
     *          + excludeFromRecents="true"
     * da NotificationActivity.
     * @param [context] application context for associate the notification with.
     * @return the activity associated to the notification.
     * @see [https://developer.android.com/guide/topics/ui/notifiers/notifications.html#NotificationResponse]
     */
    private fun getPendingIntent(context: Context): PendingIntent {
        val resultIntent = Intent(context, notificationIntentClass)
        resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        val resultPendingIntent = PendingIntent.getActivity(context, 0,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        return resultPendingIntent
    }
}