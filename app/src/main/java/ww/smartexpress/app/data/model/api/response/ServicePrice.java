package ww.smartexpress.app.data.model.api.response;

import java.util.List;

import lombok.Data;

@Data
public class ServicePrice {
    private StartPrice startPrice;
    private List<Prices> prices;
    private Integer nextPrices;
    @Data
    public class StartPrice{
        private Integer distance;
        private Integer price;
    }

    @Data
    public class Prices{
        private Integer from;
        private Integer to;
        private Integer price;
    }

}
