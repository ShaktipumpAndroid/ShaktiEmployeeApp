package shakti.shakti_employee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Trip_data_Array {

    @SerializedName("travel_head")
    @Expose
    private TripHeadResponse[] tripHeadResponses;

    @SerializedName("travel_item")
    @Expose
    private TripListResponse[] tripListResponses;


    public TripHeadResponse[] getTripHeadResponses() {
        return tripHeadResponses;
    }

    public void setTripHeadResponses(TripHeadResponse[] tripHeadResponses) {
        this.tripHeadResponses = tripHeadResponses;
    }

    public TripListResponse[] getTripListResponses() {
        return tripListResponses;
    }

    public void setTripListResponses(TripListResponse[] tripListResponses) {
        this.tripListResponses = tripListResponses;
    }
}
