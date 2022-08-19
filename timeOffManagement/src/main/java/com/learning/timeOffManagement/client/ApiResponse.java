package com.learning.timeOffManagement.client;

import com.learning.timeOffManagement.client.responseObjects.Meta;
import com.learning.timeOffManagement.client.responseObjects.ResponseBody;
import lombok.Data;

@Data
public class ApiResponse {
    private Meta meta;
    private ResponseBody response;
}
