package com.example.material3composables

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.material3composables.ui.theme.Material3ComposablesTheme
import com.jeremykruid.definedcomposables.BaseColumn24
import com.jeremykruid.definedcomposables.RoundedButton
import com.jeremykruid.definedcomposables.camera.CameraView
import com.jeremykruid.definedcomposables.textFields.CreditCardTextField
import com.jeremykruid.definedcomposables.textFields.DollarTextField
import com.jeremykruid.definedcomposables.textFields.NameTextField

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Material3ComposablesTheme {
                val context = LocalContext.current
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
//                        CameraView(context = context, onImageCaptured = { uri, b -> }, onError = {})
                        RoundedButton(
                            text = "Submit"
                        ) {
                            Toast.makeText(context, "Toast", Toast.LENGTH_SHORT).show()
                        }
                        DollarTextField(
                            number = remember { mutableStateOf("") },
                            textStyle = TextStyle(),
                            colorScheme = MaterialTheme.colorScheme
                        )
                        NameTextField(
                            name = remember { mutableStateOf("") },
                            label = "First Name",
                            textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
                            colorScheme = MaterialTheme.colorScheme
                        )

                        CreditCardTextField(
                            card = remember { mutableStateOf("") },
                            textStyle = TextStyle(),
                            colorScheme  = MaterialTheme.colorScheme,
                            textFieldColors = TextFieldDefaults.textFieldColors(
                                textColor = MaterialTheme.colorScheme.primary,
                                focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                unfocusedLabelColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Material3ComposablesTheme {
        Greeting("Android")
    }
}