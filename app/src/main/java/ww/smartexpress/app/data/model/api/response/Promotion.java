package ww.smartexpress.app.data.model.api.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Promotion {
    private Long id;
    private String avatar;
    private String description;
    private String perfix;
    private Integer status;
    private String modifiedDate;
    private String createdDate;
    private String startDate;
    private String endDate;
    private String name;
    private Integer kind;
    private Integer state;
    private Double discountValue;
    private Double limitValueMax;
    private Double limitValueMin;
    private Integer quantity;
    private List<ServiceResponse> services;
    private Boolean isInValid;
    private Boolean isSelected;
}
