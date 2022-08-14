package academy.scalefocus.timeOffManagement.service;

import academy.scalefocus.timeOffManagement.model.TimeOff;
import academy.scalefocus.timeOffManagement.model.User;

public interface EmailService {
    public void sendTimeOffRequestEmail(User sender, User recipient, TimeOff timeOff);
}
