package com.struxo.kit.core.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

/**
 * Creates a [RoomDatabase.Builder] for [AppDatabase] on Android.
 *
 * @param context Application context used to resolve the database file path.
 * @return A configured builder ready for `.build()`.
 */
fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val dbFile = context.getDatabasePath(AppDatabase.DB_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath,
    ).setDriver(BundledSQLiteDriver())
}
