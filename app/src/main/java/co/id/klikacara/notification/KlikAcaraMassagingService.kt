package co.id.klikacara.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import co.id.klikacara.BuildConfig
import co.id.klikacara.R
import co.id.klikacara.main.view.MainActivity
import co.id.klikacara.splash.SplashActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationChannel
import android.os.Build.VERSION_CODES.O
import android.os.Build
import android.system.Os


class KlikAcaraMassagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            FirebaseFirestore.getInstance().collection(BuildConfig.userDb).document(user.uid)
                .update("token", token)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val requestID = System.currentTimeMillis().toInt()
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val stackBuilder = TaskStackBuilder.create(applicationContext)
        stackBuilder.addParentStack(SplashActivity::class.java)
        stackBuilder.addNextIntent(intent)
        val pendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(
            this,
            getString(R.string.default_notification_channel_id)
        )
            .setContentTitle(remoteMessage.data["title"])
            .setContentText(remoteMessage.data["description"])
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setSmallIcon(R.mipmap.ic_logo)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(requestID, notificationBuilder.build())
    }
}