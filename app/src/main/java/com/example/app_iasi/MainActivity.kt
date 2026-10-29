package com.example.app_iasi

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app_iasi.ui.theme.APP_PDMTheme
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Details

class MessagesViewModel : ViewModel() {
    val messages = mutableStateListOf<String>()

    fun addMessage(message: String) {
        if (message.isNotBlank()) {
            messages.add(message)
        }
    }

    fun clearHistory() {
        messages.clear()
    }
}

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
                    AdaptiveApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AdaptiveApp(
    modifier: Modifier = Modifier,
    viewModel: MessagesViewModel = viewModel()
) {
    val configuration = LocalConfiguration.current
    // rememberSaveable mantiene el texto que se esta escribiendo al girar la pantalla
    var textInput by rememberSaveable { mutableStateOf("") }

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        // En horizontal mostramos entrada e historial en paralelo
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputPanel(
                textInput = textInput,
                onTextInputChange = { textInput = it },
                onSend = {
                    viewModel.addMessage(textInput)
                    textInput = ""
                },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            HistoryPanel(
                messages = viewModel.messages,
                onClearHistory = { viewModel.clearHistory() },
                showBackButton = false,
                onBack = {},
                modifier = Modifier.weight(1f)
            )
        }
    } else {
        // En vertical seguimos con la navegacion de 2 pantallas
        AppNavigation(
            viewModel = viewModel,
            textInput = textInput,
            onTextInputChange = { textInput = it },
            modifier = modifier
        )
    }
}

@Composable
fun AppNavigation(
    viewModel: MessagesViewModel,
    textInput: String,
    onTextInputChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier
    ) {
        composable<Home> {
            HomeScreen(
                textInput = textInput,
                onTextInputChange = onTextInputChange,
                onNavigateToDetails = {
                    viewModel.addMessage(textInput)
                    onTextInputChange("")
                    navController.navigate(Details)
                }
            )
        }
        composable<Details> {
            DetailsScreen(
                messages = viewModel.messages,
                onClearHistory = { viewModel.clearHistory() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun HomeScreen(
    textInput: String,
    onTextInputChange: (String) -> Unit,
    onNavigateToDetails: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InputPanel(
            textInput = textInput,
            onTextInputChange = onTextInputChange,
            onSend = onNavigateToDetails
        )
    }
}

@Composable
fun DetailsScreen(
    messages: List<String>,
    onClearHistory: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HistoryPanel(
            messages = messages,
            onClearHistory = onClearHistory,
            showBackButton = true,
            onBack = onBack
        )
    }
}

@Composable
fun InputPanel(
    textInput: String,
    onTextInputChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = textInput,
            onValueChange = onTextInputChange,
            label = { Text("Enter your message") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (textInput.isNotBlank()) {
                    onSend()
                }
            }
        ) {
            Text("Send & View History")
        }
    }
}

@Composable
fun HistoryPanel(
    messages: List<String>,
    onClearHistory: () -> Unit,
    showBackButton: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Message History:",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column(
            modifier = Modifier
                .height(160.dp)
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (messages.isEmpty()) {
                Text(text = "No messages yet", color = Color.Gray)
            } else {
                messages.forEach { msg ->
                    Text(
                        text = msg,
                        fontSize = 18.sp,
                        color = Color.Blue,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onClearHistory) {
            Text("Clear History")
        }

        if (showBackButton) {
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onBack) {
                Text("Back")
            }
        }
    }
}