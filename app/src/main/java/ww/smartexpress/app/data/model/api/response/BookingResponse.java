package ww.smartexpress.app.data.model.api.response;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse extends BaseResponse implements Serializable {
    private Long id;
    private Integer state;
    private String status;
    private String modifiedDate;
    private String createdDate;
    private Driver driver;
    private ProfileResponse customer;
    private ServiceResponse service;
    private String pickupAddress;
    private String destinationAddress;
    private Double pickupLat;
    private Double pickupLong;
    private Double destinationLat;
    private Double destinationLong;
    private Double distance;
    private Double money;
    private Double promotionMoney;
    private String code;
    private List<BookingDetails> bookingDetails;
    private String customerNote;
    private Room room;
    private RatingResponse rating;
    private DriverVehicle driverVehicle;
    private Double averageRating;

    private Double codPrice;
    private String consigneeName;
    private String consigneePhone;
    private Boolean isCod;

    private String pickupImage;
    private String deliveryImage;
    private String senderName;
    private String senderPhone;

    private Integer paymentKind;
}
