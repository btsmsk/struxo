package com.struxo.kit.feature.auth.data.local

import com.struxo.kit.core.data.local.dao.AuthUserDao
import com.struxo.kit.core.data.local.entity.AuthUserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Local data source wrapping [AuthUserDao] operations.
 *
 * @param dao Room DAO for authenticated user records.
 */
class AuthLocalSource(private val dao: AuthUserDao) {

    /**
     * Observes a single user by ID.
     */
    fun observeUser(id: String): Flow<AuthUserEntity?> = dao.getById(id)

    /**
     * Observes all cached users.
     */
    fun observeAll(): Flow<List<AuthUserEntity>> = dao.getAll()

    /**
     * Inserts or replaces a user record.
     */
    suspend fun saveUser(entity: AuthUserEntity) = dao.insert(entity)

    /**
     * Deletes all cached user records.
     */
    suspend fun clearAll() = dao.deleteAll()
}
