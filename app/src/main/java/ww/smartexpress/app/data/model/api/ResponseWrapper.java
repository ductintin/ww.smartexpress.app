package ww.smartexpress.app.data.model.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapper<T> {
    private boolean result;
    private T data;
    private String message;
    private String code;
    private String firebaseUrl;
    private String urlBase;
}
