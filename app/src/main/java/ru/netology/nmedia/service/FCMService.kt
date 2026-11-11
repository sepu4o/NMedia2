package ru.netology.nmedia.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post
import kotlin.random.Random

class FCMService : FirebaseMessagingService() {

    private val action = "action"
    private val content = "content"
    private val channelId = "remote"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Server notifications"
            val descriptionText = "Notifications from remote server"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println(token)
    }

    @Deprecated("Deprecated in Firebase")
    override fun onMessageReceived(message: RemoteMessage) {
        message.data[action]?.let { actionString ->
            val action = Action.entries.find { it.name == actionString }
            if (action == null) {
                Log.w("FCM", "Unknown action: $actionString")
                return@let
            }
            when (action) {
                Action.LIKE -> handleLike(gson.fromJson(message.data[content], Like::class.java))
                Action.POST -> handlePost(gson.fromJson(message.data[content], NewPost :: class.java))
            }
        }
    }

    private fun handleLike(content: Like) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("${content.userName} поставил лайк посту ${content.postAuthor}")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(content.postContent ?: "Без текста")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notify(notification)
    }

    private fun handlePost(content: NewPost) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("${content.userName} опубликовал новый пост:")
                .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(content.postContent)
                )

            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notify(notification)
    }

    private fun notify(notification: Notification) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(Random.nextInt(100_000), notification)
        }
    }
}

enum class Action {
    LIKE, POST
}

data class Like(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String,
    val postContent: String
)

data class NewPost(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postContent: String
)