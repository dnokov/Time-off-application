package com.learning.timeOffManagement.service;

import com.learning.timeOffManagement.model.TimeOff;
import com.learning.timeOffManagement.model.User;

public interface EmailService {
    public void sendTimeOffRequestEmail(User sender, User recipient, TimeOff timeOff);
}
