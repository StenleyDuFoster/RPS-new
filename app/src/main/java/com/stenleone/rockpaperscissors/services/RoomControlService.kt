package com.stenleone.rockpaperscissors.services

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.work.*
import com.stenleone.rockpaperscissors.workers.DestroyRoomWorker
import com.stenleone.rockpaperscissors.workers.RemoveUserFromRoomWorker

class RoomControlService : Service() {

    companion object {
        const val ROOM_NAME = "room_name"
        const val USER_NAME = "user_name"
        const val DESTROY_SELF = "destroy_self"

        fun setupHost(activity: Activity, roomName: String) {
            activity.startService(Intent(activity, RoomControlService::class.java).also { intent ->
                intent.putExtra(ROOM_NAME, roomName)
            })
        }
        fun setupPlayer(activity: Activity, roomName: String, userName: String) {
            activity.startService(Intent(activity, RoomControlService::class.java).also { intent ->
                intent.putExtra(ROOM_NAME, roomName)
                intent.putExtra(USER_NAME, userName)
            })
        }
        fun destroyHost(activity: Activity) {
            activity.startService(Intent(activity, RoomControlService::class.java).also { intent ->
                intent.putExtra(DESTROY_SELF, DESTROY_SELF)
            })
        }
        fun destroyPlayer(activity: Activity) {
            activity.startService(Intent(activity, RoomControlService::class.java).also { intent ->
                intent.putExtra(DESTROY_SELF, DESTROY_SELF)
            })
        }
    }

    private var roomName: String? = null
    private var userName: String? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.v("112233", "onStartCommand")

        intent?.extras?.getString(ROOM_NAME)?.let {
            roomName = it
        }
        intent?.extras?.getString(USER_NAME)?.let {
            userName = it
        }

        if (intent?.extras?.getString(DESTROY_SELF) != null) {
            Log.v("112233", "stop self")
            stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {

        if (userName == null && roomName != null) {
            Log.v("112233", "destroy room")
            val constraints: Constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val data = Data.Builder().also {
                it.putString(DestroyRoomWorker.ROOM_NAME, roomName)
            }

            val testWorkRequest = OneTimeWorkRequest.Builder(DestroyRoomWorker::class.java)
                .setInputData(data.build())
                .setConstraints(constraints)
                .addTag(DestroyRoomWorker.TAG)

            WorkManager
                .getInstance(applicationContext)
                .enqueue(testWorkRequest.build())
        } else if (userName != null && roomName != null) {
            Log.v("112233", "destroy user")
            val constraints: Constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val data = Data.Builder().also {
                it.putString(RemoveUserFromRoomWorker.ROOM_NAME, roomName)
                it.putString(RemoveUserFromRoomWorker.PLAYER_NAME, userName)
            }

            val testWorkRequest = OneTimeWorkRequest.Builder(RemoveUserFromRoomWorker::class.java)
                .setInputData(data.build())
                .setConstraints(constraints)
                .addTag(RemoveUserFromRoomWorker.TAG)

            WorkManager
                .getInstance(applicationContext)
                .enqueue(testWorkRequest.build())
        }

        super.onDestroy()
    }
}