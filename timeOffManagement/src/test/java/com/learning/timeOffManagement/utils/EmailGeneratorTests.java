package com.learning.timeOffManagement.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.learning.timeOffManagement.model.TimeOff;
import com.learning.timeOffManagement.model.TimeOffStatus;
import com.learning.timeOffManagement.model.TimeOffType;
import com.learning.timeOffManagement.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class EmailGeneratorTests {

    @Autowired
    private EmailGenerator emailGenerator;

    @ParameterizedTest
    @MethodSource("resultsProvider")
    void is_ValidGenerateEmailType(User sender, User recipient, TimeOff timeOff) {

        String[] expected = expectedResult(sender, recipient, timeOff);

        if (timeOff.getType().equals(TimeOffType.SICK_LEAVE)) {
            assertEquals(expected[0],
                emailGenerator.generateEmailType(sender, recipient, timeOff));
        } else if (timeOff.getStatus().equals(TimeOffStatus.APPROVED)) {
            assertEquals(expected[1],
                emailGenerator.generateEmailType(sender, recipient, timeOff));
        } else if (timeOff.getStatus().equals(TimeOffStatus.REJECTED)) {
            assertEquals(expected[2],
                emailGenerator.generateEmailType(sender, recipient, timeOff));
        } else if (timeOff.getStatus().equals(TimeOffStatus.AWAITING) ||
            timeOff.getStatus().equals(TimeOffStatus.CREATED)) {
            assertEquals(expected[3],
                emailGenerator.generateEmailType(sender, recipient, timeOff));
        }
    }

    private static Stream<Arguments> resultsProvider() {
        return Stream.of(
            Arguments.of(
                new User("testSender", "12345678",
                    "Probcho", "Probchov", 5L, false,
                    "test@aaa.bg"),
                new User("testRecipient", "12345678",
                    "Recieptcho", "Recieptchov", 5L, false,
                    "test@aaa.bg"),
                new TimeOff(1L, TimeOffType.SICK_LEAVE, "covid",
                    TimeOffStatus.AWAITING, LocalDate.of(2021, 9, 1),
                    LocalDate.of(2021, 9, 10), new User(), new ArrayList<>())
            ),
            Arguments.of(
                new User("testSender", "12345678",
                    "Probcho", "Probchov", 5L, false,
                    "test@aaa.bg"),
                new User("testRecipient", "12345678",
                    "Recieptcho", "Recieptchov", 5L, false,
                    "test@aaa.bg"),
                new TimeOff(1L, TimeOffType.PAID, "covid",
                    TimeOffStatus.APPROVED, LocalDate.of(2021, 9, 1),
                    LocalDate.of(2021, 9, 10), new User(), new ArrayList<>())
            ),
            Arguments.of(
                new User("testSender", "12345678",
                    "Probcho", "Probchov", 5L, false,
                    "test@aaa.bg"),
                new User("testRecipient", "12345678",
                    "Recieptcho", "Recieptchov", 5L, false,
                    "test@aaa.bg"),
                new TimeOff(1L, TimeOffType.PAID, "covid",
                    TimeOffStatus.REJECTED, LocalDate.of(2021, 9, 1),
                    LocalDate.of(2021, 9, 10), new User(), new ArrayList<>())
            ),
            Arguments.of(
                new User("testSender", "12345678",
                    "Probcho", "Probchov", 5L, false,
                    "test@aaa.bg"),
                new User("testRecipient", "12345678",
                    "Recieptcho", "Recieptchov", 5L, false,
                    "test@aaa.bg"),
                new TimeOff(1L, TimeOffType.PAID, "covid",
                    TimeOffStatus.AWAITING, LocalDate.of(2021, 9, 1),
                    LocalDate.of(2021, 9, 10), new User(), new ArrayList<>())
            )
        );
    }

    private static String[] expectedResult(User sender, User recipient, TimeOff timeOff) {

        String senderFullName = sender.getFirstName() + " " + sender.getLastName();
        String recipientFullName = recipient.getFirstName() + " " + recipient.getLastName();
        String duration = timeOff.getStartDate() + " to " + timeOff.getEndDate();
        String typeAndReason = timeOff.getType() + " due to:" + timeOff.getReason();

        return new String[]{
            //SICK_LEAVE
            String.format("Hello %s, \n %s's request   %s  from %s was approved.",
                recipientFullName, senderFullName, typeAndReason, duration),
            //STATUS APPROVED
            String.format("Hello %s, \n Your %s from %s was approved.",
                recipientFullName, typeAndReason, duration),
            //STATUS REJECTED
            String.format("Hello %s, \n Your  %s from %s was rejected from %s .",
                recipientFullName, typeAndReason, duration, senderFullName),
            //URL GENERATOR
            String.format("Hello %s, \n %s has requested a %s from %s",
                recipientFullName, senderFullName, typeAndReason, duration) +
                "\n Click here to approve http://localhost:8080/timeoffs/" + timeOff.getId()
                + "/approve?value=true" +
                "\n Click here to reject http://localhost:8080/timeoffs/" + timeOff.getId()
                + "/approve?value=false"
        };
    }
}
