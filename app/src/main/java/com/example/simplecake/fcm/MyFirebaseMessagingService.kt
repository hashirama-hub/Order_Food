package com.example.simplecake.fcm

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.simplecake.Activity.MainActivity
import com.example.simplecake.MyApplication
import com.example.simplecake.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
//        val notification: RemoteMessage.Notification = message.notification ?: return
//        val strTitle: String? = notification.title
//        val strMessage:String?= notification.body

        //data message
        var strMap:Map<String,String> = message.data ?: return
        val strTitle: String? = strMap["user_name"]
        val strMessage:String?= strMap["description"]
        sendNotifcation(strTitle,strMessage)
    }

    private fun sendNotifcation(strTitle: String?, strMessage: String?) {
        val intent:Intent = Intent(this, MainActivity::class.java)
        val pendingIntent:PendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT)
        val notificationBuilder:NotificationCompat.Builder = NotificationCompat.Builder(this,MyApplication.CHANNEL_ID)
            .setContentTitle(strTitle)
            .setContentText(strMessage)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)

        val notification:Notification = notificationBuilder.build()
        val notificationManager:NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(notificationManager!=null){
              notificationManager.notify(1,notification)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("Token",token)
    }
}