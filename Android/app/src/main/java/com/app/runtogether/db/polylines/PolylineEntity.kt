package com.app.runtogether.db.polylines

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "polylines")
data class PolylineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val points : String
)
