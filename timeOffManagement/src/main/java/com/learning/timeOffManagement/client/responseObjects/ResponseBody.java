package com.learning.timeOffManagement.client.responseObjects;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class ResponseBody {
    List<Holiday> holidays;

    /**
     * Returns the number of holidays which occur on a workday between two dates
     * @param start LocalDate
     * @param end LocalDate
     * @return integer which represents the count of holidays
     */
    public int getNumberOfHolidaysOccuringOnWorkDays(LocalDate start, LocalDate end){
        AtomicInteger holidayCount = new AtomicInteger();
        holidays.forEach(h -> {
            if(h.isBetween(start, end) && h.isNotWeekend()){
                holidayCount.addAndGet(1);
            }
        });
        return holidayCount.get();
    }
}
