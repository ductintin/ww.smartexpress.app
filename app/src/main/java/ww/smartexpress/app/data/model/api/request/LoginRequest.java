package ww.smartexpress.app.data.model.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest extends BaseBooking{
//    @SerializedName("grant_type")
//    private String grantType = "password";
    private String phone;
    private String password;
}
