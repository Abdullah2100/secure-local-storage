package com.example.localscurestorage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.localscurestorage.ui.theme.LocalScureStorageTheme
import com.example.localscurestorage.view.StartPage
import com.example.localscurestorage.viewModle.FileDataViewModle
import org.koin.androidx.viewmodel.ext.android.viewModel
class MainActivity : ComponentActivity() {
    private val viewModel: FileDataViewModle by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocalScureStorageTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    innerPadding.calculateTopPadding()
                    innerPadding.calculateBottomPadding()

                    StartPage()
                }
            }
        }
    }
}
