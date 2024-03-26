package ww.smartexpress.app.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDetail {
    private Long id;
    private String msg;
    private Long receiver;
    private Long sender;
    private String senderAvatar;
    private Integer state;
    private String timeSend;
    private Room room;
}
