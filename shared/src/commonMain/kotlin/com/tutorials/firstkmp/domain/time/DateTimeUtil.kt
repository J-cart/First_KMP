package com.tutorials.firstkmp.domain.time

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

object DateTimeUtil {

    fun now()= Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    fun toEpochMillis(dateTime: LocalDateTime)= dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()

    fun formatDateTime(dateTime: LocalDateTime):String{
        val month = dateTime.month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }
        val day = if (dateTime.dayOfMonth < 10) "0${dateTime.dayOfMonth}" else dateTime.dayOfMonth
        val year = dateTime.year
        val hour = if (dateTime.hour < 10) "0${dateTime.hour}" else dateTime.hour
        val minute = if (dateTime.minute < 10) "0${dateTime.minute}" else dateTime.minute
        return buildString {
            append(month)
            append(" ")
            append(day)
            append(", ")
            append(year)
        }
    }

    fun formatDateTimeToday(): String {
        val clockTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val month = clockTime.month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }
        val day = if (clockTime.dayOfMonth < 10) "0${clockTime.dayOfMonth}" else clockTime.dayOfMonth
        val dayName = clockTime.dayOfWeek.name.lowercase().take(3).replaceFirstChar { it.uppercase() }

        return buildString {
            append(month)
            append(" ")
            append(day)
            append(", ")
            append(dayName)
        }
    }
    fun formatDateTimeTodayForTitle(): String {
        val clockTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val month = clockTime.month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }
        val day = if (clockTime.dayOfMonth < 10) "0${clockTime.dayOfMonth}" else clockTime.dayOfMonth

        return buildString {
            append(month)
            append(" ")
            append(day)
        }
    }
}