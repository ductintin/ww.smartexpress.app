package ww.smartexpress.app.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverBookingResponse {
    private String driverId;
    private String driverLatitude;
    private String driverLongitude;
    private String codeBooking;
    private Long bookingId;
    private String pickupAddress;
    private String destinationAddress;
}
