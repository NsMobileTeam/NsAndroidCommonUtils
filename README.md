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

    dependencies {//OPTIONAL for firebase messaging:
      ...
      classpath 'com.google.gms:google-services:4.3.8'
      classpath 'com.google.firebase:firebase-crashlytics-gradle:2.7.1'
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
    implementation 'com.github.nsmobileteam:nsandroidcommonutils:1.1.7'
    implementation 'com.google.code.gson:gson:2.8.7'
    implementation "androidx.biometric:biometric:1.1.0"
    implementation 'androidx.security:security-crypto:1.1.0-alpha03'
    implementation 'io.github.inflationx:calligraphy3:3.1.1'
    implementation 'io.github.inflationx:viewpump:2.0.3'

    //OPTIONAL for firebase messaging:
    implementation 'com.google.firebase:firebase-messaging:22.0.0'
    implementation 'com.google.firebase:firebase-analytics:19.0.1'
    implementation 'com.google.firebase:firebase-core:19.0.1'
    implementation 'com.google.firebase:firebase-crashlytics:18.2.1'
    ...
  }

//OPTIONAL for firebase messaging:
apply plugin: 'com.google.firebase.crashlytics'
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
        UtilBase.setupCalligraphy("fonts/OpenSans-Regular.ttf");//path to ttf font in assets
        //or UtilBase.setupCalligraphy();
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
//Get a color state list for changing a view tint programmatically
public static ColorStateList getTint(@ColorInt int color)
//Converts any serializable object or array of objects into a json string
public static <T> String toJson(T object)
//Parse a json into a serializable object of type T
public static <T> T fromJson(String json, Class<T> classObject)
//Parse a json into a serializable array of objects of type T
public static <T> ArrayList<T> fromJsonArray(String jsonArray, Class<T> itemClass)
//Get application name
public static String getAppName()
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

