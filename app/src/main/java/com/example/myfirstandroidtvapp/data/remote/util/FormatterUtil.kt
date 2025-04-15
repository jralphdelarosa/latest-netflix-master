package com.example.myfirstandroidtvapp.data.remote.util

object FormatterUtil {
    fun formatDuration(minutes: String): String {
        val hours = minutes.toDouble() / 60
        val remainingMinutes = minutes.toDouble() % 60
        return when {
            hours > 0 && remainingMinutes > 0 -> "${String.format("%.2f", hours)}h ${String.format("%.2f", remainingMinutes)}m"
            hours > 0 -> "${String.format("%.2f", hours)}h"
            else -> "${remainingMinutes}m"
        }
    }
}