# Remote Config Debugging Guide

## Steps to Debug Remote Config Issues

### 1. Check Logcat Output

Run your app and check the logcat for these log messages:

```
D/RemoteConfigManager: === Current Remote Config Values ===
D/RemoteConfigManager: Welcome Message: [value]
D/RemoteConfigManager: Feature Flag: [value]
D/RemoteConfigManager: Last fetch time: [timestamp]
D/RemoteConfigManager: Fetch status: [status]
```

### 2. Common Issues and Solutions

#### Issue: Values showing as XML defaults

**Possible Causes:**

- Firebase project not properly configured
- google-services.json not updated
- Parameter names don't match exactly
- Changes not published in Firebase Console

**Solutions:**

1. Verify google-services.json is in `app/` directory
2. Check parameter names match exactly (case-sensitive)
3. Ensure changes are published in Firebase Console
4. Try the "Refresh Config" button in the app

#### Issue: Fetch status shows error

**Possible Causes:**

- Network connectivity issues
- Firebase project configuration
- Invalid parameter values

**Solutions:**

1. Check internet connection
2. Verify Firebase project is active
3. Check parameter data types in Firebase Console

#### Issue: Values not updating after publishing

**Possible Causes:**

- App cache
- Fetch interval settings
- Parameter not activated

**Solutions:**

1. Force close and reopen the app
2. Use "Refresh Config" button
3. Check if parameter is activated in Firebase Console

### 3. Testing Steps

1. **Run the app** and check logcat
2. **Look for the debug logs** showing current values
3. **Press "Refresh Config"** button
4. **Check if values change** after refresh
5. **Verify Firebase Console** shows published changes

### 4. Firebase Console Checklist

- [ ] Parameter created with exact name (case-sensitive)
- [ ] Parameter has a value set
- [ ] Parameter is activated
- [ ] Changes are published
- [ ] App package name matches Firebase project

### 5. Debug Commands

To see detailed logs, filter logcat by:

```
adb logcat | grep RemoteConfigManager
```

### 6. Expected Behavior

1. **First run**: Should show XML default values
2. **After fetch**: Should show Firebase Console values
3. **Refresh button**: Should fetch latest values
4. **Logs**: Should show fetch status and current values

## Quick Fix Checklist

1. ✅ Verify google-services.json is updated
2. ✅ Check parameter names match exactly
3. ✅ Ensure Firebase Console changes are published
4. ✅ Try "Refresh Config" button
5. ✅ Check logcat for debug messages
6. ✅ Verify internet connection
7. ✅ Force close and reopen app

## Still Not Working?

If values still don't appear:

1. **Double-check parameter names** - they must match exactly
2. **Verify Firebase project** - ensure it's the correct project
3. **Check network** - ensure device has internet access
4. **Review logs** - look for error messages in logcat
5. **Test with simple parameter** - try with just one parameter first

