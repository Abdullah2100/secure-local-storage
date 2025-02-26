package com.example.localscurestorage.DI

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.localscurestorage.servuces.FileDataDatabase
import com.example.localscurestorage.util.General
import com.example.localscurestorage.viewModle.FileDataViewModle
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun createDatabase(context: Context, databaseName: String): FileDataDatabase {
    return try {
        Room.databaseBuilder(
            context.applicationContext,
            FileDataDatabase::class.java, // ✅ Corrected to use actual Database class
            databaseName
        ).openHelperFactory(General.encriptionFactory(databaseName))
            .build()
    } catch (e: Exception) {
        Log.e("DatabaseError", "Error creating database: ${e.message}")
        throw e
    }
}

// ✅ Define the Koin module properly
val databaseModule = module {

    single<(Context, String) -> FileDataDatabase> { { context, dbName -> createDatabase(context, dbName) } }

    viewModel { FileDataViewModle(get()) }
}
