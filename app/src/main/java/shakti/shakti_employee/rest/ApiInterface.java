package shakti.shakti_employee.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import shakti.shakti_employee.retrofit_response.VersionResponse;


public interface ApiInterface {

    @GET("app_version.htm")
    Call<VersionResponse> getVersionCode();




}


