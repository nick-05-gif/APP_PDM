package com.example.app_iasi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.app_iasi.ui.theme.APP_PDMTheme
import kotlinx.serialization.Serializable

// Creamos los identificadores de cada pantalla para la navegacion
@Serializable
object Home

@Serializable
data class Details(val message: String)

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
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Controlador donde registramos las pantallas y sus transiciones
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier
    ) {
        // Primera pantalla: formulario
        composable<Home> {
            HomeScreen(
                onNavigate = { msg ->
                    navController.navigate(Details(message = msg))
                }
            )
        }
        // Segunda pantalla: detalle del mensaje
        composable<Details> { backStackEntry ->
            val details: Details = backStackEntry.toRoute()
            DetailsScreen(
                message = details.message,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    var textInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = textInput,
            onValueChange = { textInput = it },
            label = { Text("Escribe tu mensaje") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (textInput.isNotBlank()) {
                    onNavigate(textInput)
                    textInput = "" // vaciamos para que al volver este limpia
                }
            }
        ) {
            Text("Ver mensaje en nueva pantalla")
        }
    }
}

@Composable
fun DetailsScreen(message: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mensaje recibido:",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = message,
            fontSize = 24.sp,
            color = Color.Blue
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Boton para desapilar la pantalla y regresar
        Button(onClick = onBack) {
            Text("Volver")
        }
    }
}