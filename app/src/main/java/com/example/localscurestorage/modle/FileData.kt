package com.example.localscurestorage.modle

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Date


@Entity(tableName = "files")
data class FileData(
  @PrimaryKey val id:Int? = null,
    var name:String="",
    var data:ByteArray,
    var createdAt:String?  = Calendar.getInstance().getTime().toString(),
);