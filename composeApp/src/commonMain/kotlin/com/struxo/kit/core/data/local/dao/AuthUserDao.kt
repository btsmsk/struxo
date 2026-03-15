package com.struxo.kit.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.struxo.kit.core.data.local.entity.AuthUserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data access object for [AuthUserEntity] operations.
 */
@Dao
interface AuthUserDao {

    /**
     * Observes a single user by ID.
     *
     * @param id The user's unique identifier.
     * @return A [Flow] emitting the user or `null` when the table changes.
     */
    @Query("SELECT * FROM auth_users WHERE id = :id")
    fun getById(id: String): Flow<AuthUserEntity?>

    /**
     * Observes all cached users.
     *
     * @return A [Flow] emitting the full user list when the table changes.
     */
    @Query("SELECT * FROM auth_users")
    fun getAll(): Flow<List<AuthUserEntity>>

    /**
     * Inserts or replaces a user record.
     *
     * @param user The entity to upsert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: AuthUserEntity)

    /**
     * Deletes all cached user records.
     */
    @Query("DELETE FROM auth_users")
    suspend fun deleteAll()
}
