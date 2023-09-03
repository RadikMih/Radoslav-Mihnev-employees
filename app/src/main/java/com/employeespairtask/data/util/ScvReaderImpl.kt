package com.employeespairtask.data.util

import android.app.Application
import android.content.res.Resources
import android.net.Uri
import com.employeespairtask.R
import com.employeespairtask.model.DataEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDate
import javax.inject.Inject

private const val csvDelimiter = ", "
private const val NULL_DATE = "NULL"

class ScvReaderImpl @Inject constructor(
    private val context: Application
) : ScvReader {

    override suspend fun readCsv(uri: Uri) = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openInputStream(uri).use { inputStream ->
                val list = BufferedReader(InputStreamReader(inputStream))
                    .lineSequence()
                    .filter { it.isNotBlank() }
                    .map {
                        val (employeeId, projectId, dateFrom, dateTo)
                                = it.split(csvDelimiter)
                        val localDateFrom: LocalDate = DateParser.parseDate(dateFrom.trim())
                        val localDateTo: LocalDate =
                            if (dateTo == NULL_DATE) {
                                LocalDate.now()
                            } else {
                                DateParser.parseDate(dateTo.trim())
                            }

                        DataEntry(
                            employeeId = employeeId.toInt(),
                            projectId = projectId.toInt(),
                            dateFrom = localDateFrom,
                            dateTo = localDateTo
                        )
                    }.toList()
                Result.success(list)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(Exception(Resources.getSystem().getString(R.string.parsing_problem)))
        }
    }
}