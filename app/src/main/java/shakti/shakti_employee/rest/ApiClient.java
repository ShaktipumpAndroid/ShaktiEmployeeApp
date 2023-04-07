package shakti.shakti_employee.rest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    private static final String URL = "http://shaktidev.shaktipumps.com:8000/sap(bD1lbiZjPTkwMA==)/bc/bsp/sap/zhr_emp_app_1/";  ////// server

    //private static final String URL = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zhr_emp_app_1/";  ////// server
    //private static final String URL = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zemp_ref_app/";  ////// server
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiClient.URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
