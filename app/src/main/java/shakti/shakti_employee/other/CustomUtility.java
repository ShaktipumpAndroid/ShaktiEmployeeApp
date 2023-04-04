package shakti.shakti_employee.other;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by shakti on 11/21/2016.
 */
public class CustomUtility {
    static boolean connected;
    String current_date, current_time;
    Calendar calander = null;
    public static Context appContext;
    SimpleDateFormat simpleDateFormat = null;
    private static String PREFERENCE = "DealLizard";
    GPSTracker gps;

    public static void ShowToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isDateTimeAutoUpdate(Context mContext) {
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (android.provider.Settings.Global.getInt(mContext.getContentResolver(), android.provider.Settings.Global.AUTO_TIME) == 1) {
                    return true;
                }
            } else {
                if (android.provider.Settings.System.getInt(mContext.getContentResolver(), android.provider.Settings.Global.AUTO_TIME) == 1) {
                    return true;
                }
            }


        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String formateDate(String date) {
        String formatedDate = "";
        try {
            SimpleDateFormat formate = new SimpleDateFormat("yyyyMMdd");
            Date mDate = formate.parse(date);
//            SimpleDateFormat appFormate = new SimpleDateFormat("dd MMM, yyyy");
            SimpleDateFormat appFormate = new SimpleDateFormat("dd.MM.yyyy");
            formatedDate = appFormate.format(mDate);
            Log.i("Result", "mDate " + formatedDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatedDate;
    }

    public static String formateTime(String time) {
        String formatedDate = "";
        try {
            SimpleDateFormat formate = new SimpleDateFormat("HHmmss");
            Date mDate = formate.parse(time);
//            SimpleDateFormat appFormate = new SimpleDateFormat("dd MMM, yyyy");
            SimpleDateFormat appFormate = new SimpleDateFormat("HH:mm:ss");
            formatedDate = appFormate.format(mDate);
            Log.i("Result", "mDate " + formatedDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatedDate;
    }

    public static String formateDate1(String date) {
        String formatedDate = "";
        try {
            SimpleDateFormat formate = new SimpleDateFormat("dd.MM.yyyy",Locale.US);
            Date mDate = formate.parse(date);
//            SimpleDateFormat appFormate = new SimpleDateFormat("dd MMM, yyyy");
            SimpleDateFormat appFormate = new SimpleDateFormat("yyyyMMdd",Locale.US);
            formatedDate = appFormate.format(mDate);
            Log.i("Result", "mDate " + formatedDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatedDate;
    }

    public static String formateTime1(String time) {
        String formatedDate = "";
        try {
            SimpleDateFormat formate = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
            Date mDate = formate.parse(time);
//            SimpleDateFormat appFormate = new SimpleDateFormat("dd MMM, yyyy");
            SimpleDateFormat appFormate = new SimpleDateFormat("HHmmss",Locale.getDefault());
            formatedDate = appFormate.format(mDate);
            Log.i("Result", "mDate " + formatedDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatedDate;
    }

    public static void showSettingsAlert(final Context mContext) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        // Setting Dialog Title
        alertDialog.setTitle("Date & Time Settings");
        // Setting Dialog Message
        alertDialog.setMessage("Please enable automatic date and time setting");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        // on pressing cancel button

//        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });

        // Showing Alert Message
        alertDialog.show();
        //alertDialog.setCancelable(cancellable);
    }

    public static void showTimeSetting(final Context mContext, DialogInterface.OnClickListener pos, DialogInterface.OnClickListener neg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        // Setting Dialog Title
        alertDialog.setTitle("DATE TIME SETTINGS");
        // Setting Dialog Message
        alertDialog.setMessage("Date Time not auto update please check it.");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
        //alertDialog.setCancelable(cancellable);
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        return capitalize(manufacturer) + "--" + model;
//        if (model.startsWith(manufacturer)) {
//            return capitalize(model);
//        } else {
//            return capitalize(manufacturer) + "--" + model;
//        }


    }

    public static String getDeviceId(Context mContext) {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static boolean checkNetwork(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isInternetOn(Context mContext) {


        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    Log.e("INTERNET:", String.valueOf(i));
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Log.e("INTERNET123:", "connected!");
                        return true;
                    }
                }
            }
        }
        return false;

    }

    public static boolean CheckGPS(Context mContext) {

        GPSTracker gps = new GPSTracker(mContext);

        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            if (latitude == 0.0 && longitude == 0.0) {
                CustomUtility.ShowToast("Lat Long not captured, Please try again.", mContext);
                return false;
            }
        } else {
            gps.showSettingsAlert();
            return false;
        }


        return true;
    }
//    TelephonyManager telephonyManager = (TelephonyManager) activity
//            .getSystemService(Context.TELEPHONY_SERVICE);
//    return telephonyManager.getDeviceId();
//}

    /*compress image and convert to  stirng*/
    public static String CompressImage(String mFile) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        //options.inSampleSize = 2;

        Log.d("mFile", "" + mFile);

        final Bitmap bitmap = BitmapFactory.decodeFile(mFile, options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();


        //    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);

        byte[] byteArray = stream.toByteArray();


        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encodedImage;
    }


    // for username string preferences
    public static void setSharedPreference(Context context, String name,
                                           String value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putString(name, value);
        editor.commit();
    }

    public static String getSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        return settings.getString(name, "");
    }

    public String getCurrentDate() {
        simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        current_date = simpleDateFormat.format(new Date());
        return current_date.trim();
    }

    public String getCurrentTime() {
        calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HHmmss");
        current_time = simpleDateFormat.format(calander.getTime());
        return current_time.trim();
    }


    public String getCurrentDate1() {
        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        current_date = simpleDateFormat.format(new Date());
        return current_date.trim();
    }

    public String getCurrentTime1() {
        calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        current_time = simpleDateFormat.format(calander.getTime());
        return current_time.trim();
    }



}
