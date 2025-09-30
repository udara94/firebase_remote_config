package com.example.firebaseremoteconfig

import android.graphics.Color
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firebaseremoteconfig.ui.theme.FirebaseremoteconfigTheme
import kotlinx.coroutines.launch

@Composable
fun RemoteConfigShowcase(
    remoteConfigManager: RemoteConfigManager,
    modifier: Modifier = Modifier
) {
    val primaryColor = remember { 
        try {
            ComposeColor(android.graphics.Color.parseColor(remoteConfigManager.getString(RemoteConfigManager.PRIMARY_COLOR_KEY)))
        } catch (e: Exception) {
            ComposeColor(0xFF2196F3)
        }
    }
    
    val showEmergency = remoteConfigManager.getString(RemoteConfigManager.EMERGENCY_MESSAGE_KEY).isNotEmpty()
    val showAnnouncement = remoteConfigManager.getBoolean(RemoteConfigManager.SHOW_ANNOUNCEMENT_KEY)
    val maintenanceMode = remoteConfigManager.getBoolean(RemoteConfigManager.MAINTENANCE_MODE_KEY)
    val showPremium = remoteConfigManager.getBoolean(RemoteConfigManager.SHOW_PREMIUM_FEATURES_KEY)
    val enableAnimations = remoteConfigManager.getBoolean(RemoteConfigManager.ENABLE_ANIMATIONS_KEY)
    val discountPercentage = remoteConfigManager.getLong(RemoteConfigManager.DISCOUNT_PERCENTAGE_KEY)
    
    // Animation for dynamic content
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = primaryColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🚀 Firebase Remote Config",
                    style = MaterialTheme.typography.headlineLarge,
                    color = ComposeColor.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Live Demo Showcase",
                    style = MaterialTheme.typography.titleMedium,
                    color = ComposeColor.White.copy(alpha = 0.9f)
                )
            }
        }

        // Emergency Banner
        if (showEmergency) {
            EmergencyBanner(
                message = remoteConfigManager.getString(RemoteConfigManager.EMERGENCY_MESSAGE_KEY),
                enableAnimations = enableAnimations
            )
        }

        // Maintenance Mode
        if (maintenanceMode) {
            MaintenanceBanner(enableAnimations = enableAnimations)
        }

        // Announcement Banner
        if (showAnnouncement) {
            AnnouncementBanner(
                text = remoteConfigManager.getString(RemoteConfigManager.ANNOUNCEMENT_TEXT_KEY),
                enableAnimations = enableAnimations
            )
        }

        // Dynamic Theme Showcase
        DynamicThemeCard(
            primaryColor = primaryColor,
            theme = remoteConfigManager.getString(RemoteConfigManager.APP_THEME_KEY),
            enableAnimations = enableAnimations
        )

        // A/B Testing Demo
        ABTestingCard(
            buttonStyle = remoteConfigManager.getString(RemoteConfigManager.BUTTON_STYLE_KEY),
            onboardingVariant = remoteConfigManager.getString(RemoteConfigManager.ONBOARDING_VARIANT_KEY),
            checkoutFlow = remoteConfigManager.getString(RemoteConfigManager.CHECKOUT_FLOW_KEY),
            enableAnimations = enableAnimations
        )

        // Feature Flags Demo
        FeatureFlagsCard(
            showPremium = showPremium,
            enableAnimations = enableAnimations,
            enableAnalytics = remoteConfigManager.getBoolean(RemoteConfigManager.ENABLE_ANALYTICS_KEY)
        )

        // Dynamic Content Demo
        DynamicContentCard(
            featuredProduct = remoteConfigManager.getString(RemoteConfigManager.FEATURED_PRODUCT_KEY),
            discountPercentage = discountPercentage,
            maxItems = remoteConfigManager.getLong(RemoteConfigManager.MAX_ITEMS_PER_PAGE_KEY),
            enableAnimations = enableAnimations,
            pulse = if (enableAnimations) pulse else 1f
        )

        // Performance Metrics
        PerformanceMetricsCard(
            apiTimeout = remoteConfigManager.getLong(RemoteConfigManager.API_TIMEOUT_SECONDS_KEY),
            maxFileUpload = remoteConfigManager.getLong(RemoteConfigManager.MAX_FILE_UPLOAD_MB_KEY),
            cacheDuration = remoteConfigManager.getLong(RemoteConfigManager.CACHE_DURATION_HOURS_KEY)
        )

        // Configuration Values
        ConfigurationValuesCard(remoteConfigManager = remoteConfigManager)
    }
}

@Composable
fun EmergencyBanner(message: String, enableAnimations: Boolean) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (enableAnimations) 1f else 1f,
        animationSpec = if (enableAnimations) infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ) else tween(0),
        label = "emergency_alpha"
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ComposeColor.Red),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Warning,
                contentDescription = "Emergency",
                tint = ComposeColor.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                color = ComposeColor.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(animatedAlpha)
            )
        }
    }
}

