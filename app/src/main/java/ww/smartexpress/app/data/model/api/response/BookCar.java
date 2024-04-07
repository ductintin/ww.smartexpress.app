package ww.smartexpress.app.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCar {
    private Long serviceId;
    private String name;
    private String image;
    private Double price;
    private Double discount;
    private String size;
    private String weight;
}
