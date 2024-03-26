package ww.smartexpress.app.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelReason {
    private String id;
    private String reason;
    private Boolean isSelected;

    @Override
    public String toString() {
        return reason;
    }
}
