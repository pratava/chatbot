package com.ai.chatbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ai.chatbot.ui.theme.ChatBotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatBotTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChatBotScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ChatBotScreen(modifier: Modifier = Modifier) {
    val models = listOf("gpt-3.5-turbo", "gpt-4")
    var expanded by remember { mutableStateOf(false) }
    var selectedModel by remember { mutableStateOf(models.first()) }
    var selectedPdfName by remember { mutableStateOf<String?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        selectedPdfName = uri?.lastPathSegment
    }

    Column(modifier = modifier.padding(16.dp)) {
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            TextField(
                value = selectedModel,
                onValueChange = {},
                readOnly = true,
                label = { Text("Model") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                models.forEach { model ->
                    DropdownMenuItem(text = { Text(model) }, onClick = {
                        selectedModel = model
                        expanded = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { launcher.launch(arrayOf("application/pdf")) }) {
            Text("Choose PDF")
        }

        selectedPdfName?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Selected: $it")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatBotPreview() {
    ChatBotTheme {
        ChatBotScreen()
    }
}