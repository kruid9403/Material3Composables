package com.jeremykruid.definedcomposables.textFields

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditCardTextField(
    card: MutableState<String>,
    textStyle: TextStyle,
    label: String = "Credit Card Number"
){

    Column(
        modifier = Modifier.fillMaxWidth()
    ){

        TextField(
            value = card.value,
            onValueChange = {
                if (it.length <= 16) {
                    card.value = it
                }
            },
            label = { Text(text = label) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .padding(top = 4.dp)
                .fillMaxWidth()
                .height(60.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
            ),
            textStyle = textStyle,
            maxLines = 1,
            visualTransformation = VisualTransformation { number ->
                when (identifyCardScheme(card.value)) {
                    CardScheme.AMEX -> formatAmex(number)
                    CardScheme.DINERS_CLUB -> formatDinnersClub(number)
                    else -> formatOtherCardNumbers(number)
                }
            },
        )
    }
}

enum class CardScheme {
    JCB, AMEX, DINERS_CLUB, VISA, MASTERCARD, DISCOVER, MAESTRO, UNKNOWN
}


fun identifyCardScheme(cardNumber: String): CardScheme {
    val jcbRegex = Regex("^(?:2131|1800|35)[0-9]{0,}$")
    val ameRegex = Regex("^3[47][0-9]{0,}\$")
    val dinersRegex = Regex("^3(?:0[0-59]{1}|[689])[0-9]{0,}\$")
    val visaRegex = Regex("^4[0-9]{0,}\$")
    val masterCardRegex = Regex("^(5[1-5]|222[1-9]|22[3-9]|2[3-6]|27[01]|2720)[0-9]{0,}\$")
    val maestroRegex = Regex("^(5[06789]|6)[0-9]{0,}\$")
    val discoverRegex =
        Regex("^(6011|65|64[4-9]|62212[6-9]|6221[3-9]|622[2-8]|6229[01]|62292[0-5])[0-9]{0,}\$")

    val trimmedCardNumber = cardNumber.replace(" ", "")

    return when {
        trimmedCardNumber.matches(jcbRegex) -> CardScheme.JCB
        trimmedCardNumber.matches(ameRegex) -> CardScheme.AMEX
        trimmedCardNumber.matches(dinersRegex) -> CardScheme.DINERS_CLUB
        trimmedCardNumber.matches(visaRegex) -> CardScheme.VISA
        trimmedCardNumber.matches(masterCardRegex) -> CardScheme.MASTERCARD
        trimmedCardNumber.matches(discoverRegex) -> CardScheme.DISCOVER
        trimmedCardNumber.matches(maestroRegex) -> if (cardNumber[0] == '5') CardScheme.MASTERCARD else CardScheme.MAESTRO
        else -> CardScheme.UNKNOWN
    }
}

fun formatAmex(text: AnnotatedString): TransformedText {
//
    val trimmed = if (text.text.length >= 15) text.text.substring(0..14) else text.text
    var out = ""

    for (i in trimmed.indices) {
        out += trimmed[i]
//        put - character at 3rd and 9th indicies
        if (i ==3 || i == 9 && i != 14) out += "-"
    }
//    original - 345678901234564
//    transformed - 3456-7890123-4564
//    xxxx-xxxxxx-xxxxx
    /**
     * The offset translator should ignore the hyphen characters, so conversion from
     *  original offset to transformed text works like
     *  - The 4th char of the original text is 5th char in the transformed text. (i.e original[4th] == transformed[5th]])
     *  - The 11th char of the original text is 13th char in the transformed text. (i.e original[11th] == transformed[13th])
     *  Similarly, the reverse conversion works like
     *  - The 5th char of the transformed text is 4th char in the original text. (i.e  transformed[5th] == original[4th] )
     *  - The 13th char of the transformed text is 11th char in the original text. (i.e transformed[13th] == original[11th])
     */
    val creditCardOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 3) return offset
            if (offset <= 9) return offset + 1
            if(offset <= 15) return offset + 2
            return 17
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 4) return offset
            if (offset <= 11) return offset - 1
            if(offset <= 17) return offset - 2
            return 15
        }
    }
    return TransformedText(AnnotatedString(out), creditCardOffsetTranslator)
}

fun formatDinnersClub(text: AnnotatedString): TransformedText {
    val trimmed = if (text.text.length >= 14) text.text.substring(0..13) else text.text
    var out = ""

    for (i in trimmed.indices) {
        out += trimmed[i]
        if (i ==3 || i == 9 && i != 13) out += "-"
    }

//    xxxx-xxxxxx-xxxx
    val creditCardOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 3) return offset
            if (offset <= 9) return offset + 1
            if(offset <= 14) return offset + 2
            return 16
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 4) return offset
            if (offset <= 11) return offset - 1
            if(offset <= 16) return offset - 2
            return 14
        }
    }
    return TransformedText(AnnotatedString(out), creditCardOffsetTranslator)
}

fun formatOtherCardNumbers(text: AnnotatedString): TransformedText {

    val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text
    var out = ""

    for (i in trimmed.indices) {
        out += trimmed[i]
        if (i % 4 == 3 && i != 15) out += "-"
    }
    val creditCardOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 3) return offset
            if (offset <= 7) return offset + 1
            if (offset <= 11) return offset + 2
            if (offset <= 16) return offset + 3
            return 19
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 4) return offset
            if (offset <= 9) return offset - 1
            if (offset <= 14) return offset - 2
            if (offset <= 19) return offset - 3
            return 16
        }
    }

    return TransformedText(AnnotatedString(out), creditCardOffsetTranslator)
}

@Preview
@Composable
fun CreditCardTextFieldPrev(){

}