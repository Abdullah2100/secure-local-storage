package com.example.localscurestorage.DI


import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
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
    single { (context: Context, databaseName: String) -> createDatabase(context, databaseName) }
}
