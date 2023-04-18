package com.jeremykruid.definedcomposables.textFields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameTextField(
    name: MutableState<String>,
    textStyle: TextStyle,
    label: String = "Credit Card Number",
    colorScheme: ColorScheme,
    modifier: Modifier = Modifier
        .padding(top = 4.dp)
        .fillMaxWidth()
        .height(60.dp)
        .border(width = 1.dp, color = colorScheme.onBackground, shape = RoundedCornerShape(50))
        .clip(shape = RoundedCornerShape(50)),
    labelMod: Modifier = Modifier,
    labelStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next,
        capitalization = KeyboardCapitalization.Words
    ),
    keyboardActions: KeyboardActions = KeyboardActions(
        onNext = {}
    ),
    textFieldColors: TextFieldColors = TextFieldDefaults.textFieldColors(
        textColor = Color.Black,
    ),
    maxLines: Int = 1
){

    TextField(
        value = name.value,
        onValueChange = {
            name.value = it

        },
        label = { Text(
            text = label,
            modifier = labelMod,
            style = labelStyle
        ) },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier,
        colors = textFieldColors,
        textStyle = textStyle,
        maxLines = maxLines,
    )

}

@Preview
@Composable
fun NameTextFieldPrev(){
    MaterialTheme {
        NameTextField(
            name = remember { mutableStateOf("") },
            textStyle = TextStyle(),
            label = "Name",
            colorScheme = MaterialTheme.colorScheme
        )
    }
}