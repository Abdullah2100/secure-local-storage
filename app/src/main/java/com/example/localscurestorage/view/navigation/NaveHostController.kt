package com.example.localscurestorage.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.localscurestorage.modle.Screen
import com.example.localscurestorage.view.pages.Home
import com.example.localscurestorage.view.pages.StartPage

@Composable
fun RootNavHost(
    navcontroller:NavHostController
){
    NavHost(
        navController = navcontroller,
        startDestination =Screen.CreatePage
    ){
     composable<Screen.CreatePage>(){
         StartPage(navcontroller = navcontroller);
     }

        navigation<Screen.homeGraph>(
            startDestination =Screen.home
        ){
         composable<Screen.home>(){
             Home(navcontroller = navcontroller)
         }
        }
    }
}

