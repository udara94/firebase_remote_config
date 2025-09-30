# Firebase Remote Config Android Demo

This project demonstrates how to implement Firebase Remote Config in an Android app using Jetpack Compose.

## Features

- ✅ Firebase Remote Config integration
- ✅ Modern Jetpack Compose UI
- ✅ Real-time configuration updates
- ✅ Default values handling
- ✅ Error handling and logging

## Setup Instructions

### 1. Firebase Project Setup

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select an existing one
3. Add an Android app with package name: `com.example.firebaseremoteconfig`
4. Download the `google-services.json` file
5. Replace the placeholder file at `app/google-services.json.placeholder` with your actual `google-services.json`

### 2. Configure Remote Config Parameters

1. In Firebase Console, go to Remote Config
2. Add the following parameters:
   - `welcome_message` (String): "Welcome to our app!"
   - `feature_flag_enabled` (Boolean): false
   - `api_base_url` (String): "https://api.example.com"
   - `app_version` (String): "1.0.0"
3. Publish the changes

### 3. Build and Run

1. Sync the project in Android Studio
2. Build and run the app
3. The app will display the Remote Config values

## Project Structure

```
app/
├── src/main/java/com/example/firebaseremoteconfig/
│   ├── MainActivity.kt              # Main activity with Compose UI
│   ├── RemoteConfigManager.kt       # Remote Config management
│   └── ui/theme/                    # Compose theme files
├── google-services.json.placeholder # Replace with your actual file
└── build.gradle.kts                 # App-level dependencies
```

## Key Components

### RemoteConfigManager

- Handles all Remote Config operations
- Sets default values
- Fetches and activates new configurations
- Provides type-safe getters for different data types

### MainActivity

- Demonstrates Remote Config usage in Compose UI
- Shows real-time configuration values
- Includes refresh functionality

## Dependencies

- Firebase BOM 32.7.0
- Firebase Remote Config KTX
- Firebase Analytics KTX
- Jetpack Compose
- Material 3

## Next Steps

1. Replace `google-services.json.placeholder` with your actual Firebase configuration
2. Customize the Remote Config parameters for your use case
3. Implement additional features like A/B testing or conditional parameters
4. Add proper error handling and user feedback

## Resources

- [Firebase Remote Config Documentation](https://firebase.google.com/docs/remote-config)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Firebase Console](https://console.firebase.google.com/)
