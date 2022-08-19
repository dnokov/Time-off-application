package com.learning.timeOffManagement.utils;

import static org.junit.jupiter.api.Assertions.*;

import com.learning.timeOffManagement.client.HolidayApiClient;
import com.learning.timeOffManagement.dto.TimeOffCreationDTO;
import com.learning.timeOffManagement.dto.mappers.TimeOffMapper;
import com.learning.timeOffManagement.model.TimeOff;
import com.learning.timeOffManagement.model.TimeOffType;
import com.learning.timeOffManagement.model.User;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class TimeOffUtilsTests {

    @Autowired
    private TimeOffMapper timeOffMapper;

    @Autowired
    private HolidayApiClient holidayApiClient;

    private TimeOffUtils timeOffUtils;

    private TimeOffCreationDTO timeOffCreationDTO;
    private User user;

    @BeforeEach
    public void setUp () {
        user = new User();

        timeOffCreationDTO = new TimeOffCreationDTO();
        timeOffCreationDTO.setReason("testing reason");
        timeOffCreationDTO.setStartDate(LocalDate.of(2021, 8, 1));
        timeOffCreationDTO.setEndDate(LocalDate.of(2021, 8, 15));

        timeOffUtils = new TimeOffUtils(holidayApiClient);
    }

    //user paid leave = 5
    //timeoff request = 15
    //result -> true
    @Test
    public void checkIfUserIsAbleToRequestPaidLeaveShouldReturnTrueWhenPaidIsLess () {
        timeOffCreationDTO.setType("PAID");
        user.setPaidLeave(5);
        assertTrue(timeOffUtils.checkIfUserIsAbleToRequest(user, timeOffCreationDTO));
    }

    //user paid leave = 20
    //timeoff request = 15
    //result -> false
    @Test
    public void checkIfUserIsAbleToRequestPaidLeaveShouldReturnFalseWhenRequestIsMore () {
        timeOffCreationDTO.setType("PAID");
        assertFalse(timeOffUtils.checkIfUserIsAbleToRequest(user, timeOffCreationDTO));
    }

    //user paid leave = 20
    //timeoff request = 20
    //result -> false
    @Test
    public void checkIfUserIsAbleToRequestPaidLeaveShouldReturnFalseWhenEquals () {
        timeOffCreationDTO.setType("PAID");
        timeOffCreationDTO.setEndDate(LocalDate.of(2021, 8, 20));
        assertFalse(timeOffUtils.checkIfUserIsAbleToRequest(user, timeOffCreationDTO));
    }

    //user unpaid leave = 90
    //timeoff request = 15
    //result -> false
    @Test
    public void checkIfUserIsAbleToRequestUnpaidLeaveShouldReturnFalseWhenUnpaidMore () {
        timeOffCreationDTO.setType("UNPAID");
        assertFalse(timeOffUtils.checkIfUserIsAbleToRequest(user, timeOffCreationDTO));
    }

    //user unpaid leave = 2
    //timeoff request = 15
    //result -> true
    @Test
    public void checkIfUserIsAbleToRequestUnpaidLeaveShouldReturnTrueWhenRequestMore () {
        timeOffCreationDTO.setType("UNPAID");
        user.setUnpaidLeave(2);
        assertTrue(timeOffUtils.checkIfUserIsAbleToRequest(user, timeOffCreationDTO));
    }

    //user unpaid leave = 5
    //timeoff request = 5
    //result -> false
    @Test
    public void checkIfUserIsAbleToRequestUnpaidLeaveShouldReturnFalseWhenEquals () {
        timeOffCreationDTO.setType("UNPAID");
        user.setUnpaidLeave(5);
        timeOffCreationDTO.setEndDate(LocalDate.of(2021, 8, 5));
        assertFalse(timeOffUtils.checkIfUserIsAbleToRequest(user, timeOffCreationDTO));
    }

    //user sick leave = 5
    //timeoff request = 5
    //result -> false
    @Test
    public void checkIfUserIsAbleToRequestSickLeaveShouldReturnFalseWhenEquals () {
        timeOffCreationDTO.setType("SICK_LEAVE");
        user.setSickLeave(5);
        timeOffCreationDTO.setEndDate(LocalDate.of(2021, 8, 5));
        assertFalse(timeOffUtils.checkIfUserIsAbleToRequest(user, timeOffCreationDTO));
    }

    //user sick leave = 10
    //timeoff request = 5
    //result -> false
    @Test
    public void checkIfUserIsAbleToRequestSickLeaveShouldReturnFalseWhenSickIsMore () {
        timeOffCreationDTO.setType("SICK_LEAVE");
        user.setSickLeave(10);
        timeOffCreationDTO.setEndDate(LocalDate.of(2021, 8, 5));
        assertFalse(timeOffUtils.checkIfUserIsAbleToRequest(user, timeOffCreationDTO));
    }

    //user sick leave = 5
    //timeoff request = 15
    //result -> true
    @Test
    public void checkIfUserIsAbleToRequestSickLeaveShouldReturnTrueWhenRequestIsMore () {
        timeOffCreationDTO.setType("SICK_LEAVE");
        user.setSickLeave(5);
        assertTrue(timeOffUtils.checkIfUserIsAbleToRequest(user, timeOffCreationDTO));
    }

    @Test
    public void compareDatesShouldReturnTrue () {
        assertTrue(timeOffUtils.compareDates(timeOffCreationDTO.getStartDate(),
            timeOffCreationDTO.getEndDate()));
    }

    @Test
    public void compareDatesShouldReturnFalse () {
        assertFalse(timeOffUtils.compareDates(timeOffCreationDTO.getEndDate(),
            timeOffCreationDTO.getStartDate()));
    }

    @ParameterizedTest
    @MethodSource("resultsProviderPaidLeave")
    void is_ValidPaidLeave(int assumed, TimeOffType timeOffType, LocalDate start,
        LocalDate end) {
        user = new User();

        TimeOff testTimeOff = new TimeOff();
        testTimeOff.setType(timeOffType);
        testTimeOff.setStartDate(start);
        testTimeOff.setEndDate(end);

        assertEquals(assumed, timeOffUtils.reduceDays(user, testTimeOff).getPaidLeave());
    }

    private static Stream<Arguments> resultsProviderPaidLeave() {
        return Stream.of(
            Arguments.of(10, TimeOffType.PAID, LocalDate.of(2021, 9, 1),
                LocalDate.of(2021, 9, 15)),
            Arguments.of(17, TimeOffType.PAID, LocalDate.of(2021, 9, 1),
                LocalDate.of(2021, 9, 5)),
            Arguments.of(6, TimeOffType.PAID, LocalDate.of(2021, 9, 1),
                LocalDate.of(2021, 9, 21)),
            Arguments.of(9, TimeOffType.PAID, LocalDate.of(2021, 7, 11),
                LocalDate.of(2021, 7, 26)),
            Arguments.of(19, TimeOffType.PAID, LocalDate.of(2021, 7, 13),
                LocalDate.of(2021, 7, 13)),
            Arguments.of(20, TimeOffType.PAID, LocalDate.of(2021, 7, 11),
                LocalDate.of(2021, 7, 11))
        );
    }
}

