package com.example.app_permission_manager

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.app_permission_manager.annotation.Permission
import com.example.app_permission_manager.ui.theme.ApppermissionmanagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApppermissionmanagerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PermissionRequestScreen(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PermissionRequestScreen(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(modifier = modifier.fillMaxSize()) {
            Button(onClick = {
                getPermission(context)
            }) {
                Text(text = "Button 1")
            }
            Button(onClick = { /* TODO: Handle button 2 click */ }) {
                Text(text = "Button 2")
            }
        }
}

@Permission(value = ["android.permission.CAMERA"], requestCode = 100)
fun getPermission(context: Context) {
    TODO("Not yet implemented")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ApppermissionmanagerTheme {
        PermissionRequestScreen("Android")
    }
}