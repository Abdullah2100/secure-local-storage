package com.example.localscurestorage.viewModle

import android.content.Context
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



     fun createDatabase(context: Context){
         
           try {
               roomOperationFlow.update { enRoomOperationStatus.LOADIN }
               createDatabase(context)
               roomOperationFlow.update { enRoomOperationStatus.COMPLATIN }
           }catch (e:Exception) {
               roomOperationFlow.update { enRoomOperationStatus.ERROR }
               error.update { e.message }
           }

        }







}