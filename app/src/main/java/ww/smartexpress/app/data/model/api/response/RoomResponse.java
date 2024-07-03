package ww.smartexpress.app.data.model.api.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private Long id;
    private int status;
    private String modifiedDate;
    private String createdDate;
    private String timeStart;
    private List<MessageChat> chatDetails;
    private Driver driver;
    private Customer customer;
}
