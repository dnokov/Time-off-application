package academy.scalefocus.timeOffManagement.exception.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ValidationErrorDetails {

    @JsonProperty
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private final LocalDateTime timestamp;

    @JsonProperty("uri")
    private final String uriRequested;

    @JsonProperty
    private Map<String, String> messages;

    public ValidationErrorDetails(String uriRequested, Map<String, String> messages) {
        this.uriRequested = uriRequested;
        this.timestamp = LocalDateTime.now();
        this.messages = messages;
    }
}
