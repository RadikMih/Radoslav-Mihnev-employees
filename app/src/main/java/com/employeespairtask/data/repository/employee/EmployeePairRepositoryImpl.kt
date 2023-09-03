package com.employeespairtask.data.repository.employee

import android.content.res.Resources
import android.net.Uri
import com.employeespairtask.R
import com.employeespairtask.data.repository.entrydata.EntryDataRepository
import com.employeespairtask.model.DataEntry
import com.employeespairtask.model.Team
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.abs

class EmployeePairRepositoryImpl @Inject constructor(
    private val entryDataRepository: EntryDataRepository
) : EmployeePairRepository {

    private var teams: MutableList<Team> = mutableListOf()

    override suspend fun getEmployeeTopPair(uri: Uri): Result<Team> {
        val result = entryDataRepository.collectDataFromFile(uri)

        return result.fold(
            onSuccess = {
                getTeamsFromResult(it)
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    private fun getTeamsFromResult(data: List<DataEntry>): Result<Team> {
        val allTeams = getAllTeams(data)

        if (allTeams.isEmpty()) {
            return Result.failure(Exception(Resources.getSystem().getString(R.string.no_team_found)))
        }

        var topScore: Long = allTeams.first().totalTimeAsTeam
        var topTeam: Team = allTeams.first()

        allTeams.forEach { team ->
            if (team.totalTimeAsTeam > topScore) {
                topScore = team.totalTimeAsTeam
                topTeam = team
            }
        }

        return Result.success(topTeam)
    }

    private fun getAllTeams(dataEntries: List<DataEntry>): List<Team> {
        for (i in 0 until dataEntries.size - 1) {
            for (j in i + 1 until dataEntries.size) {

                val firstEmployee = dataEntries[i]
                val secondEmployee = dataEntries[j]

                if (firstEmployee.projectId == secondEmployee.projectId
                    && workedTogether(firstEmployee, secondEmployee)
                ) {
                    val daysTogether = calculateDayWorkedTogether(firstEmployee, secondEmployee)

                    if (daysTogether > 0) {
                        updateTeamsList(
                            firstEmployee = firstEmployee,
                            secondEmployee = secondEmployee,
                            daysTogether = daysTogether
                        )
                    }
                }
            }
        }
        
        return teams
    }

    private fun workedTogether(firstEmployee: DataEntry, secondEmployee: DataEntry): Boolean {
        return (firstEmployee.dateFrom.isBefore(secondEmployee.dateTo) ||
                firstEmployee.dateFrom.isEqual(secondEmployee.dateTo)) &&
                (firstEmployee.dateTo.isAfter(secondEmployee.dateFrom) ||
                        firstEmployee.dateTo.isEqual(secondEmployee.dateFrom))
    }

    private fun calculateDayWorkedTogether(
        firstEmployee: DataEntry,
        secondEmployee: DataEntry
    ): Long {
        val startDate: LocalDate = if (firstEmployee.dateFrom.isBefore(secondEmployee.dateFrom)) {
            secondEmployee.dateFrom
        } else {
            firstEmployee.dateFrom
        }

        val endDate: LocalDate = if (firstEmployee.dateTo.isBefore(secondEmployee.dateTo)) {
            firstEmployee.dateTo
        } else {
            secondEmployee.dateTo
        }

        return abs(ChronoUnit.DAYS.between(startDate, endDate))
    }

    private fun updateTeamsList(
        firstEmployee: DataEntry,
        secondEmployee: DataEntry,
        daysTogether: Long
    ) {
        var teamExist = false

        teams.forEach { team ->
            if (teamExist(team, firstEmployee.employeeId, secondEmployee.employeeId)) {
                team.projectsAndTime[firstEmployee.projectId] = daysTogether
                team.totalTimeAsTeam += daysTogether
                teamExist = true
            }
        }

        if (!teamExist) {
            teams.add(
                Team(
                    firstEmployeeId = firstEmployee.employeeId,
                    secondEmployeeId = secondEmployee.employeeId,
                    projectsAndTime = mutableMapOf(firstEmployee.projectId to daysTogether),
                    totalTimeAsTeam = daysTogether
                )
            )
        }
    }

    private fun teamExist(team: Team, firstEmployeeId: Int, secondEmployeeId: Int): Boolean {
        return ((team.firstEmployeeId == firstEmployeeId
                && team.secondEmployeeId == secondEmployeeId)
                || (team.firstEmployeeId == secondEmployeeId
                && team.secondEmployeeId == firstEmployeeId))
    }
}