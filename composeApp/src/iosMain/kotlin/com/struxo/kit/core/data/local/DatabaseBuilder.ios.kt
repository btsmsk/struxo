@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.struxo.kit.core.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import platform.Foundation.NSFileManager
import platform.Foundation.NSHomeDirectory

/**
 * Creates a [RoomDatabase.Builder] for [AppDatabase] on iOS.
 *
 * The database file is stored under `~/databases/` in the app sandbox.
 * The parent directory is created if it does not exist.
 *
 * @return A configured builder ready for `.build()`.
 */
fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbDir = NSHomeDirectory() + "/databases"
    NSFileManager.defaultManager.createDirectoryAtPath(dbDir, withIntermediateDirectories = true, attributes = null, error = null)
    val dbFilePath = "$dbDir/${AppDatabase.DB_NAME}"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath,
    ).setDriver(BundledSQLiteDriver())
}
