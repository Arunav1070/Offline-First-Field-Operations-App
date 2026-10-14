package com.example.offline_firstfieldoperationsapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey val id: String,
    val title : String,
    val description : String,
    val status : String,
    val syncStatus : SyncStatus = SyncStatus.SYNCED,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val photoPath: String? = null,
    val updatedAt: Long = System.currentTimeMillis()

)
enum class SyncStatus {
    SYNCED,
    PENDING_UPDATE,
    PENDING_UPLOAD
}
