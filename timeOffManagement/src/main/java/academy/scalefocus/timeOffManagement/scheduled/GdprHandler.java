package academy.scalefocus.timeOffManagement.scheduled;

import academy.scalefocus.timeOffManagement.repository.TimeOffRepository;
import academy.scalefocus.timeOffManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
public class GdprHandler {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TimeOffRepository timeOffRepository;

    /**
     * Scheduled cron task which runs every midnight
     */
    @Scheduled(cron = "${crontrigger.midnight}")
    public void clearPersonalInfo(){
        clearFormerEmployees();
        clearOldTimeOffs();
    }

    /**
     * Clears all users, who aren't employed in the company for longer than 6 months
     */
    private void clearFormerEmployees(){
        userRepository.findAll().stream()
           .filter(user -> !user.isEmployed() && DAYS.between(user.getDateOfLastChange(), LocalDate.now()) >= 180L)
           .forEach(oldEmplyee -> userRepository.delete(oldEmplyee));
    }

    /**
     * Clears all timeoffs, that have ended before 6 months
     */
    private void clearOldTimeOffs(){
        timeOffRepository.findAll().stream()
            .filter(timeOff ->  DAYS.between(timeOff.getEndDate(), LocalDate.now()) >= 180L )
            .forEach(oldTimeOff -> timeOffRepository.delete(oldTimeOff));
    }
}
