package com.learning.timeOffManagement.service;

import com.learning.timeOffManagement.model.TimeOff;
import com.learning.timeOffManagement.model.User;
import com.learning.timeOffManagement.utils.EmailGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Value("${spring.mail.username}")
    private String email;
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private EmailGenerator emailGenerator;

    /**
     * Method which sends a time off request email to all of the team leaders of a user
     *
     * @param sender  person who requests a timeoff
     * @param timeOff timeoffObject
     */
    @Override
    public void sendTimeOffRequestEmail(User sender, User recipient, TimeOff timeOff) {
        SimpleMailMessage message = generateTimeOffMessage(sender, recipient, timeOff);
        emailSender.send(message);
    }

    /**
     * Method which generates a time off request message
     *
     * @return message object which contains the information about a time off request
     */
    private SimpleMailMessage generateTimeOffMessage(User sender, User recipient, TimeOff timeOff) {
        SimpleMailMessage message = new SimpleMailMessage();
        String messageContent = generateTextBody(sender, recipient, timeOff);
        message.setFrom(email);
        message.setTo(recipient.getEmail());
        message.setSubject(timeOff.getType() + " request");
        message.setText(messageContent);
        return message;
    }


    /**
     * Method which generates the body of a time off request email
     *
     * @return a string which represents the body of the request
     */
    private String generateTextBody(User sender, User recipient, TimeOff timeOff) {

        return emailGenerator.generateEmailType(sender, recipient, timeOff);
    }
}

