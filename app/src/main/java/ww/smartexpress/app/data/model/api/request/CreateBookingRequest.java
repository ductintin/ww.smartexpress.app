package ww.smartexpress.app.data.model.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {
    private String pickupAddress;
    private String destinationAddress;
    private Double pickupLat;
    private Double pickupLong;
    private Double destinationLat;
    private Double destinationLong;
    private Double distance;
    private Double money;
    private Double promotionMoney;
    private Long serviceId;
    private String customerNote;
}
