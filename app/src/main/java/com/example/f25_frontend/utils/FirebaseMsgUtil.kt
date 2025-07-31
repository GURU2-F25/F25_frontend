package com.example.f25_frontend.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat.*
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.f25_frontend.MainActivity
import com.example.f25_frontend.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
/*
    @Author 조수연
    Firebase Cloud Messaging 라이브러리 유틸리티
    일정/팔로우 FCM PUSH 알림 기능
    @TODO 알림페이지 리다이렉션 데이터 연동 예정
*/
class FirebaseMsgUtil : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        @TODO PUSH 알림 내 서버 데이터 파싱 후 페이지 리다이렉션 구현 예정
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "메세지 데이터 확인: ${remoteMessage.data}")
//            대규모 프로세싱 시 스케쥴러 사용
            if (needsToBeScheduled()) {
                scheduleJob()
            } else {
//            10초 이내 프로세스용
                handleNow()
            }
        }
        remoteMessage.notification?.let {
            Log.d(TAG, "메세지 알림 내용: ${it.body}")
        }
        /* sendNotification DOCS 참조하여 FCM 사용 시 반드시 initial 해줄 것*/
    }

    private fun needsToBeScheduled() = true
//    FCM 토큰 갱신 시 서버측에 토큰 업데이트
    override fun onNewToken(token: String) {
        Log.d(TAG, "토큰 갱신: $token")
        sendRegistrationToServer(token)
    }

    private fun scheduleJob() {
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .build()
        WorkManager.getInstance(this)
            .beginWith(work)
            .enqueue()
    }

    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "서버 전송 토큰($token)")
    }

    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val requestCode = 0
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("FCM Message")
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

    internal class MyWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
        override fun doWork(): Result {
            // TODO(developer): add long running task here.
            return Result.success()
        }
    }
}