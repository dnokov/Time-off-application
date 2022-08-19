package com.learning.timeOffManagement.utils;

import com.learning.timeOffManagement.client.HolidayApiClient;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class HolidayApiClientTests {

    @Autowired
    public HolidayApiClient holidayUtils;

    @Test
    public void testThatStatusIsOkay() {
        int expectedHolidayCount = 13;
        int actualHolidayCount = holidayUtils.getHolidaysBetween(LocalDate.parse("2021-01-01"), LocalDate.parse("2021-12-31"));
        Assert.assertEquals(expectedHolidayCount, actualHolidayCount);
    }
}
