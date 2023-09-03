package com.employeespairtask.data.util

import android.net.Uri
import com.employeespairtask.model.DataEntry

interface ScvReader {

    suspend fun readCsv(uri: Uri): Result<List<DataEntry>>
}