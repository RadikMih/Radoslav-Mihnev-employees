package com.employeespairtask.data.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class DateParser {
    companion object {
        fun parseDate(inputTime: String): LocalDate {
            val inputFormats = arrayOf(
                "yyyy-MM-d",
                "yyyy/MM/d",
                "yyyy;MM;d",
                "yyyy:MM:d",
                "d-MM-yyyy",
                "d/MM/yyyy",
                "d;MM;yyyy",
                "d:MM:yyyy",
                "MMM d yy",
                "MMM-d-yy",
                "MMM;d;yy",
                "MMM:d:yy",
                "yyyy MMMM dd",
                "yyyy-MMMM-dd",
                "yyyy:MMMM:dd",
                "yyyy/MM/dd",
                "yyyy-MM-dd",
                "yyyy;MM;dd",
                "yyyy:MM:dd",
                "dd-MM-yyyy",
                "dd/MM/yyyy",
                "MM/dd/yyyy"
            )

            for (inputFormat in inputFormats) {
                try {
                    val parser = DateTimeFormatter.ofPattern(inputFormat)
                    return LocalDate.parse(inputTime, parser)
                } catch (e: DateTimeParseException) {
                    // Try the next format if parsing fails
                }
            }

            throw DateTimeParseException("Problem with date parsing", "", 0)
        }
    }
}