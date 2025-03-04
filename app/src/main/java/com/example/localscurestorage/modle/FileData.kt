package com.example.localscurestorage.modle

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar


@Entity(tableName = "files")
data class FileData(
  @PrimaryKey(autoGenerate = true) val id: Int? = 0, // Auto-increment ID  var name:String="",
  var data:ByteArray?=null,
  var createdAt:String?  = Calendar.getInstance().time.toString(),
  var mintype:String?=null,
  var name:String?=null,
  var size:Long? = null
);