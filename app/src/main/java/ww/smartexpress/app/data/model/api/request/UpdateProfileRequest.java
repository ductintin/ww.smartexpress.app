package ww.smartexpress.app.data.model.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequest {
    private String avatar;

    private String name;

    private String newPassword;

    private String oldPassword;

    private String bankCard;
}
