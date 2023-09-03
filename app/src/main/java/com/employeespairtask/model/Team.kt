package com.employeespairtask.model

data class Team(
    val firstEmployeeId: Int,
    val secondEmployeeId: Int,
    val projectsAndTime: MutableMap<Int, Long>,
    var totalTimeAsTeam: Long
)