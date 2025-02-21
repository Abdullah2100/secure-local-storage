package com.example.localscurestorage.servuces

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.localscurestorage.modle.FileData
import kotlinx.coroutines.flow.Flow

interface FileDataDao {

    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun addNewFile(fileData:FileData)

    @Query("SELECT * FROM files ORDER BY createdAt DESC LIMIT :numberOfFile OFFSET :pageNumber")
    suspend fun getFileDataByPaggination(numberOfFile:Int,pageNumber:Int):Flow<List<FileData>>
}