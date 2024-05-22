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
public class ServicePromotion extends BaseResponse{
    private Double money;
    private ServiceResponse service;
    private Long selectedId;
}
