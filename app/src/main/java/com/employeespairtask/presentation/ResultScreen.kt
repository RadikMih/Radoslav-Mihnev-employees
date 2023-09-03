package com.employeespairtask.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.employeespairtask.R
import com.employeespairtask.model.Team

@Composable
fun ResultScreen(team: Team) {

    val firstEmployeeId = team.firstEmployeeId.toString()
    val secondEmployeeId = team.secondEmployeeId.toString()
    val projectCount = team.projectsAndTime.size
    val projectIds = team.projectsAndTime.keys.map { it.toString() }
    val timeOnProjects = team.projectsAndTime.values.map { it.toString() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 48.dp)
    ) {
        item {
            EntryItem(
                firstEmployeeId = stringResource(R.string.employee1),
                secondEmployeeId = stringResource(R.string.employee2),
                projectId = stringResource(R.string.project_id),
                timeSpent = stringResource(R.string.days_worked)
            )
        }

        items(projectCount) { index ->
            EntryItem(
                firstEmployeeId = firstEmployeeId,
                secondEmployeeId = secondEmployeeId,
                projectId = projectIds[index],
                timeSpent = timeOnProjects[index]
            )
        }
    }
}

@Composable
private fun EntryItem(
    firstEmployeeId: String,
    secondEmployeeId: String,
    projectId: String,
    timeSpent: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        EntryText(text = firstEmployeeId)
        EntryText(text = secondEmployeeId)
        EntryText(text = projectId)
        EntryText(text = timeSpent)
    }
}

@Composable
private fun EntryText(text: String) {
    Text(
        text = text,
        fontSize = 20.sp,
        textAlign = TextAlign.Start
    )
}