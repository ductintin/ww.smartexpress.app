package ww.smartexpress.app.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountResponse {
    private String code;
    private String desc;
    private AccountName data;
}
