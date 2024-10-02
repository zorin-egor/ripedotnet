package com.sample.ripedotnet.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sample.ripedotnet.core.database.model.InetNumEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InetNumsDao {

    @Query("SELECT * FROM InetNum")
    fun getInetNums(): Flow<List<InetNumEntity>>

    @Query("SELECT * FROM InetNum WHERE InetNum.name LIKE '%' || :query || '%' " +
            "ORDER BY InetNum.name LIMIT :limit OFFSET :offset")
    fun getInetNums(query: String, offset: Int, limit: Int = 30): Flow<List<InetNumEntity>>

    @Query("SELECT * FROM InetNum WHERE InetNum.ip LIKE '%' || :ip || '%' OR InetNum.query_ip LIKE :ip")
    suspend fun getInetNumByIp(ip: String): InetNumEntity?

    @Query("SELECT * FROM InetNum WHERE InetNum.org_id LIKE :orgId " +
            "ORDER BY InetNum.ip LIMIT :limit OFFSET :offset")
    suspend fun getInetNumsByOrgId(orgId: String, offset: Int, limit: Int = 30): List<InetNumEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: InetNumEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<InetNumEntity>)

    @Delete
    suspend fun delete(item: InetNumEntity)
}
