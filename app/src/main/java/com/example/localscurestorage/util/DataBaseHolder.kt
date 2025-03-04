package com.example.localscurestorage.util

import com.example.localscurestorage.modle.FileData
import com.example.localscurestorage.servuces.FileDataDatabase
import kotlinx.coroutines.flow.MutableStateFlow

class DataBaseHolder {
    companion object {
        var fileDataBaseHolder = MutableStateFlow<FileDataDatabase?>(null)
        var localFiles = MutableStateFlow<MutableList<FileData>?>(null)
        var pageNumber = MutableStateFlow<Int>(1)

    }
}