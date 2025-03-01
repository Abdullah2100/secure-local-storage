package com.example.localscurestorage.view.pages

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.localscurestorage.util.General.Companion.canGoBAckToStartScreen
import com.example.localscurestorage.view.component.SnackBarBuilder
import com.example.localscurestorage.viewModle.FileDataViewModle
import org.koin.androidx.compose.koinViewModel


@Composable
fun Home(
    viewModel: FileDataViewModle = koinViewModel(),
         navcontroller: NavHostController
){
   val context = LocalContext.current
    val activity = context as Activity
    val pressCounter = remember { mutableStateOf(0) }
    BackHandler(enabled = true//canGoBack.value
        , onBack = {
        Log.d("backPress","this shown the back button is prpess ${pressCounter.value}")
            pressCounter.value+=1;
            if(pressCounter.value==1){
                Toast.makeText(
                    context,"عند الضغط مرة اخرى سيتم اخراجك من التطبيق"
                    ,Toast.LENGTH_LONG
                ).show()
            }
            else if (pressCounter.value==2){
                activity.finish()
            }
    })



    SnackBarBuilder(viewModel = viewModel) {
        Scaffold(
            modifier = Modifier.fillMaxSize().background(Color.Green)
        ) { it ->
            it.calculateTopPadding()
            it.calculateBottomPadding()


        }
    }
}