package com.example.localscurestorage.viewModle

import android.R.attr.password
import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.lifecycle.ViewModel
import com.example.localscurestorage.servuces.FileDataDatabase
import com.example.localscurestorage.util.enRoomOperationStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.mindrot.jbcrypt.BCrypt
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
               roomOperationFlow.update { enRoomOperationStatus.LOADIN }
               fileDataBaseHolder= createDatabase(context,generateHashDatabaseName(context,databaseName))
               roomOperationFlow.update { enRoomOperationStatus.COMPLATIN }
           }catch (e:Exception) {
               roomOperationFlow.update { enRoomOperationStatus.ERROR }
               error.update { e.message }
           }

        }







}