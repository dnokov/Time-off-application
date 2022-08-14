package academy.scalefocus.timeOffManagement.utils;
import academy.scalefocus.timeOffManagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class RootUsersInitializerTest {
    @Autowired
    UserRepository userRepository;

    @Test
    public void rootAdminAndCeoUserAreCreatedIfNoUsersArePresentInRepository(){
        int expectedUsersCount = 2;
        int actualUsersCount = userRepository.findAll().size();
        assertEquals(expectedUsersCount, actualUsersCount);
    }
}
