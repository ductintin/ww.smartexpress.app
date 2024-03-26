package ww.smartexpress.app.data.model.api.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse implements Serializable {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String avatar;
    private Integer status;
}
