package com.example.localscurestorage.DI


import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.localscurestorage.servuces.FileDataDatabase
import com.example.localscurestorage.viewModle.FileDataViewModle
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


fun createDatabase(context: Context, databaseName: String): RoomDatabase {
   try{
       return Room.databaseBuilder(
           context.applicationContext,
           RoomDatabase::class.java,
           databaseName
       ).build()
   }catch (e:Exception){
       Log.e("databaseCreationError","this error from creation DataBase ${e.message}")
       throw e;
   }
}

val databaseModule = module {
    factory<(Context, String) -> RoomDatabase> {
        { context, databaseName -> createDatabase(context, databaseName) }
    }
    viewModel { FileDataViewModle(get()) }
}