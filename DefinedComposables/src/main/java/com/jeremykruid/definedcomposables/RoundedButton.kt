package com.jeremykruid.definedcomposables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RoundedButton(
    height: Dp = 50.dp,
    backgroundColor: Color = Color.Red,
    text: String = "Login",
    textStyle: TextStyle = TextStyle(
        color = Color.White,
        textAlign = TextAlign.Center,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
        ),
    textMod: Modifier = Modifier,
    modifier: Modifier = Modifier
        .clickable { click.invoke() }
        .padding(top = 16.dp)
        .fillMaxWidth()
        .height(height)
        .background(color = backgroundColor, shape = RoundedCornerShape(50))
        .clip(RoundedCornerShape(50)),
    click: ()->Unit
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            style = textStyle,
            modifier = textMod,
        )
    }
}

@Preview
@Composable
fun RoundedButtonPrev(){
    MaterialTheme {
        RoundedButton(
            textStyle = TextStyle(),
            text = "Login",
        ){

        }
    }
}