Class: **NsPrefs**:
```java
////Constructors:
//Get default app shared preference
public static NsPrefs get()
//Get or create a new sharedPreference bundle by Name
public static NsPrefs get(String name)
//Get or create the default EncryptedSharedPreferences bundle by Name
public static NsPrefs getSecure()
//Get or create a new EncryptedSharedPreferences bundle by Name
public static NsPrefs getSecure(String name)
//Get or create the default EncryptedSharedPreferences bundle by Name locked by device locking mechanism
public static void getLocked(IUserAuthListener listener)
//Get or create a new EncryptedSharedPreferences bundle by Name locked by device locking mechanism
public static void getLocked(String name, IUserAuthListener listener)

////Methods:
//Save a boolean value for a String key in the sharedPreferences
public void saveBoolean(String key, boolean bool)
//Save byte data for a String key in the sharedPreferences
public void saveBytes(String key, byte[] data)
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
//Get byte data from its sharedPreference key
public byte[] getBytes(String key)
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
//Null-safe string value of any object, returns fallbackValue if null
public static <T> String text(T value, String fallbackValue)
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

Class: **EncryptionUtil.Store**:
```java
//Get the default android keystore "AndroidKeyStore"
public static KeyStore getMainKeystore()
//Get or Generate a key pair within the main keystore
public static KeyPair getKeyPair(String alias, String keyType, int sizeBits, boolean userAuthenticated)
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
//Launch a universal ActivityResultLauncher bound by a modifiable contract
public void startLauncher(Object input, NsActivityContract.IContractInterface<Object,Object> listener)
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
//Get the parent activity
public Activity getParent()
//Pop the main backstack
public void onBack()
//Pop the entire fragment backstack
public void popBackstack()
//Gets the passed extra object
protected <S> S getExtra()
```

Abstract class (handles ViewBindings): **NsDialog&lt;T extends ViewBinding&gt;**:
```java
//abstract setup of a full screen dialog
```

ViewHolder class (handles ViewBindings): **UniHolder&lt;T extends ViewBinding&gt;**:
```java
//Create a universal recycler ViewHolder
public UniHolder(T binding)
```

ViewHolder class (handles ViewBindings): **NsAdapter&lt;T extends ViewBinding&gt;**:
```java
//Create a universal recycler Item Adapter
```

Interface: **IUniversalListener&lt;T extends Object&gt;**:
```java
//Return object of class T if process ended successfully
void onSuccess(T result)
//return an exception if process failed
void onFail(Exception e)
```

Dialog class: **NsAlertDialog**:
```java
//Display a customized AlertDialog with custom components and action
public static void show(Context context, @LayoutRes int layoutRes, Component... components)
//Display a customized AlertDialog with custom components and action
public static void show(Context context, boolean requiresAction, @LayoutRes int layoutRes, Component... components)
//Object used to set the text of any view by it's id
public static class Text
//Object used to set a click listener of any view by it's id
public static class Button
```

Dialog class: **NsLoadingScreen**:
```java
//Sets a default layout to be used in case a special layout is defined
public static void setDefaultLayout(@LayoutRes int layoutId)
//Starts a single fullscreen topmost instance of a loader
public static void show(FragmentTransaction transaction)
//Starts a single fullscreen topmost instance of a loader
public static void show(FragmentManager manager)
//Starts a single fullscreen topmost instance of a loader
public static void show(FragmentManager manager, @Nullable Integer layoutId)
//Starts a single fullscreen topmost instance of a loader
public static void show(FragmentTransaction transaction, @Nullable Integer layoutId)
//Close and destroy the currently running instance of the loader
public static void close()
```

Helper class: **NsChannel**:
```java
//Constructor for creating an API safe NotificationChannel for Firebase messaging
public NsChannel(String channelId, String channelName, int importance)
```

Abstract firebase service class: **NsFirebaseHandler&lt;T extends NsNotification&gt;**:
```java
//Abstract method called after a new token was issued and saved
public abstract void onNewToken();
//Abstract method called after a new message was received
public abstract void onMessageReceived(@NonNull T notification);
//Abstract method for retrieval of the class object of the T NsNotification
public abstract @NonNull Class<T> getNotificationClass();
//Abstract method for retrieval of the api safe NotificationChannel
public abstract NsChannel getChannel();
//Show notification of type NsNotification in the notification bar
public void publish(T notification)
//Get the currently available GMS token
public static String getToken()
//Cancel any NsNotificaiton
public static <T extends NsNotification> void cancel(Context context, T notification)
```

Abstract class: **NsNotification**:
```java
//Abstract method for retrieval of the notification's title
public abstract String title()
//Abstract method for retrieval of the notification's message
public abstract String message()
//Abstract method for retrieval of the notification's icon
public abstract Integer icon()
//Abstract method for retrieval of the notification's body icon
public abstract Bitmap thumbnail()
//Abstract method for retrieval of the notification's pending intent
public abstract PendingIntent getPendingIntent(Context context)
//Abstract method for retrieval of the notification custom view
public abstract RemoteViews getContentView(String packageName)
//Gets the randomised notification id
public int id()
//Create a pending intent for this Notification
public PendingIntent getPendingIntent(Context context, Class<? extends Activity> activityClass)
//Set the remotely created notification
public void setRemoteNotification(RemoteMessage.Notification remoteNotification)
//Gets the remotely created notification
public RemoteMessage.Notification getRemoteNotification()
//Create an NsNotification embedded within the extras in an intent
public static <T extends NsNotification> T fromBundle(Intent intent)
//Creates a default NsNotification from a remotely crated notification
public static NsNotification getDefault(RemoteMessage.Notification remoteNotification)
```

Logger class: **Note**:
```java
//Log an exception online and within the logcat
public static void o(Exception e)
//Log any type of content online and within the logcat
public static void o(String tag, String content)
//Log an exception within the logcat
public static void l(Exception e)
//Log any type of content online and within the logcat
public static void l(String tag, String content)
//Setup a handler for online logging
public static void setOnlineLogger(ILogOnline onlineLogger)
```

## License
[Apache License 2.0](https://github.com/NsMobileTeam/NsAndroidCommonUtils/blob/main/LICENSE)
