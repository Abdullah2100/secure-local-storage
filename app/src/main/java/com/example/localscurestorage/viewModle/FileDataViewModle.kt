package com.example.localscurestorage.viewModle

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.localscurestorage.servuces.FileDataDatabase
import com.example.localscurestorage.util.enRoomOperationStatus
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FileDataViewModle( val createDatabase:(context: Context, databaseName:String)->FileDataDatabase):ViewModel() {


    private var fileDataBaseHolder:FileDataDatabase?=null;
    var roomOperationFlow= MutableStateFlow<enRoomOperationStatus>(enRoomOperationStatus.NOTHING);
    var error = MutableStateFlow<String?>(null);


     @SuppressLint("HardwareIds")
     fun generateHashDatabaseName(context: Context): String {
         val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
         return deviceId.toString()
     }

     fun createDatabas(context: Context){
           try {
               roomOperationFlow.update { enRoomOperationStatus.LOADIN }
               fileDataBaseHolder= createDatabase(context,generateHashDatabaseName(context))
               roomOperationFlow.update { enRoomOperationStatus.COMPLATIN }
           }catch (e:Exception) {
               roomOperationFlow.update { enRoomOperationStatus.ERROR }
               error.update { e.message }
           }

        }







}