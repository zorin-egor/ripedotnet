package com.sample.ripedotnet.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "InetNum",
    indices = [
        Index(value = ["inet_id"], unique = true),
        Index(value = ["ip"], unique = true),
        Index(value = ["org_id"]),
        Index(value = ["query_ip"]),
    ]
)
data class InetNumEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "inet_id") val inetId: String,
    @ColumnInfo(name = "ip") val ip: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "query_ip") val queryIp: String?,
    @ColumnInfo(name = "org_id") val orgId: String?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "ip_from") val ipFrom: String?,
    @ColumnInfo(name = "ip_to") val ipTo: String?,
    @ColumnInfo(name = "desc") val descr: String?,
)