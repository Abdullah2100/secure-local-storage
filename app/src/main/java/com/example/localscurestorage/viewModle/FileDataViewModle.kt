package com.example.localscurestorage.viewModle

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.compose.ui.util.fastForEach
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.room.Room
import com.example.localscurestorage.DI.databaseModule
import com.example.localscurestorage.modle.FileData
import com.example.localscurestorage.modle.Screen
import com.example.localscurestorage.servuces.FileDataDatabase
import com.example.localscurestorage.util.DataBaseHolder
import com.example.localscurestorage.util.General
import com.example.localscurestorage.util.enRoomOperationStatus
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar


class FileDataViewModle() : ViewModel() {


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
            ).openHelperFactory(General.encryptionFactory(databaseName))
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

    fun createDatabas(context: Context, databaseName: String, navContoller: NavHostController) {

        val result = validationInput(databaseName)
        if (result == false) {
            return;
        }
        viewModelScope.launch(Dispatchers.Main + errorHandler)
        {

            operationFlow.value = enRoomOperationStatus.LOADIN

            delay(2000L)

            val databaseNameGen = generateHashDatabaseName(context, databaseName);

            val dataBaseHolder = createDatabase(context, databaseNameGen)

            DataBaseHolder.fileDataBaseHolder.emit(dataBaseHolder)

            getLocalFile()

            context.getDatabasePath(databaseNameGen).absolutePath

            operationFlow.value = enRoomOperationStatus.COMPLATIN

            navContoller.navigate(Screen.homeGraph) { popUpTo(0) }

        }
    }

    fun clearErrorMessage() {
        viewModelScope.launch {
            error.emit("")
        }
    }


    fun addNewFile(fileString: Uri, ocntext: Context) {
        viewModelScope.launch(Dispatchers.IO + errorHandler)
        {
            operationFlow.emit(enRoomOperationStatus.LOADIN)


            val fileInfoArray = General.fileInfoAndByteFromUri(ocntext, fileString)

            val fileName = fileInfoArray[0] as String;
            val minFile = fileInfoArray[1] as String;
            val fileSize = fileInfoArray[2] as Long
            val fileByte = fileInfoArray[3] as ByteArray;

            val fileDataHolder = FileData(
                null,
                fileByte,
                mintype = minFile,
                createdAt = Calendar.getInstance().time.toString(),
                name = fileName,
                size = fileSize,
            )

            DataBaseHolder.fileDataBaseHolder.value?.fileDo()?.addNewFile(fileDataHolder)

            operationFlow.emit(enRoomOperationStatus.COMPLATIN)

            getLocalFile()

        }
    }

    fun getLocalFile() {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            operationFlow.value = enRoomOperationStatus.LOADIN

            val files = DataBaseHolder
                .fileDataBaseHolder
                .value?.fileDo()?.getFileDataByPaggination(
                    pageNumber = DataBaseHolder.pageNumber.value, numberOfFile = 24
                )
            when (DataBaseHolder.localFiles.value) {
                null -> DataBaseHolder.localFiles.emit(files as MutableList<FileData>?)
                else -> {
                    if (files != null) {
                        DataBaseHolder.localFiles.value!!.addAll(files)
                    }
                }
            }



            operationFlow.value = enRoomOperationStatus.COMPLATIN
        }
    }

}