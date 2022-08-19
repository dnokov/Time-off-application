package com.learning.timeOffManagement.service;

import com.learning.timeOffManagement.model.Team;
import com.learning.timeOffManagement.model.TimeOff;
import com.learning.timeOffManagement.model.TimeOffType;
import com.learning.timeOffManagement.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles("test")
public class EmailServiceImplTests {
    @Autowired
    EmailServiceImpl emailService;
    public static User user;
    public static Team team;
    public static TimeOff timeOff;
    public static User teamLeader;

    @Test
    public void sendMailShouldSendMail(){
        assertDoesNotThrow( () -> emailService.sendTimeOffRequestEmail(user, teamLeader, timeOff));
    }

    @BeforeAll
    public static void setUp(){
        String teamLeaderUsername = "recipient";
        user = new User("username", "password", "Subordinate", "Subordinatesson",
                 null, false, "timeoffmanagementjava@gmail.com");
        teamLeader = new User(teamLeaderUsername, "password", "Teamleader", "Teamleadersson",
                null, false, "timeoffmanagementjava@gmail.com");

        timeOff = new TimeOff();
        timeOff.setType(TimeOffType.SICK_LEAVE);
        timeOff.setReason("I've fallen and I can't get up");

        user.setId(1L);
        teamLeader.setId(2L);
        team = new Team("Tortuga", "Survivor is an old show.");
        team.addMember(user);
        team.addMember(teamLeader);
        team.setTeamLeader(teamLeader.getId());
    }
}
