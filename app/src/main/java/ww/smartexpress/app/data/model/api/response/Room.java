package ww.smartexpress.app.data.model.api.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    private Long id;
    private BookingResponse booking;
    private ProfileResponse customer;
    private Driver driver;
    private Integer status;
    private String timeEnd;
    private String timeStart;
    private String createdDate;
    private String modifiedDate;
    private List<ChatDetail> chatDetails;
}
