package com.employeespairtask.data.repository.entrydata

import android.net.Uri
import com.employeespairtask.model.DataEntry

interface EntryDataRepository {

    suspend fun collectDataFromFile(uri: Uri): Result<List<DataEntry>>
}