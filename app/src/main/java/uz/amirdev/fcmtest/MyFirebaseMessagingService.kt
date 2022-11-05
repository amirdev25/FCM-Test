package uz.amirdev.fcmtest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelID = "notification_channel"
const val channelName = "uz.amirdev.fcmtest"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            generateNotification(
                remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!
            )
        }
    }

    private fun generateNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelID)
                .setSmallIcon(R.drawable.ilm_u_ziyo_logo).setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000)).setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
        builder = builder.setContent(getRemoteView(title, message))

        val notificationManger =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManger.createNotificationChannel(notificationChannel)
        }

        notificationManger.notify(0, builder.build())
    }

    private fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteViews = RemoteViews("uz.amirdev.fcmtest", R.layout.layout_notification)
        remoteViews.setTextViewText(R.id.notificationTitle, title)
        remoteViews.setTextViewText(R.id.notificationDesc, message)
        remoteViews.setImageViewResource(R.id.image, R.drawable.ilm_u_ziyo_logo)
        return remoteViews
    }



}