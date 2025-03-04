package com.example.localscurestorage.view.pages

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.localscurestorage.util.enRoomOperationStatus
import com.example.localscurestorage.viewModle.FileDataViewModle
import org.koin.androidx.compose.koinViewModel
import androidx.navigation.NavHostController
import com.example.localscurestorage.view.component.SnackBarBuilder


@Composable()
fun StartPage(
    viewModel: FileDataViewModle = koinViewModel(),
    navcontroller: NavHostController
) {
    val databaseNam = remember { mutableStateOf("") }
    val context = LocalContext.current;
    val status = viewModel.operationFlow.collectAsState();
    val isLoading = status.value == enRoomOperationStatus.LOADIN
    val errorMessage = viewModel.error.collectAsState();
    val snackbarHostState = remember { SnackbarHostState() }

//    Log.d("thisError","${errorMessage.value}")

    SnackBarBuilder(viewModel = viewModel) {
        Scaffold(
        )
        { it ->
            it.calculateTopPadding()
            it.calculateBottomPadding()


            Column(
                modifier = Modifier
                    .fillMaxSize(),

                horizontalAlignment = Alignment
                    .CenterHorizontally,
                verticalArrangement = Arrangement
                    .Center
            ) {
                OutlinedTextField(
                    onValueChange = { textValue ->
                        databaseNam.value = textValue
                    },
                    value = databaseNam.value,
                    shape = RoundedCornerShape(25.dp), label = {
                        Text("ادخل اسم قاعدة البيانات")
                    }
                )
                Button(
                    enabled = !isLoading,
                    onClick = { viewModel.createDatabas(context, databaseNam.value,navcontroller) },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .padding(start = 64.dp, end = 64.dp, top = 5.dp)
                        .fillMaxWidth(1f)
                ) {
                    if (isLoading)
                        CircularProgressIndicator(
                            color = Color.Blue,
                            modifier = Modifier.size(25.dp),
                            strokeWidth = 2.dp
                        )
                    else
                        Text("تسحيل".uppercase(), fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

        }
    }

}