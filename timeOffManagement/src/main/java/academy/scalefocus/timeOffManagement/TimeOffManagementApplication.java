package academy.scalefocus.timeOffManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TimeOffManagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(TimeOffManagementApplication.class, args);
	}
}
