package com.employeespairtask.data.repository.entrydata

import android.net.Uri
import com.employeespairtask.data.util.ScvReader
import com.employeespairtask.model.DataEntry
import javax.inject.Inject

class EntryDataRepositoryImpl @Inject constructor(
    private val scvReader: ScvReader
) : EntryDataRepository {

    override suspend fun collectDataFromFile(uri: Uri): Result<List<DataEntry>> {
        return scvReader.readCsv(uri)
    }
}