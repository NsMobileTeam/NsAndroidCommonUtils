# NsAndroidCommonUtils

NsAndroidCommonUtils is an Android commonly used utility bundle library.

## Implementation
Available on [JitPack](https://jitpack.io/#NsMobileTeam/NsAndroidCommonUtils)

In build.gradle **(Project)**:

```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

In build.gradle **(Module)**:

```gradle
dependencies {
  ...
  implementation 'com.google.code.gson:gson:2.8.6'
  implementation 'com.github.nsmobileteam:nsandroidcommonutils:0.3.1'
}
```

**IMPORTANT** next steps:
1. Add these non-runtime permissions in the **Mainifest**:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.DOWNLOAD_WITHOUT_NOTIFICATION" />
```

2. Create an Application extension and add it's name in the Manifest.
3. Add file provider authority.
4. Initialize the UtilBase in the Application extension class:
```java
public class AppExtension extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UtilBase.init(this, R.string.fileProviderName);
        // Resouce Example: <string translatable="false" name="fileProviderName">com.nextsense.utilbundle.fileprovider</string>
    }
}
```

## Usage

Class: **CommonUtils**:
```java
//Display a toast message
public static void toast(String message)
//Display a toast message with duration
public static void toast(String message, int duration)
//Hide the software keyboard
public static void hideKeyboard(View view)
//Get the current display metrics
public static DisplayMetrics getDisplayMetrics()
//Convert dp to pixels
public static float dpToPx(float dp)
//Convert pixels in dp
public static float pxToDp(float px)
//Check if current api level is at least the value of apiLevel
public static boolean minApiLevel(int apiLevel)
//Check if current api level is below or equal the to value of apiLevel
public static boolean maxApiLevel(int apiLevel)
//Generate a bitmap form a Drawable object
public static Bitmap drawableToBitmap(Drawable drawable)
//Get a list of Strings(Groups) that match a regex pattern in a text string
public static ArrayList<String> regexSearch(String text, String regex)  
```
Class: **ResourceFetch**:
```java
//Get string from its resource id
public static String getString(@StringRes int stringId)
//Get localised string from its resource id
public static String getString(@StringRes int stringId, @Nullable Locale locale)
//Get a Drawable object from its resource id
public static Drawable getDrawable(@DrawableRes int drawableId)
//Get a Bitmap from a Drawable resource id
public static Bitmap getDrawableBitmap(@DrawableRes int drawableId)
//Get a color integer from its resource id
public static int getColor(@ColorRes int colorId)
//Get a System Service by the class object of the desired system service
public static <T> T getSystemService(Class<T> serviceClass)
```
Class: **NsDate** (an extension of the Android Date class):
```java
////Constructors:
//New date object from a textString that conforms to the defined SimpleDateFormat formatString
public NsDate(String rawDate, String formatPattern)
//New date object from a textString that conforms to the defined localised SimpleDateFormat
public NsDate(String rawDate, String formatPattern, Locale locale)
//New date object from a textString that conforms to the defined SimpleDateFormat
public NsDate(String rawDate, SimpleDateFormat format)
//Date from a timeString containing a millisecond substring
public NsDate(String rawMillis)
//Date from a timeString containing a timeMark in unknown scale multiplied by its divisor into milliseconds
public NsDate(String rawTime, long timeDivisor)
//Date from milliseconds
public NsDate(long millis)
//Current time and date
public NsDate()

////Methods:
//Set date object from a textString that conforms to the defined SimpleDateFormat formatString
public void setTime(String rawDate, String formatPattern)
//Set date object from a textString that conforms to the defined localised SimpleDateFormat
public void setTime(String rawDate, String formatPattern, Locale locale)
//Set date object from a textString that conforms to the defined SimpleDateFormat
public void setTime(String rawDate, SimpleDateFormat format)
//Set date object from a timeString containing a millisecond substring
public void setTime(String rawMillis)
//Set date object from a timeString containing a timeMark in unknown scale multiplied by its divisor into milliseconds
public void setTime(String rawTime, long timeDivisor)
//Set date object from milliseconds
public void setTime(long millis)
//Creates SimpleDateFormat from a String and returns a formatted date string
public String toString(String dateFormat)
//Creates localised SimpleDateFormat from a String and returns a formatted date string
public String toString(String format, Locale locale)
//Formats date into string defined in SimpleDateFormat
public String toString(SimpleDateFormat format)
//String value of the time in milliseconds
public String toStringMs()
//String value of time in milliseconds devided by the divisor
public String toStringMs(int divisor)
//Get a formatted time string using the keyword &DATE& to place a value of time
public String toStringFormat(String stringFormat, Object... objects)
//Get a formatted time string (divided by the divisor) using the keyword &DATE& to place a value of time
public String toStringFormat(int divisor, String stringFormat, Object... objects)
```

Class: **PermissionUtil**:
```java
//Determine whether all the listed permissions are granted
public static boolean arePermissionsGranted(String... permissions)
//Request listed permissions from the context of an Activity
public static void requestPermissions(Activity activity, String... permissions)
//Request listed permissions from the context of a Fragment
public static void requestPermission(Fragment fragment, String... permissions)
//Opens the app settings of the current app
public static void openAppSettings()
//Gets a unique request code for the listed permissions
public static int requestCodeFor(String... permissions)
```

Class: **FileUtil**:
```java
//Get a file name without its extension from an url
public static String fileNameFromUrl(String url)
//Get a full file name from an url
public static String fullFileNameFromUrl(String url)
//Get the extension of a file from an url
public static String extensionFromUrl(String url)
//Get the extension of a file from an uri
public static String extensionFromUri(Uri uri)
//Get the mime type of a file from an url
public static String mimeTypeOf(String url)
//Get the mime type of a file from an uri
public static String mimeTypeOf(Uri uri)
//Open any accessible file with an Implicit intent
public static void openFile(File file)
//Open any accessible uri with an Implicit intent
public static void openUri(Uri uri)
//Get an File from an accessible uri
public static File fileFromUri(Uri uri)
```

Class: **PreferencesHelper**:
```java
////Constructors:
//Get or create a new sharedPreference bundle by Name
public PreferencesHelper(String name)

////Methods:
//Save a boolean value for a String key in the sharedPreferences
public void saveBoolean(String key, boolean bool)
//Save an int value for a String key in the sharedPreferences
public void saveInt(String key, int n)
//Save a long value for a String key in the sharedPreferences
public void saveLong(String key, long n)
//Save a String value for a String key in the sharedPreferences
public void saveString(String key, String text)
//Save a serializable Object value for a String key in the sharedPreferences
public void saveObject(String key, Object object)
//Get a boolean value from its sharedPreference key
public boolean getBoolean(String key, boolean defValue)
//Get an Integer object from the sharedPreferences by its key
public Integer getInt(String key)
//Get a Long object from the sharedPreferences by its key
public Long getLong(String key)
//Get a String object from the sharedPreferences by its key
public String getString(String key)
//Get any serializable object from shared preferences by its key
public <T> T getObject(String key, Class<T> classObject)
//Delete a list of keys from the sharedPreferences
public void delete(String... keys)
```

Class: **DownloadUtil**:
```java
//Download a file from a HTTP url into the internal storage
public static void downloadUrl(String url)
//Download a file from a HTTP url into the internal storage
public static void downloadUrl(String url, @Nullable DownloadUtil.DownloadCallback callback)
//Download a file from a HTTP if publicFile==true the file will be saved under the Downloads directory
public static void downloadUrl(String url, boolean publicFile, @Nullable DownloadUtil.DownloadCallback callback)
//Download a file from a HTTP if publicFile==true the file will be saved under the Downloads directory
public static void downloadUrl(String url, @Nullable String subDirectory, boolean notification, boolean publicFile, @Nullable DownloadUtil.DownloadCallback callback)
```

Class: **LocaleUtil**:
```java
//Initialises and sets application locale
public static void initAppLocale(@Nullable Locale defaultLocale)
//Apply locale to a context
public static void applyCurrentLocale(Context context)
//Apply locale to an Activity
public static void setLocale(Activity activity, String languageCode)
//Apply locale to a context
public static void setLocale(Context context, String languageCode)
//Apply locale to a context
public static void setLocale(Context context, String languageCode, @Nullable String region)
//Apply locale to a context
public static void setLocale(Context context, Locale locale)
//Gets the prefered locale from preferences if saved, the argument provided or device locale
public static Locale getPreferredLocale(@Nullable Locale defaultLocale)
//Gets the currently set locale from app configuration
public static Locale getCurrentAppLocale()
```

Class: **NsActivity**:
```java
//Loads fragment into container with optional(Nullable) animation (set of 4 animation resources)
protected void loadFragment(Fragment fragment, @IdRes int containerId, boolean addToBackStack, @Nullable Integer[] animationSet)
//Starts another NsActivity with no flags or extras
protected void startActivity(Class<? extends NsActivity> activity)
//Starts another NsActivity with an extra object passed as an argument with no flags
protected void startActivity(Class<? extends NsActivity> activity, @Nullable Object extra)
//Starts another NsActivity with intent flags with no extras
protected void startActivity(Class<? extends NsActivity> activity, @Nullable Integer flags)
//Starts another NsActivity with intent flags and an extra object passed as an argument
protected void startActivity(Class<? extends NsActivity> activity, @Nullable Object extra, @Nullable Integer flags)
//Gets the passed extra object
protected <T> T getExtra()
```

## License
[Apache License 2.0](https://github.com/NsMobileTeam/NsAndroidCommonUtils/blob/main/LICENSE)
