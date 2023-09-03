package com.employeespairtask.data.repository.employee

import android.net.Uri
import com.employeespairtask.model.Team

interface EmployeePairRepository {

    suspend fun getEmployeeTopPair(uri: Uri): Result<Team>
}