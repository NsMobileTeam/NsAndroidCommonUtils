# NsAndroidCommonUtils

NsAndroidCommonUtils is an Android commonly used utility bundle library.

## Implementation
Available on [JitPack](https://jitpack.io/#NsMobileTeam/NsAndroidCommonUtils)

In build.gradle**(Project)**:

```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

In build.gradle**(Module)**:

```gradle
dependencies {
  ...
  implementation 'com.google.code.gson:gson:2.8.6'
  implementation 'com.github.nsmobileteam:nsandroidcommonutils:0.2'
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
        UtilBase.init(this, this::getFileProviderAuthority);
    }

    private String getFileProviderAuthority() {
        return getString(R.string.fileProviderName);
    }
}
```

## Usage

Class: **CommonUtils**:
```java
public static void toast(String message) //Display a toast message
public static void toast(String message, int duration) //Display a toast message with duration
public static void hideKeyboard(View view) //Hide the software keyboard
public static DisplayMetrics getDisplayMetrics() //Get the current display metrics
public static float dpToPx(float dp) //Convert dp to pixels
public static float pxToDp(float px) //Convert pixels in dp
public static boolean minApiLevel(int apiLevel) //Check if current api level is at least the value of apiLevel
public static boolean maxApiLevel(int apiLevel) //Check if current api level is below or equal the to value of apiLevel
public static Bitmap drawableToBitmap(Drawable drawable) //Generate a bitmap form a Drawable object
public static ArrayList<String> regexSearch(String text, String regex)  //Get a list of Strings(Groups) that match a regex pattern in a text string
```
Class: **ResourceFetch**:
```java
public static String getString(@StringRes int stringId) //Get string from its resource id
public static String getString(@StringRes int stringId, @Nullable Locale locale) //Get localised string from its resource id
public static Drawable getDrawable(@DrawableRes int drawableId) //Get a Drawable object from its resource id
public static Bitmap getDrawableBitmap(@DrawableRes int drawableId) //Get a Bitmap from a Drawable resource id
public static int getColor(@ColorRes int colorId) //Get a color integer from its resource id
public static <T> T getSystemService(Class<T> serviceClass) //Get a System Service by the class object of the desired system service
```
Class: **NsDate** (an extension of the Android Date class):
```java
//Constructors:
public NsDate(String rawDate, String formatPattern) //New date object from a textString that conforms to the defined SimpleDateFormat formatString
public NsDate(String rawDate, String formatPattern, Locale locale) //New date object from a textString that conforms to the defined localised SimpleDateFormat
public NsDate(String rawDate, SimpleDateFormat format) //New date object from a textString that conforms to the defined SimpleDateFormat
public NsDate(String rawMillis) //Date from a timeString containing a millisecond substring
public NsDate(String rawTime, long timeDivisor) //Date from a timeString containing a timeMark in unknown scale multiplied by its divisor into milliseconds
public NsDate(long millis) //Date from milliseconds

//Methods:
public void setTime(String rawDate, SimpleDateFormat format) //Sets the date from a textString that conforms to the defined SimpleDateFormat
private long longFromString(String rawNumber) //Converts any string containing a number into a longInt
public String toString(String dateFormat) //Creates SimpleDateFormat from a String and returns a formatted date string
public String toString(String format, Locale locale) //Creates localised SimpleDateFormat from a String and returns a formatted date string
public String toString(SimpleDateFormat format) //Formats date into string defined in SimpleDateFormat
public String toStringMs() //String value of the time in milliseconds
public String toStringMs(int divisor) //String value of time in milliseconds devided by the divisor
public String toStringFormat(String stringFormat, Object... objects) //Get a formatted time string using the keyword &DATE& to place a value of time
public String toStringFormat(int divisor, String stringFormat, Object... objects) //Get a formatted time string (divided by the divisor) using the keyword &DATE& to place a value of time
```

Class: **PermissionUtil**:
```java
public static boolean arePermissionsGranted(String... permissions) //Determine whether all the listed permissions are granted
public static void requestPermissions(Activity activity, String... permissions) //Request listed permissions from the context of an Activity
public static void requestPermission(Fragment fragment, String... permissions) //Request listed permissions from the context of a Fragment
public static void openAppSettings() //Opens the app settings of the current app
public static int requestCodeFor(String... permissions) //Gets a unique request code for the listed permissions
```

Class: **FileUtil**:
```java
public static String fileNameFromUrl(String url) //Get a file name without its extension from an url
public static String fullFileNameFromUrl(String url) //Get a full file name from an url
public static String extensionFromUrl(String url) //Get the extension of a file from an url
public static String extensionFromUri(Uri uri) //Get the extension of a file from an uri
public static String mimeTypeOf(String url) //Get the mime type of a file from an url
public static String mimeTypeOf(Uri uri) //Get the mime type of a file from an uri
public static void openFile(File file) //Open any accessible file with an Implicit intent
public static void openUri(Uri uri) //Open any accessible uri with an Implicit intent
public static File fileFromUri(Uri uri) //Get an File from an accessible uri
```

Class: **PreferencesHelper**:
```java
//Constructors:
public PreferencesHelper(String name) //Get or create a new sharedPreference bundle by Name

//Methods:
public void saveBoolean(String key, boolean bool) //Save a boolean value for a String key in the sharedPreferences
public void saveInt(String key, int n) //Save an int value for a String key in the sharedPreferences
public void saveLong(String key, long n) //Save a long value for a String key in the sharedPreferences
public void saveString(String key, String text) //Save a String value for a String key in the sharedPreferences
public void saveObject(String key, Object object) //Save a serializable Object value for a String key in the sharedPreferences
public boolean getBoolean(String key, boolean defValue) //Get a boolean value from its sharedPreference key
public Integer getInt(String key) //Get an Integer object from the sharedPreferences by its key
public Long getLong(String key) //Get a Long object from the sharedPreferences by its key
public String getString(String key) //Get a String object from the sharedPreferences by its key
public <T> T getObject(String key, Class<T> classObject) //Get any serializable object from shared preferences by its key
public void delete(String... keys) //Delete a list of keys from the sharedPreferences
```

Class: **DownloadUtil**:
```java
public static void downloadUrl(String url) //Download a file from a HTTP url into the internal storage
public static void downloadUrl(String url, @Nullable DownloadUtil.DownloadCallback callback) //Download a file from a HTTP url into the internal storage
public static void downloadUrl(String url, boolean publicFile, @Nullable DownloadUtil.DownloadCallback callback) //Download a file from a HTTP if publicFile==true the file will be saved under the Downloads directory
public static void downloadUrl(String url, @Nullable String subDirectory, boolean notification, boolean publicFile, @Nullable DownloadUtil.DownloadCallback callback) //Download a file from a HTTP if publicFile==true the file will be saved under the Downloads directory
```

## License
[Apache License 2.0](https://github.com/NsMobileTeam/NsAndroidCommonUtils/blob/main/LICENSE)
