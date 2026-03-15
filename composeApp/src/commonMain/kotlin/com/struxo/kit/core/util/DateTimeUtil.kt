package com.struxo.kit.core.util

import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Common date/time utilities built on `kotlinx-datetime`.
 *
 * All functions use the system default [TimeZone] unless stated otherwise.
 */
object DateTimeUtil {

    /**
     * Returns the current [Instant].
     */
    fun now(): Instant = Clock.System.now()

    /**
     * Returns today's [LocalDate] in the system default time zone.
     */
    fun today(): LocalDate = now().toLocalDateTime(TimeZone.currentSystemDefault()).date
}

/**
 * Formats this [Instant] into a human-readable string.
 *
 * Supported patterns:
 * - `"yyyy-MM-dd"` → `2026-03-11`
 * - `"yyyy-MM-dd HH:mm"` → `2026-03-11 14:30`
 * - `"dd/MM/yyyy"` → `11/03/2026`
 * - `"MM/dd/yyyy"` → `03/11/2026`
 * - `"HH:mm:ss"` → `14:30:45`
 * - `"HH:mm"` → `14:30`
 *
 * @param pattern Date/time format pattern.
 * @param timeZone Time zone for conversion (default: system default).
 * @return Formatted date/time string.
 */
fun Instant.toFormattedString(
    pattern: String,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): String {
    val ldt = this.toLocalDateTime(timeZone)
    val year = ldt.year.toString().padStart(4, '0')
    val month = ldt.month.toString().padStart(2, '0')
    val day = ldt.day.toString().padStart(2, '0')
    val hour = ldt.hour.toString().padStart(2, '0')
    val minute = ldt.minute.toString().padStart(2, '0')
    val second = ldt.second.toString().padStart(2, '0')

    return pattern
        .replace("yyyy", year)
        .replace("MM", month)
        .replace("dd", day)
        .replace("HH", hour)
        .replace("mm", minute)
        .replace("ss", second)
}

/**
 * Returns a human-readable relative time string for this [Instant].
 *
 * Examples: `"just now"`, `"5 min ago"`, `"2 hours ago"`, `"yesterday"`, `"3 days ago"`.
 *
 * @param now Reference instant for comparison (default: current time).
 * @return Relative time description.
 */
fun Instant.toRelativeString(now: Instant = Clock.System.now()): String {
    val duration = now - this
    val seconds = duration.inWholeSeconds
    val minutes = duration.inWholeMinutes
    val hours = duration.inWholeHours
    val days = duration.inWholeDays

    return when {
        seconds < 0 -> "in the future"
        seconds < 60 -> "just now"
        minutes < 60 -> "$minutes min ago"
        hours < 24 -> "$hours hour${if (hours != 1L) "s" else ""} ago"
        days == 1L -> "yesterday"
        days < 30 -> "$days days ago"
        days < 365 -> "${days / 30} month${if (days / 30 != 1L) "s" else ""} ago"
        else -> "${days / 365} year${if (days / 365 != 1L) "s" else ""} ago"
    }
}
