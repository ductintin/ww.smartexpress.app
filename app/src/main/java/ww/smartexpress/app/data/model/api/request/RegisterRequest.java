package ww.smartexpress.app.data.model.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest extends BaseBooking{
    private String name;
    private String email;
    private String phone;
    private String password;
}
