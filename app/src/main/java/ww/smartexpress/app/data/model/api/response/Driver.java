package ww.smartexpress.app.data.model.api.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Driver implements Serializable {
    private Long id;
    private String fullName;
    private String phone;
    private String address;
    private Integer status;
    private String avatar;
}
