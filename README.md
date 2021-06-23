# NsAndroidCommonUtils

NsAndroidCommonUtils is an Android commonly used utility bundle library.

## Implementation
[![](https://jitpack.io/v/NsMobileTeam/NsAndroidCommonUtils.svg)](https://jitpack.io/#NsMobileTeam/NsAndroidCommonUtils)

In build.gradle **(Project)**:

```gradle
buildscript {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
  }

allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

In build.gradle **(Module)**:

```gradle
  android {
    ...
    buildFeatures {
      viewBinding true
      dataBinding false
    }
  }

  dependencies {
    ...
    implementation 'com.google.code.gson:gson:2.8.6'
    implementation 'com.github.nsmobileteam:nsandroidcommonutils:0.3.8'
    ...
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
        UtilBase.init(this, R.string.fileProviderName, new Locale("mk"));
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
//Opens the app settings of the current app
public static void openAppSettings()
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
//Get the app resources
public static Resources getResources()
//Get the app configuration
public static Configuration getConfiguration()
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
//Gets the year of this date
public int year()
//Gets the native month (January == 0) of this date
public int monthNative()
//Gets the month (January == 1) of this date
public int month()
//Gets the day of month of this date
public int dayOfMonth()
//Gets the hour of this date
public int hour()
//Gets the minute of this date
public int minute()
//Gets the second of this date
public int second()
//Gets the millisecond of this date
public int millisecond()
```

Class: **NsDateDialog**:
```java
//Creates a date picker dialog
public static void showDateDialog(FragmentManager fragmentManager, NsDate fromNsDate, NsDate toNsDate, NsDate selectedNsDate, IUniversalListener<NsDate> listener)
//Creates a time picker dialog
public static void showTimeDialog(FragmentManager fragmentManager, NsDate fromNsDate, NsDate toNsDate, NsDate selectedNsDate, IUniversalListener<NsDate> listener)
//Creates a date and time picker dialog
public static void showDateTimeDialog(FragmentManager fragmentManager, NsDate fromNsDate, NsDate toNsDate, NsDate selectedNsDate, IUniversalListener<NsDate> listener)
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

Class: **Safe**:
```java
//Null-safe string value of any object
public static <T> String text(T value)
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

Class: **EncryptionUtil**:
```java
//Gets an overly complicated secure random generator
public static SecureRandom secureRandomInstance()
//Get a secure random array
public static byte[] secureRandom(int byteNum)
//Sign any type of data
public static byte[] signData(KeyPair keyPair, String algorithm, byte[] data)
//Asymmetric encryption of any data
public static byte[] encrypt(PublicKey publicKey, String algorithm, byte[] plainText)
//Asymmetric decryption of any data
public static byte[] decrypt(PrivateKey privateKey, String algorithm, byte[] cipherText)
//Generate an Private/Public key pair
public static KeyPair generateKeyPair(String algorithm, int keySize)
//Derive secret key from a string password
public static SecretKey deriveSecretKey(String algorithm, String keyFactoryAlgorithm, int iterationCount, int keyLength, String password)
//Derive secret key from a string password with salt
public static SecretKey deriveSecretKey(String algorithm, String keyFactoryAlgorithm, int iterationCount, int keyLength, String password)
//Derive secret key from bytes
public static SecretKey deriveSecretKey(byte[] keyBytes, String algorithm)
//Get a randomized iv parameter spec
public static IvParameterSpec randomIv(int ivSize)
//Symmetric encryption of a plaintext
public static byte[] encrypt(String algorithm, SecretKey secretKey, IvParameterSpec cbcIv, byte[] plaintext)
//Symmetric decryption of a ciphertext
public static byte[] decrypt(String algorithm, SecretKey secretKey, IvParameterSpec cbcIv, byte[] ciphertext)
```

Class: **EncryptionUtil.TOTP**:
```java
//Generate the current TOTP
public static int generate(byte[] secret)
//Generate a TOTP for a custom time
public static int generate(byte[] secret, long timeMillis)
//Generate a very customized TOTP
public static int generate(byte[] secret, long timeMillis, @Nullable Long validityTimeMillis, @Nullable Integer codeDigits, @Nullable String hashAlgorithm)
```

Abstract class (handles ViewBindings): **NsActivity&lt;T extends ViewBinding&gt;**:
```java
//Loads fragment into container with optional(Nullable) animation (set of 4 animation resources)
public void loadFragment(Fragment fragment, @IdRes int containerId, boolean addToBackStack, @Nullable Integer[] animationSet)
//Start request dialogs for each of the permissions
public void requestPermissions(IUniversalListener<Map<String, Boolean>> permissionListener, String... permissions)
//Determine whether all the listed permissions are granted
public boolean arePermissionsGranted(Map<String, Boolean> permissionResult)
//Determine whether all the listed permissions are granted
public boolean arePermissionsGranted(String... permissions)
//Starts another NsActivity with no flags or extras
public void startActivity(Class<? extends NsActivity> activity)
//Starts another NsActivity with an extra object passed as an argument with no flags
public void startActivity(Class<? extends NsActivity> activity, @Nullable Object extra)
//Starts another NsActivity with intent flags with no extras
public void startActivity(Class<? extends NsActivity> activity, @Nullable Integer flags)
//Starts another NsActivity with intent flags and an extra object passed as an argument
public void startActivity(Class<? extends NsActivity> activity, @Nullable Object extra, @Nullable Integer flags)
//Gets the passed extra object
protected <S> S getExtra()
```

Abstract class (handles ViewBindings): **NsFragment&lt;T extends ViewBinding&gt;**:
```java
//Pass an extra serializable object to a newly created Fragment
public void setExtra(@Nullable Object extra);
//Loads fragment into container within the parent activity with optional(Nullable) animation (set of 4 animation resources)
public void loadFragment(Fragment fragment, @IdRes int containerId, boolean addToBackStack, @Nullable Integer[] animationSet)
//Get a NsActivity if fragment is loaded from a NsActivity
public NsActivity<?> getNsActivity()
//Gets the passed extra object
protected <S> S getExtra()
```

ViewHolder class (handles ViewBindings): **UniHolder&lt;T extends ViewBinding&gt;**:
```java
//Create a universal recycler ViewHolder
public UniHolder(@NonNull View itemView, T binding)
```

Interface: **IUniversalListener&lt;T extends Object&gt;**:
```java
//Return object of class T if process ended successfully
void onSuccess(T result)
//return an exception if process failed
void onFail(Exception e)
```

## License
[Apache License 2.0](https://github.com/NsMobileTeam/NsAndroidCommonUtils/blob/main/LICENSE)
