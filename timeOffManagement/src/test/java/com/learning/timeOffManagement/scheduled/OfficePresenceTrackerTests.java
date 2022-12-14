package com.learning.timeOffManagement.scheduled;

import com.learning.timeOffManagement.model.TimeOff;
import com.learning.timeOffManagement.model.TimeOffStatus;
import com.learning.timeOffManagement.model.TimeOffType;
import com.learning.timeOffManagement.model.User;
import com.learning.timeOffManagement.repository.TimeOffRepository;
import com.learning.timeOffManagement.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;


@SpringBootTest
@ActiveProfiles("test")
public class OfficePresenceTrackerTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TimeOffRepository timeOffRepository;

    @Test
    @DirtiesContext
    public void userInOfficeShouldBeSetToFalseWhenUserIsOnPaidLeave() throws InterruptedException {
        TimeUnit.SECONDS.sleep(70);
        boolean actualOfficeStatus = userRepository.findByUsername("test").get().isInOffice();
        Assert.assertEquals(false, actualOfficeStatus);
    }

    @Test
    @DirtiesContext
    public void userInOfficeShouldBeSetToFalse() throws InterruptedException{
        TimeUnit.SECONDS.sleep(70);
        boolean actualOfficeStatus = userRepository.findByUsername("admin").get().isInOffice();
        Assert.assertEquals(true, actualOfficeStatus);
    }

    @BeforeEach
    private void  insertUser(){
        User testUser = new User("test", "password", "firstname","lastname", null, true, "test@gmail.com");
        TimeOff testUserTimeoff = new TimeOff(1L, TimeOffType.PAID, "I'm tired" ,TimeOffStatus.APPROVED, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), testUser, null);
        testUser.getRequests().add(testUserTimeoff);
        userRepository.save(testUser);
        timeOffRepository.save(testUserTimeoff);
    }
}
