package ww.smartexpress.app.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Setting {
    private Long id;
    private String settingKey;
    private String settingValue;
    private String groupName;
}
