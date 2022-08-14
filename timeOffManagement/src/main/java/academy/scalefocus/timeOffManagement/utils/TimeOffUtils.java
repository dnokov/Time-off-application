package academy.scalefocus.timeOffManagement.utils;

import academy.scalefocus.timeOffManagement.client.HolidayApiClient;
import academy.scalefocus.timeOffManagement.dto.TimeOffCreationDTO;
import academy.scalefocus.timeOffManagement.model.TimeOff;
import academy.scalefocus.timeOffManagement.model.TimeOffType;
import academy.scalefocus.timeOffManagement.model.User;
import java.time.DayOfWeek;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;


@Component
public class TimeOffUtils {
    private HolidayApiClient holidayApiClient;

    public TimeOffUtils(HolidayApiClient holidayApiClient) {
        this.holidayApiClient = holidayApiClient;
    }

    public Boolean checkIfUserIsAbleToRequest(User user, TimeOffCreationDTO timeOffCreationDTO) {

        int days = calculateDays(timeOffCreationDTO.getStartDate(), timeOffCreationDTO.getEndDate());

        switch (timeOffCreationDTO.getType()) {
            case "SICK_LEAVE":
                return days > user.getSickLeave();
            case "PAID":
                return days > user.getPaidLeave();
            case "UNPAID":
                return days > user.getUnpaidLeave();
        }

        return false;
    }

    public Boolean compareDates(LocalDate startDate, LocalDate endDate) {

        return startDate.isBefore(endDate) || startDate.isEqual(endDate);
    }

    public User reduceDays(User user, TimeOff timeOff) {
        int days = calculateDays(timeOff.getStartDate(), timeOff.getEndDate());

        switch (timeOff.getType()) {
            case SICK_LEAVE:
                user.setSickLeave(Math.abs(user.getSickLeave() - days));
                break;
            case PAID:
                user.setPaidLeave(Math.abs(user.getPaidLeave() - days));
                break;
            case UNPAID:
                user.setUnpaidLeave(Math.abs(user.getUnpaidLeave() - days));
                break;
        }

        return user;
    }

    private int calculateDays(LocalDate startDate, LocalDate endDate) {

        int businessDays = (int) DAYS.between(startDate, endDate) + 1;

        for (LocalDate i = startDate; i.isBefore(endDate) || i.isEqual(endDate); i = i.plusDays(1)) {
            DayOfWeek day = i.getDayOfWeek();
            if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                businessDays--;
            }
        }

        businessDays -= holidayApiClient.getHolidaysBetween(startDate, endDate);
        return businessDays;
    }
}
