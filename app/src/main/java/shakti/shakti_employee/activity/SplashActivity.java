package shakti.shakti_employee.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shakti.shakti_employee.BuildConfig;
import shakti.shakti_employee.R;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.rest.ApiClient;
import shakti.shakti_employee.rest.ApiInterface;
import shakti.shakti_employee.retrofit_response.VersionResponse;
import shakti.shakti_employee.utility.Utility;


public class SplashActivity extends AppCompatActivity {
    final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
    ImageView imageView;
    DatabaseHelper databaseHelper;
    Intent i;
    String versionName = "0.0";
    String newVersion = "0.0";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;


        imageView = findViewById(R.id.imageSplash);

        versionName = BuildConfig.VERSION_NAME;

        databaseHelper = new DatabaseHelper(SplashActivity.this);

        new Worker1().execute();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

              /*  String data = null;
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);


                try {

                    if (CustomUtility.isInternetOn(mContext)) {

                        //********************************** check app version ********************************************
                        param.clear();

                        param.add(new BasicNameValuePair("app_version", ""));

                        String app_version_response = CustomHttpClient.executeHttpPost1(SapUrl.APP_VERSION, param);

                        JSONObject app_version_obj = new JSONObject(app_version_response);

                        newVersion = app_version_obj.getString("app_version");


                        Log.d("newVersion1", newVersion + "--" + versionName + "--" + app_version_obj);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
*/
                if (Float.parseFloat(newVersion) > Float.parseFloat(versionName)) {

                    SplashActivity.this.finish();
                    Intent i = new Intent(SplashActivity.this, UpdateActivity.class);
                    startActivity(i);

                } else {

                    Log.d("newVersion", newVersion + "--" + versionName);
                    if (Utility.isDateTimeAutoUpdate(mContext)) {

                        if (databaseHelper.getLogin()) {
                            i = new Intent(SplashActivity.this, DashboardActivity.class);
                        } else {

                            i = new Intent(SplashActivity.this, LoginActivity.class);
                        }
                        startActivity(i);
                        SplashActivity.this.finish();


//                    startActivity(LoginActivity.getIntent(mContext));
                    } else {
                        Utility.ShowToast("Date Time not auto update please check it.", mContext);
                        Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                        mContext.startActivity(intent);
                        finish();
                    }
                }
            }
        }, 1000);

    }

    public void getVersion() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<VersionResponse> call = apiService.getVersionCode();
        call.enqueue(new Callback<VersionResponse>() {
            @Override
            public void onResponse(@NonNull Call<VersionResponse> call, @NonNull Response<VersionResponse> response) {
                try {
                    VersionResponse dashResponse = response.body();
                    if (dashResponse != null) {

                        newVersion = dashResponse.getVersion();
                        Log.e("VERSION", "&&&&" + newVersion);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<VersionResponse> call, @NonNull Throwable t) {

                Toast.makeText(SplashActivity.this, "FAILED...", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private class Worker1 extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {

            String data = null;

            try {


                if (CustomUtility.isInternetOn(mContext)) {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                    StrictMode.setThreadPolicy(policy);

                    getVersion();


                }

            } catch (Exception e) {
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

}
