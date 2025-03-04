package com.example.localscurestorage.view.pages

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import androidx.navigation.NavHostController
import com.example.localscurestorage.util.DataBaseHolder
import com.example.localscurestorage.util.enRoomOperationStatus
import com.example.localscurestorage.view.component.SnackBarBuilder
import com.example.localscurestorage.viewModle.FileDataViewModle
import org.koin.androidx.compose.koinViewModel


@Composable
fun Home(
    viewModel: FileDataViewModle = koinViewModel(),
    navcontroller: NavHostController
) {

    val context = LocalContext.current
    val activity = context as Activity
    val operationType = viewModel.operationFlow.collectAsState()
    val files = DataBaseHolder.localFiles.collectAsState()

    val isLoading = operationType.value == enRoomOperationStatus.LOADIN

    val pressCounterToCloseApp = remember { mutableStateOf(0) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        it->
        if (it != null) {
            viewModel.addNewFile(it,context)
        }
    }





    BackHandler(enabled = true//canGoBack.value
        , onBack = {
            pressCounterToCloseApp.value += 1;
            if (pressCounterToCloseApp.value == 1) {
                Toast.makeText(
                    context, "عند الضغط مرة اخرى سيتم اخراجك من التطبيق", Toast.LENGTH_LONG
                ).show()
            } else if (pressCounterToCloseApp.value == 2) {
                activity.finish()
            }
        })



    SnackBarBuilder(viewModel = viewModel) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green),
            content = { it ->
                it.calculateTopPadding()
                it.calculateBottomPadding()

                LazyColumn(modifier = Modifier.fillMaxSize()) {

                    when(files.value){
                        null->{
                            item {

                            }
                        }
                        else->{

                        }
                    }

                }


            }, floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        launcher.launch(
                            arrayOf(
                                "image/*",
                                "video/*",
                                "application/*"
                            )
                        )
                    }, content = {
                        if (isLoading)
                            CircularProgressIndicator(
                                color = Color.Black,
                                modifier = Modifier.size(25.dp),
                                strokeWidth = 2.dp
                            )
                        else
                        Image(Icons.Default.Add, contentDescription = "")
                    }
                )
            },
            floatingActionButtonPosition = FabPosition.Center
        )

    }
}