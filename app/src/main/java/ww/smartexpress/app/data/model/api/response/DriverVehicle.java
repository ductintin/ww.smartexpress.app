package ww.smartexpress.app.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverVehicle {
    private Long id;
    private String image;
    private String licenseNo;
    private String name;
    private String plate;
    private CategoryVehicle brand;
}
