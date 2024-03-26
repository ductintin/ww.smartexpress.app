package ww.smartexpress.app.data.model.api.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderOption {
    private String id;
    private String name;
    private Boolean isOptional;
    private List<OrderOptionDetail> optionDetailList;
}
