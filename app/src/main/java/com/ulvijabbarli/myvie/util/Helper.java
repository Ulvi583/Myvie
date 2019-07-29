package com.ulvijabbarli.myvie.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.content.Context.TELEPHONY_SERVICE;
import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Ulvi Jabbarli on 25/02/2019.
 */

public class Helper {

    private static ProgressBar progressBar;

    public static String calcPercent(double oldPrice, double newPrice) {
        double percent = ((oldPrice - newPrice) * 100) / oldPrice;
        return String.valueOf((int) percent);
    }

    public static Double calcPercentAccordingToNumbers(double numberOne, double numberTwo) {
        Log.e("LOG_PERCENT", "numberOne--->" + numberOne + ",   numberTwo-->" + numberTwo);
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        if (numberOne > numberTwo) {
            Log.e("LOG_PERCENT", "numberOne > numberTwo");
            return Double.valueOf(decimalFormat.format((numberTwo * 100) / numberOne).replaceAll(",", "."));
        } else if (numberOne < numberTwo) {
            Log.e("LOG_PERCENT", "numberOne < numberTwo");
            return Double.valueOf(decimalFormat.format((numberOne * 100) / numberTwo).replaceAll(",", "."));
        } else if (numberOne == 0.0 || numberTwo == 0.0) {
            return Double.valueOf(decimalFormat.format(0.0).replaceAll(",", "."));
        } else {
            return Double.valueOf(decimalFormat.format(100.0).replaceAll(",", "."));

        }
    }

    public static String calcPriceAccordingToPercent(double price, double percent) {
        double finalPrice = price - ((price * percent) / 100);
        return String.valueOf(finalPrice);
    }

    public static String generateRandomString() {
        return UUID.randomUUID().toString();
    }

    public static String getStringFromCalendar(Calendar calendar, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        return sdf.format(calendar.getTime());
    }

    public static String getIpOfDevice(Context context) {
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

    public static String getHashCodeOfStrings(String key, String... texts) {
        StringBuilder fullText = new StringBuilder();
        String hmac = "";
        Mac mac = null;
        for (String text : texts) {
            fullText.append(text);
        }
        try {
            mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            mac.init(secret);
            byte[] digest = mac.doFinal(fullText.toString().getBytes());
            BigInteger hash = new BigInteger(1, digest);
            hmac = hash.toString(16);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }

        if (hmac.length() % 2 != 0) {
            hmac = "0" + hmac;
        }

        return hmac;
    }

    public static String splitInstagramName(String fullString) {
        if (!TextUtils.isEmpty(fullString)) {
            return fullString.substring(fullString.lastIndexOf("/") + 1, fullString.length());
        } else {
            return "";
        }
    }

    public static String convertObjectToString(Object recommendationPOJO) {
        Gson gson = new Gson();
        return gson.toJson(recommendationPOJO);
    }

    public static <T> Object convertStringToObject(String string, Class<T> c) {
        Gson gson = new Gson();
        return gson.fromJson(string, c);
    }

    public static Bitmap uriToBitmap(Context context, Uri selectedFileUri) {
        Bitmap image = null;
        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static Bitmap getImageAsBitmap(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static Calendar getDateFromString(String date, String format) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(date));
            return cal;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Intent to open the official Instagram app to the user's profile. If the Instagram app is not
     * installed then the Web Browser will be used.</p>
     * <p>
     * Example usage:</p> {@code newInstagramProfileIntent(context.getPackageManager(),
     * "http://instagram.com/jaredrummler");}</p>
     *
     * @param pm  The {@link PackageManager}. You can find this class through
     *            {@link Context#getPackageManager()}.
     * @param url The URL to the user's Instagram profile.
     * @return The intent to open the Instagram app to the user's profile.
     */
    public static Intent newInstagramProfileIntent(PackageManager pm, String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            if (pm.getPackageInfo("com.instagram.android", 0) != null) {
                if (url.endsWith("/")) {
                    url = url.substring(0, url.length() - 1);
                }
                final String username = url.substring(url.lastIndexOf("/") + 1);
                // http://stackoverflow.com/questions/21505941/intent-to-open-instagram-user-profile-on-android
                intent.setData(Uri.parse("http://instagram.com/_u/" + username));
                intent.setPackage("com.instagram.android");
                return intent;
            }
        } catch (PackageManager.NameNotFoundException ignored) {

        }
        intent.setData(Uri.parse(url));
        return intent;
    }

    public static byte[] getImageAsBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        return baos.toByteArray();
    }

    public static void setImageWithGlide(Context context, String url, ImageView target) {
        Glide.with(context)
                .load(url)
                .into(target);
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // the public methods don't seem to work for me, so… reflection.
        try {
            Method showSoftInputUnchecked = InputMethodManager.class.getMethod("showSoftInputUnchecked", int.class, ResultReceiver.class);
            showSoftInputUnchecked.setAccessible(true);
            showSoftInputUnchecked.invoke(imm, 0, null);
        } catch (Exception e) {
            // ho hum
        }
    }

    public static void hideKeyboard(Activity activity) {
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        //Find the currently focused view, so we can grab the correct window token from it.
//        View view = activity.getCurrentFocus();
//        //If no view currently has focus, create a new one, just so we can grab a window token from it
//        if (view == null) {
//            view = activity.getCurrentFocus();
//        }
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isCameraExists(Activity activity) {
        return activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static boolean isTelephonyEnabled(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        return telephonyManager != null && telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean isInputsNotEmpty(String... args) {
        for (String val : args) {
            if (TextUtils.isEmpty(val)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isLogined(Context context) {
        return !TextUtils.isEmpty("");
    }

    public static void reCreateApp(Context context) {
//        Intent intent = new Intent(context, SplashActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        context.startActivity(intent);
    }

    public static int dpToPx(Context context, int dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    public static MultipartBody.Part prepareFilePart(String partName, File file) {
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("ic_add_photo/*"),
                        file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public static RequestBody createPartFromString(String data) {
        return RequestBody.create(MediaType.parse("text/plain"), data);
    }

    public static File convertBitmapToFile(Context context, Bitmap bitmap) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, "ic_add_photo.jpg");
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            return imageFile;
        } catch (Exception e) {
            return null;
        }
    }

    public static File createImageFile(Activity activity) {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            return null;
        }

        return image;
    }

    public static Uri getImageUriOfThumbnail(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000, true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        String path = "";
        if (context.getContentResolver() != null) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public static void call(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    public static int getCurrentDayOfWeek() {
        Log.e("LOG_DAY_OF_WEEK", "day index--->" + Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + ", today-->" + Calendar.WEDNESDAY);
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
    }


}