@Composable
fun MaintenanceBanner(enableAnimations: Boolean) {
    val animatedRotation by animateFloatAsState(
        targetValue = if (enableAnimations) 360f else 0f,
        animationSpec = if (enableAnimations) infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ) else tween(0),
        label = "maintenance_rotation"
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ComposeColor(0xFFFF9800)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Build,
                contentDescription = "Maintenance",
                tint = ComposeColor.White,
                modifier = Modifier
                    .size(24.dp)
                    .rotate(animatedRotation)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "🔧 Maintenance Mode Active - App is under maintenance",
                color = ComposeColor.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AnnouncementBanner(text: String, enableAnimations: Boolean) {
    val animatedOffset by animateDpAsState(
        targetValue = if (enableAnimations) 0.dp else 0.dp,
        animationSpec = if (enableAnimations) infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ) else tween(0),
        label = "announcement_offset"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = animatedOffset),
        colors = CardDefaults.cardColors(containerColor = ComposeColor(0xFF4CAF50)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Notifications,
                contentDescription = "Announcement",
                tint = ComposeColor.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                color = ComposeColor.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DynamicThemeCard(primaryColor: ComposeColor, theme: String, enableAnimations: Boolean) {
    val animatedColor by animateColorAsState(
        targetValue = primaryColor,
        animationSpec = if (enableAnimations) tween(1000) else tween(0),
        label = "theme_color"
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "🎨 Dynamic Theme Control",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Primary Color",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Theme: $theme",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(animatedColor, CircleShape)
                        .border(2.dp, ComposeColor.Black.copy(alpha = 0.1f), CircleShape)
                )
            }
        }
    }
}

@Composable
fun ABTestingCard(buttonStyle: String, onboardingVariant: String, checkoutFlow: String, enableAnimations: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "🧪 A/B Testing Demo",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            ABTestItem("Button Style", buttonStyle, Icons.Default.Settings)
            ABTestItem("Onboarding", onboardingVariant, Icons.Default.Person)
            ABTestItem("Checkout Flow", checkoutFlow, Icons.Default.ShoppingCart)
        }
    }
}

@Composable
fun ABTestItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun FeatureFlagsCard(showPremium: Boolean, enableAnimations: Boolean, enableAnalytics: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "🚩 Feature Flags",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            FeatureFlagItem("Premium Features", showPremium, enableAnimations)
            FeatureFlagItem("Analytics", enableAnalytics, enableAnimations)
        }
    }
}

@Composable
fun FeatureFlagItem(label: String, enabled: Boolean, enableAnimations: Boolean) {
    val animatedScale by animateFloatAsState(
        targetValue = if (enabled && enableAnimations) 1.2f else 1f,
        animationSpec = if (enableAnimations) tween(300) else tween(0),
        label = "flag_scale"
    )
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            if (enabled) Icons.Default.CheckCircle else Icons.Default.Close,
            contentDescription = label,
            modifier = Modifier
                .size(20.dp)
                .scale(animatedScale),
            tint = if (enabled) ComposeColor(0xFF4CAF50) else ComposeColor(0xFFF44336)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = if (enabled) "ON" else "OFF",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = if (enabled) ComposeColor(0xFF4CAF50) else ComposeColor(0xFFF44336)
        )
    }
}

@Composable
fun DynamicContentCard(featuredProduct: String, discountPercentage: Long, maxItems: Long, enableAnimations: Boolean, pulse: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "📦 Dynamic Content",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            if (discountPercentage > 0) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(pulse),
                    colors = CardDefaults.cardColors(containerColor = ComposeColor(0xFFFF5722)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "🔥 ${discountPercentage}% OFF!",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        textAlign = TextAlign.Center,
                        color = ComposeColor.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            Text(
                text = "Featured: $featuredProduct",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Max items per page: $maxItems",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun PerformanceMetricsCard(apiTimeout: Long, maxFileUpload: Long, cacheDuration: Long) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "⚡ Performance Metrics",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            PerformanceMetric("API Timeout", "${apiTimeout}s", Icons.Default.Settings)
            PerformanceMetric("Max Upload", "${maxFileUpload}MB", Icons.Default.AccountCircle)
            PerformanceMetric("Cache Duration", "${cacheDuration}h", Icons.Default.Settings)
        }
    }
}

@Composable
fun PerformanceMetric(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ConfigurationValuesCard(remoteConfigManager: RemoteConfigManager) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "⚙️ Configuration Values",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            ConfigValue("Welcome Message", remoteConfigManager.getString(RemoteConfigManager.WELCOME_MESSAGE_KEY))
            ConfigValue("API URL", remoteConfigManager.getString(RemoteConfigManager.API_URL_KEY))
            ConfigValue("App Version", remoteConfigManager.getString(RemoteConfigManager.APP_VERSION_KEY))
            ConfigValue("Max Retries", remoteConfigManager.getLong(RemoteConfigManager.MAX_RETRY_ATTEMPTS_KEY).toString())
        }
    }
}

@Composable
fun ConfigValue(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}


@Preview(showBackground = true)
@Composable
fun RemoteConfigShowcasePreview() {
    FirebaseremoteconfigTheme {
        // Preview with mock data
        RemoteConfigShowcase(
            remoteConfigManager = object {
                fun getString(key: String): String = when (key) {
                    "welcome_message" -> "Welcome to our amazing app!"
                    "primary_color" -> "#FF5722"
                    "app_theme" -> "dark"
                    "featured_product" -> "iPhone 15 Pro"
                    "announcement_text" -> "New features available!"
                    "emergency_message" -> "System maintenance in progress"
                    else -> "Preview Value"
                }
                fun getBoolean(key: String): Boolean = when (key) {
                    "show_premium_features" -> true
                    "enable_animations" -> true
                    "show_announcement" -> true
                    "maintenance_mode" -> false
                    else -> false
                }
                fun getLong(key: String): Long = when (key) {
                    "discount_percentage" -> 25L
                    "max_items_per_page" -> 50L
                    "api_timeout_seconds" -> 15L
                    else -> 0L
                }
            } as RemoteConfigManager
        )
    }
}