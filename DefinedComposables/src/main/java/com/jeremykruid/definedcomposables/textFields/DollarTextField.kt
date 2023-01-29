package com.jeremykruid.definedcomposables.textFields

import androidx.compose.foundation.border
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.substring
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeremykruid.definedcomposables.convertDollarsCents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DollarTextField(
    number: MutableState<String>,
    textStyle: TextStyle,
    label: String = "Price",
    colorScheme: ColorScheme,
    modifier: Modifier = Modifier
        .padding(horizontal = 32.dp)
        .padding(top = 4.dp)
        .fillMaxWidth()
        .height(60.dp)
        .border(width = 1.dp, color = colorScheme.onPrimary, shape = RoundedCornerShape(50))
        .clip(shape = RoundedCornerShape(50)),
    labelMod: Modifier = Modifier
        .fillMaxWidth(),
    labelStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        textAlign = TextAlign.Center
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
    keyboardType = KeyboardType.Decimal,
    imeAction = ImeAction.Next
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
        value = number.value,
        onValueChange = {
            val count = it.split(".").size
            var secLen = 0

            if (count > 1){
                secLen = it.split(".")[1].length
            }
            if (count < 3 && secLen < 3){
                number.value = it
            }
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
        visualTransformation = VisualTransformation {
            val s = if (it.isNotEmpty()){
                AnnotatedString(it.text.toDouble().convertDollarsCents())
            }else{
                AnnotatedString(0.0.convertDollarsCents())
            }
            val split = s.text.split(",").size
            TransformedText(s, object: OffsetMapping{
                override fun originalToTransformed(offset: Int): Int {
                    var offset = 0
                    when(true){
                        (it.isEmpty())->  {
                            offset = 0
                        }
                        (it.isNotEmpty())->{
                            offset = it.length + split - 1
                        }
                        else->{

                        }
                    }
                    return offset + 1
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return offset
                }

            })
        }
    )
}

fun formatDollars(text: AnnotatedString): TransformedText {
//
//    val trimmed = if (text.text.length >= 15) text.text.substring(0..14) else text.text
    var out = ""

//    for (i in trimmed.indices) {
//        out += trimmed[i]
////        put - character at 3rd and 9th indicies
//        if (i ==3 || i == 9 && i != 14) out += "-"
//    }

    when(true){
        (text.text.isEmpty())->{
            "$0.00"
        }
        (text.text.length == 1)->{
            out = "$0.0${text.text}"
        }
        (text.text.length == 2)->{
            out = "$0.${text.text}"
        }
        else -> {
            val first = text.text.substring(0, text.text.length - 2)
            val lastTwo = text.text.takeLast(2)
            out = "$$first.$lastTwo"
        }
    }


    val dollarTransform = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return offset
        }

        override fun transformedToOriginal(offset: Int): Int {
            return offset
        }
    }
    return TransformedText(AnnotatedString(out), dollarTransform)
}

@Preview
@Composable
fun DollarTextFieldPref(){
    MaterialTheme {
        DollarTextField(
            number = remember { mutableStateOf("") },
            textStyle = TextStyle(),
            colorScheme = MaterialTheme.colorScheme
        )
    }
}
