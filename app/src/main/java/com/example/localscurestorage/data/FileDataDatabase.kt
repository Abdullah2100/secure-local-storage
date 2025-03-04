package com.example.localscurestorage.servuces

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.localscurestorage.modle.FileData

@Database(entities = [FileData::class], version = 1, exportSchema = false)
abstract class FileDataDatabase:RoomDatabase() {
    abstract fun fileDo():FileDataDao
}