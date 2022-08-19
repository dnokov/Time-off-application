package com.learning.timeOffManagement.scheduled;

import com.learning.timeOffManagement.model.TimeOff;
import com.learning.timeOffManagement.model.TimeOffStatus;
import com.learning.timeOffManagement.model.User;
import com.learning.timeOffManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OfficePresenceTracker {
    @Autowired
    UserRepository userRepository;

    /**
     * Scheduled cron task which runs every midnight
     */
    @Scheduled(cron = "${crontrigger.midnight}")
    public void setOfficePresence(){
        List<User> users = userRepository.findAll().stream().filter(User::isEmployed).collect(Collectors.toList());
        users.forEach(this::setUserOfficeStatus);
    }

    private boolean todayIsInTimeOffRange(TimeOff timeOff){
        LocalDate today = LocalDate.now();
        boolean todayIsBeforeEndDate = (today.isBefore(timeOff.getEndDate()) || today.equals(timeOff.getEndDate()));
        boolean todayIsAfterStartDate = (today.isAfter(timeOff.getStartDate()) || today.equals(timeOff.getStartDate()));
        return todayIsAfterStartDate && todayIsBeforeEndDate;
    }

    private void setUserOfficeStatus(User user){
        boolean inOffice = true;
        List<TimeOff> timeOffs = user.getRequests().stream().filter(timeOff -> timeOff.getStatus().equals(TimeOffStatus.APPROVED)).collect(Collectors.toList());
        for(TimeOff timeoff : timeOffs){
            if(todayIsInTimeOffRange(timeoff)){
                inOffice = false;
                break;
            }
        }
        user.setInOffice(inOffice);
        userRepository.save(user);
    }
}
