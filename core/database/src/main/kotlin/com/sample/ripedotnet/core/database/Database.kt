package com.sample.ripedotnet.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sample.ripedotnet.core.database.dao.InetNumsDao
import com.sample.ripedotnet.core.database.dao.OrganizationsDao
import com.sample.ripedotnet.core.database.model.InetNumEntity
import com.sample.ripedotnet.core.database.model.OrganizationsEntity

@Database(
    entities = [
        OrganizationsEntity::class,
        InetNumEntity::class
    ],
    version = 1,
    exportSchema = false
)
internal abstract class Database : RoomDatabase() {

    abstract fun detailsDao(): OrganizationsDao

    abstract fun usersDao(): InetNumsDao

}
