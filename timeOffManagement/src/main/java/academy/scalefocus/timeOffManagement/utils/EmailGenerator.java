package academy.scalefocus.timeOffManagement.utils;

import academy.scalefocus.timeOffManagement.model.TimeOff;
import academy.scalefocus.timeOffManagement.model.TimeOffStatus;
import academy.scalefocus.timeOffManagement.model.TimeOffType;
import academy.scalefocus.timeOffManagement.model.User;
import org.springframework.stereotype.Component;

@Component
public class EmailGenerator {

    public String generateEmailType(User sender, User recipient, TimeOff timeOff) {
        String senderFullName = sender.getFirstName() + " " + sender.getLastName();
        String recipientFullName = recipient.getFirstName() + " " + recipient.getLastName();
        String duration = timeOff.getStartDate() + " to " + timeOff.getEndDate();
        String typeAndReason = timeOff.getType() + " due to:" + timeOff.getReason();

        if (timeOff.getType().equals(TimeOffType.SICK_LEAVE)) {
            return String.format("Hello %s, \n %s's request   %s  from %s was approved.",
                    recipientFullName, senderFullName, typeAndReason, duration);
        }

        if (timeOff.getStatus().equals(TimeOffStatus.APPROVED)) {
            return String.format("Hello %s, \n Your %s from %s was approved.", recipientFullName, typeAndReason, duration);
        }

        if (timeOff.getStatus().equals(TimeOffStatus.REJECTED)) {
            return String.format("Hello %s, \n Your  %s from %s was rejected from %s .", recipientFullName, typeAndReason, duration, senderFullName);
        }

        return String.format("Hello %s, \n %s has requested a %s from %s", recipientFullName, senderFullName,
                typeAndReason, duration) + generateURL(timeOff);
    }

    private String generateURL(TimeOff timeOff) {
        return "\n Click here to approve http://localhost:8080/timeoffs/" + timeOff.getId() + "/approve?value=true" +
                "\n Click here to reject http://localhost:8080/timeoffs/" + timeOff.getId() + "/approve?value=false";
    }
}
