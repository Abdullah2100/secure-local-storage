package com.example.localscurestorage.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable()
@Preview()
fun StartPage(){
    var databaseNam = remember { mutableStateOf("ادخل اسم قاعدة البيانات") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            onValueChange = {
                databaseNam.value =it
            },
            value = databaseNam.value
                ,
            shape = RoundedCornerShape(7.dp)

        )
        Button(
            onClick = { /* Handle click */ },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(start = 49.dp, end = 49.dp, top = 5.dp)
                .fillMaxWidth(1f)

        ) {
            Text("تسحيل".uppercase(), fontWeight = FontWeight.Bold, color = Color.White)
        }
    }

}