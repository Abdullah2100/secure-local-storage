package com.example.localscurestorage.viewModle

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.localscurestorage.modle.FileData
import com.example.localscurestorage.servuces.FileDataDatabase
import com.example.localscurestorage.util.enRoomOperationStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.security.MessageDigest


class FileDataViewModle( val createDatabase:(context: Context, databaseName:String)->FileDataDatabase):ViewModel() {


    private var fileDataBaseHolder:FileDataDatabase?=null;
    var roomOperationFlow= MutableStateFlow<enRoomOperationStatus>(enRoomOperationStatus.NOTHING);
    var error = MutableStateFlow<String?>(null);


    private fun String.toSHA256(): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(this.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) } // Convert to Hex String
    }


    @SuppressLint("HardwareIds")
    private  fun generateHashDatabaseName(context: Context,databaseName:String): String {
         val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
         val databaseNameHolder =deviceId+databaseName;
         val databaseNameHash = databaseNameHolder.toSHA256()+".db"
         return databaseNameHash
     }

     fun createDatabas(context: Context,databaseName: String){
           try {
               Log.d("createDatabase","start")
               roomOperationFlow.update { enRoomOperationStatus.LOADIN }
               val databaseNameGen = generateHashDatabaseName(context,databaseName);
               fileDataBaseHolder= createDatabase(context,databaseNameGen)
               val databasePath = context.getDatabasePath(databaseNameGen).absolutePath
               Log.d("createDatabase","conplate  ${databasePath}")
               CoroutineScope(Dispatchers.IO).launch {
                   fileDataBaseHolder?.fileDo()?.addNewFile(FileData(null,null,null,null))
               }
               roomOperationFlow.update { enRoomOperationStatus.COMPLATIN }
           }catch (e:Exception) {
               roomOperationFlow.update { enRoomOperationStatus.ERROR }
               error.update { e.message }

           }
         finally {
             Log.d("createDatabase","complate")

         }

        }

}