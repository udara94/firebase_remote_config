package com.example.firebaseremoteconfig

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.firebaseremoteconfig.ui.theme.FirebaseremoteconfigTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var remoteConfigManager: RemoteConfigManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Remote Config Manager with context
        remoteConfigManager = RemoteConfigManager(this)

        // Fetch Remote Config values
        lifecycleScope.launch {
            remoteConfigManager.fetchAndActivate()
        }

        setContent {
            FirebaseremoteconfigTheme {
                var isRefreshing by remember { mutableStateOf(false) }
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                isRefreshing = true
                                kotlinx.coroutines.GlobalScope.launch {
                                    try {
                                        val result = remoteConfigManager.fetchAndActivate()
                                        Log.d("MainActivity", "Refresh result: $result")
                                    } catch (e: Exception) {
                                        Log.e("MainActivity", "Error refreshing config", e)
                                    } finally {
                                        isRefreshing = false
                                    }
                                }
                            },
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            if (isRefreshing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = androidx.compose.ui.graphics.Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = "Refresh Remote Config",
                                    tint = androidx.compose.ui.graphics.Color.White
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    RemoteConfigShowcase(
                        remoteConfigManager = remoteConfigManager,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun RemoteConfigScreen(
    remoteConfigManager: RemoteConfigManager,
    modifier: Modifier = Modifier
) {
    var isRefreshing by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Firebase Remote Config Demo",
            style = MaterialTheme.typography.headlineMedium
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Welcome Message:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = remoteConfigManager.getString(RemoteConfigManager.WELCOME_MESSAGE_KEY),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Feature Flag:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = if (remoteConfigManager.getBoolean(RemoteConfigManager.FEATURE_FLAG_KEY))
                        "Enabled" else "Disabled",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "API URL:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = remoteConfigManager.getString(RemoteConfigManager.API_URL_KEY),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Max Retry Attempts:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = remoteConfigManager.getLong(RemoteConfigManager.MAX_RETRY_ATTEMPTS_KEY).toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Cache Duration (hours):",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = remoteConfigManager.getLong(RemoteConfigManager.CACHE_DURATION_HOURS_KEY).toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Button(
            onClick = {
                isRefreshing = true
                // Use GlobalScope for now, or implement proper coroutine scope
                kotlinx.coroutines.GlobalScope.launch {
                    try {
                        val result = remoteConfigManager.fetchAndActivate()
                        Log.d("MainActivity", "Refresh result: $result")
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error refreshing config", e)
                    } finally {
                        isRefreshing = false
                    }
                }
            },
            enabled = !isRefreshing
        ) {
            if (isRefreshing) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text("Refresh Config")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RemoteConfigScreenPreview() {
    FirebaseremoteconfigTheme {
        // Preview with mock data
        RemoteConfigScreen(
            remoteConfigManager = object {
                fun getString(key: String): String = "Preview Message"
                fun getBoolean(key: String): Boolean = true
            } as RemoteConfigManager
        )
    }
}