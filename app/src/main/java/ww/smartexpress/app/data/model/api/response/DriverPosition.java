package ww.smartexpress.app.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverPosition {
    private String codeBooking;
    private String driverId;
    private String latitude;
    private String longitude;
    private String timeUpdate;
}
