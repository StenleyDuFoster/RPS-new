package com.stenleone.rockpaperscissors.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.stenleone.rockpaperscissors.managers.HostRoomManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DestroyRoomWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val TAG = "DestroyRoomWorker"
        const val ROOM_NAME = "room_name"
    }

    private val hostRoomManager = HostRoomManager()

    override suspend fun doWork(): Result {

        var result = Result.success()

        inputData.getString(ROOM_NAME)?.let {
            hostRoomManager.removeRoom(it, {

            }, {
                result = Result.failure()
            })
        }

        return result
    }

}