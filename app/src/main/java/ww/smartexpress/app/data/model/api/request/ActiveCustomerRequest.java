package ww.smartexpress.app.data.model.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActiveCustomerRequest {
    private String otp;
    private String userId;
}
