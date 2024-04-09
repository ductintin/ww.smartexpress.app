package ww.smartexpress.app.data.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchLocation {
    private String id;
    private String name;
    private String address;
    private String description;
    private String place_id;
    private String reference;
    private Structure structured_formatting;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Structure{
        private String main_text;
        private String secondary_text;
    }
}
