package ww.smartexpress.app.data.model.api.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {
    private String id;
    private String image;
    private String value;
    private String applyFor;
    private String expiry;
}
