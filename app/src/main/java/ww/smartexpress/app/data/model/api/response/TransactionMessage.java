package ww.smartexpress.app.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionMessage extends BaseResponse{

    private MessageTrans message;
    private String app;
    private Integer kind;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class MessageTrans{
        private Long notificationId;
        private Long transactionId;
        private Long money;
    }
}
