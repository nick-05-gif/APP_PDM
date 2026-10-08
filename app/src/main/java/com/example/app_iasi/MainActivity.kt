package com.example.app_iasi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_iasi.ui.theme.APP_PDMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            APP_PDMTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFFE8F5E9)
                ) { innerPadding ->
                    // Llamamos a la nueva pantalla en vez de a Greeting
                    MessageScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MessageScreen(modifier: Modifier = Modifier) {
    var textInput by remember { mutableStateOf("") }
    var toDisplay by remember { mutableStateOf("") }

    Column(
        modifier = modifier.padding(16.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = textInput,
            onValueChange = { textInput = it },
            label = { Text("Escribe tu mensaje") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (textInput.isNotBlank()) {
                    toDisplay += textInput + "\n"
                    textInput = ""
                }
            }
        ) {
            Text("Añadir texto")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { toDisplay = "" }
        ) {
            Text("Borrar historial")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = toDisplay,
            modifier = Modifier
                .height(200.dp)
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    APP_PDMTheme {
        // En la previsualizacion tambien llamamos a la nueva
        MessageScreen()
    }
}