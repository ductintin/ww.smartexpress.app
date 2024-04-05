package ww.smartexpress.app.data.model.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateShippingRequest {
    private Double codPrice;
    private String consigneeName;
    private String consigneePhone;
    private String customerNote;
    private Boolean isCod;

    private String senderName;
    private String senderPhone;

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

}
