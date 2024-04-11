package ww.smartexpress.app.data.model.api.response;

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
public class AddressMap {
    private String id;
    private String description;
    private Double lat;
    private Double lng;
    private Integer kind;
}
