package com.sample.ripedotnet.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sample.ripedotnet.core.database.model.OrganizationsEntity


@Dao
interface OrganizationsDao {

    @Query("SELECT * FROM Organizations")
    suspend fun getOrganizations(): List<OrganizationsEntity>

    @Query(
        "SELECT * FROM Organizations " +
        "WHERE Organizations.name LIKE :query OR Organizations.name LIKE '%' || ' ' || :query OR " +
                "Organizations.name LIKE :query || ' ' || '%' OR " +
                "Organizations.name LIKE '%' || ' ' || :query || ' ' || '%' " +
                "ORDER BY Organizations.org_id LIMIT :limit OFFSET :offset"
    )
    suspend fun getOrganizations(query: String, offset: Int, limit: Int = 30): List<OrganizationsEntity>

    @Query("SELECT * FROM Organizations WHERE Organizations.org_id LIKE :id")
    suspend fun getOrganizationById(id: String): OrganizationsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: OrganizationsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<OrganizationsEntity>)

    @Delete
    suspend fun delete(item: OrganizationsEntity)

}
