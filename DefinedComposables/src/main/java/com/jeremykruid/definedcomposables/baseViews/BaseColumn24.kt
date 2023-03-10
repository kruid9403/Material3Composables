package com.jeremykruid.definedcomposables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BaseColumn24(
    backgroundColor: Color,
    padding: Int = 24,
    content: @Composable ()->Unit,
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding.dp)
        ){
            content.invoke()
        }

    }
}

@Preview
@Composable
fun BaseColumn24Prev(){
    MaterialTheme {
        BaseColumn24(
            content = {Text(text = "some Text")},
            backgroundColor = Color.Black
        )
    }
}