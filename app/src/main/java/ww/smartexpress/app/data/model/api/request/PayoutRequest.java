package ww.smartexpress.app.data.model.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayoutRequest {
    private String bankCard;
    private int money;
}
