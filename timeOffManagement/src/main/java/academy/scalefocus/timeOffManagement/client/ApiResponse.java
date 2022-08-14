package academy.scalefocus.timeOffManagement.client;

import academy.scalefocus.timeOffManagement.client.responseObjects.Meta;
import academy.scalefocus.timeOffManagement.client.responseObjects.ResponseBody;
import lombok.Data;

@Data
public class ApiResponse {
    private Meta meta;
    private ResponseBody response;
}
