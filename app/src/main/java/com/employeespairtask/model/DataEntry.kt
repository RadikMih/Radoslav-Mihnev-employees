package com.employeespairtask.model

import java.time.LocalDate

data class DataEntry(
    val employeeId: Int,
    val projectId: Int,
    val dateFrom: LocalDate,
    val dateTo: LocalDate
)
