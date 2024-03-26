package ww.smartexpress.app.data.model.api.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponse implements Serializable {
    private Long id;
    private String name;
    private String description;
    private String image;
    private String price;
    private Integer kind;
    private CategoryResponse category;
}
