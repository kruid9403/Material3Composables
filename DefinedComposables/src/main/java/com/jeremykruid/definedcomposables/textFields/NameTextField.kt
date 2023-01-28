package com.jeremykruid.definedcomposables.textFields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameTextField(
    name: MutableState<String>,
    label: String,
    textStyle: TextStyle
){

    TextField(
        value = name.value,
        onValueChange = {
            name.value = it

        },
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            capitalization = KeyboardCapitalization.Words
        ),
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .padding(top = 4.dp)
            .fillMaxWidth()
            .height(60.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colorScheme.onPrimary,
        ),
        textStyle = textStyle,
        maxLines = 1,
    )

}

@Preview
@Composable
fun NameTextFieldPrev(){
    MaterialTheme {
        NameTextField(
            name = remember { mutableStateOf("") },
            textStyle = TextStyle(),
            label = "Name"
        )
    }
}