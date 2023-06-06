package shakti.shakti_employee.utility;

import java.util.Map;

import models.DistanceResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface DistanceApiClient {
    @GET("maps/api/directions/json")
    Call<DistanceResponse> getDistanceInfo(
            @QueryMap Map<String, String> parameters
    );
}
