package com.employeespairtask.di

import android.app.Application
import com.employeespairtask.data.repository.employee.EmployeePairRepository
import com.employeespairtask.data.repository.employee.EmployeePairRepositoryImpl
import com.employeespairtask.data.repository.entrydata.EntryDataRepository
import com.employeespairtask.data.repository.entrydata.EntryDataRepositoryImpl
import com.employeespairtask.data.util.ScvReader
import com.employeespairtask.data.util.ScvReaderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindEntryDataRepository(
        entryDataRepository: EntryDataRepositoryImpl
    ): EntryDataRepository

    @Binds
    @Singleton
    abstract fun bindEmployeePairRepository(
        entryDataRepository: EmployeePairRepositoryImpl
    ): EmployeePairRepository
}

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {

    @Singleton
    @Provides
    fun provideCsvReader(context: Application): ScvReader {
        return ScvReaderImpl(context)
    }
}

