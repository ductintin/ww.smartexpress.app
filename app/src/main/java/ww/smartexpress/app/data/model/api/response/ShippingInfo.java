package ww.smartexpress.app.data.model.api.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingInfo extends BaseResponse implements Serializable{
    private String origin;
    private String originId;
    private String destination;
    private String destinationId;
    private String senderName;
    private String senderPhone;
    private String consigneeName;
    private String consigneePhone;
    private Boolean isCod;
    private Integer codPrice;
}
