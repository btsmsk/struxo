# ADR-0009: Storage

**Status:** Accepted
**Date:** 2026-03-10

## Context

Need local database and secure token storage that works across KMP targets.

## Decision

Room Multiplatform for database, expect/actual for DatabaseBuilder and TokenProvider.

## Rules

**R1 — Room database defined in commonMain:**
```kotlin
@Database(entities = [AuthUserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authUserDao(): AuthUserDao
    companion object { const val DB_NAME = "struxo.db" }
}
```

**R2 — DatabaseBuilder via expect/actual:**
- `androidMain`: `Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)`
- `iosMain`: `Room.databaseBuilder<AppDatabase>(NSHomeDirectory() + "/databases/" + DB_NAME)`

**R3 — DAOs return Flow<T> for reactive queries:**
```kotlin
@Dao
interface AuthUserDao {
    @Query("SELECT * FROM auth_users WHERE id = :id")
    fun getById(id: String): Flow<AuthUserEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: AuthUserEntity)
}
```

**R4 — TokenProvider via expect/actual:**
- `androidMain`: EncryptedSharedPreferences
- `iosMain`: Keychain via Security framework

**R5 — No direct storage access in features:**
Features use Repository interfaces. Direct Room/preferences access only in data layer implementations.

**R6 — KSP configuration:**
Room schema export directory configured in build.gradle.kts. KSP processors for each target.

## Consequences

- Same Room database definition compiles for Android and iOS
- Platform-specific storage (Keychain vs EncryptedSharedPreferences) isolated behind interfaces
- Reactive DAOs enable offline-first patterns
