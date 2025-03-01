package com.example.localscurestorage.viewModle

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.room.Room
import com.example.localscurestorage.modle.FileData
import com.example.localscurestorage.modle.Screen
import com.example.localscurestorage.modle.enErrorType
import com.example.localscurestorage.servuces.FileDataDatabase
import com.example.localscurestorage.util.General
import com.example.localscurestorage.util.enRoomOperationStatus
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.security.MessageDigest

class FileDataViewModle() : ViewModel() {

    private var fileDataBaseHolder: FileDataDatabase? = null;

    var operationFlow = MutableStateFlow<enRoomOperationStatus>(enRoomOperationStatus.NOTHING);
    var error = MutableStateFlow<String?>(null);
    var canNvigateFromHomeToStartScreen = MutableStateFlow(false)

    private var errorHandler = CoroutineExceptionHandler { _, exption ->
        viewModelScope.launch {
            error.emit(null)
            operationFlow.emit(enRoomOperationStatus.ERROR)
            error.emit(exption.message)
        }
    }


    @SuppressLint("HardwareIds")
    private fun generateHashDatabaseName(context: Context, databaseName: String): String {
        val deviceId =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val databaseNameHolder = deviceId + databaseName;
        val databaseNameHash = General.hashString(databaseNameHolder) + ".db"
        return databaseNameHash
    }

    private fun createDatabase(context: Context, databaseName: String): FileDataDatabase {
        return try {
            Room.databaseBuilder(
                context.applicationContext,
                FileDataDatabase::class.java, // ✅ Corrected to use actual Database class
                databaseName
            ).openHelperFactory(General.encriptionFactory(databaseName))
                .fallbackToDestructiveMigration()
                .build()
        } catch (e: Exception) {
            Log.e("DatabaseError", "Error creating database: ${e.message}")
            throw e
        }
    }

    fun validationInput(name: String): Boolean {
        if (name.length < 1) {
            viewModelScope.launch {
                error.emit("")
                delay(1000)
                operationFlow.emit(enRoomOperationStatus.INFO)
                error.emit("name must not be null")
            }
            return false;
        }
        return true;
    }

    fun createDatabas(context: Context, databaseName: String,navContoller: NavHostController) {

        val result = validationInput(databaseName)
        if (result==false) {
            return;
        }
        viewModelScope.launch(Dispatchers.Main + errorHandler)
        {
//            throw Exception("this just text")

            operationFlow.value = enRoomOperationStatus.LOADIN

            delay(2000L)

            val databaseNameGen = generateHashDatabaseName(context, databaseName);

            fileDataBaseHolder = createDatabase(context, databaseNameGen)

            context.getDatabasePath(databaseNameGen).absolutePath

            operationFlow.value = enRoomOperationStatus.COMPLATIN
            navContoller.navigate(Screen.homeGraph){
                popUpTo(0)
            }
        }

    }


    fun clearErrorMessage() {
        viewModelScope.launch {
            error.emit( "")
        }
    }
}