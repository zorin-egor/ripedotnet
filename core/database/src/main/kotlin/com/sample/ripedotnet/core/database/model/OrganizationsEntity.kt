package com.sample.ripedotnet.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Organizations",
    indices = [
        Index(value = ["org_id"], unique = true),
        Index(value = ["name"])
    ]
)
data class OrganizationsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "org_id") val orgId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "created") val created: String,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "modified") val modified: String?,
    @ColumnInfo(name = "phone") val phone: String?,
    @ColumnInfo(name = "fax") val fax: String?,
)