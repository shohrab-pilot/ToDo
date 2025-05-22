package com.example.yeshasprabhakar.todo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class MyNotificationPublisher : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = "notification-id"
        const val NOTIFICATION = "notification"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        // intent?.getParcelableExtra(NOTIFICATION) needs API level check for newer Android versions
        // For simplicity, assuming it's handled correctly or using a compatibility library
        val notification: Notification? = intent?.getParcelableExtra(NOTIFICATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                MainActivity.NOTIFICATION_CHANNEL_ID, // Assuming MainActivity.NOTIFICATION_CHANNEL_ID is accessible
                "NOTIFICATION_CHANNEL_NAME",
                importance
            )
            notificationManager?.createNotificationChannel(notificationChannel)
        }

        val id = intent?.getIntExtra(NOTIFICATION_ID, 0) ?: 0
        if (notification != null) { // Only notify if notification is not null
            notificationManager?.notify(id, notification)
        }
    }
}
