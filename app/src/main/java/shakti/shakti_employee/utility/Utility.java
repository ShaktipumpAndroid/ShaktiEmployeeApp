package shakti.shakti_employee.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by shakti on 11/15/2016.
 */


public class Utility {

    public static Context appContext;

    public static   String GALLERY_DIRECTORY_NAME_COMMON = "ShaktiEmployeeApp";
    public static   String CUSTOMERID_ID = "";

    private static String PREFERENCE = "DealLizard";

    public static void ShowToast(String text, Context context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static boolean isDateTimeAutoUpdate(Context mContext) {
        try {
            if (Settings.Global.getInt(mContext.getContentResolver(), Settings.Global.AUTO_TIME) == 1) {
                return true;
            } else {
                //Settings.Global.getInt(mContext.getContentResolver(), Settings.Global.AUTO_TIME);
                return false;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getVersionCode(Context mContext) {
        String version = "1.0";
        PackageInfo pInfo = null;
        try {
            pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = pInfo.versionName;
        return version;
    }


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

    public static String findJSONFromUrl(String url) {
        // TODO Auto-generated method stub
        JSONObject jObj = new JSONObject();
        String result = "";

        System.out.println("URL comes in jsonparser class is:  " + url);
        try {
            int TIMEOUT_MILLISEC = 100000; // = 10 seconds
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams,
                    TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            // httpGet.setURI(new URI(url));

            HttpResponse httpResponse = httpClient.execute(httpGet);
            int status = httpResponse.getStatusLine().getStatusCode();

            InputStream is = httpResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");

            }

            is.close();
            result = sb.toString();
            System.out.println("result  in jsonparser class ........" + result);

        } catch (Exception e) {
            System.out.println("exception in jsonparser class ........");
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public static String postParamsAndfindJSON(String url, ArrayList<NameValuePair> params) {
        // TODO Auto-generated method stub
        JSONObject jObj = new JSONObject();
        String result = "";

        System.out.println("URL comes in jsonparser class is:  " + url);
        Log.e("url is....", url+"");
        Log.e("params  is....", params+"");
        try {
            int TIMEOUT_MILLISEC = 100000; // = 10 seconds
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams,
                    TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            // httpGet.setURI(new URI(url));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            int status = httpResponse.getStatusLine().getStatusCode();

            InputStream is = httpResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");

            }

            is.close();
            result = sb.toString();

        } catch (Exception e) {
            System.out.println("exception in jsonparser class ........");
            e.printStackTrace();
            return null;
        }
        return result;
    }
}
