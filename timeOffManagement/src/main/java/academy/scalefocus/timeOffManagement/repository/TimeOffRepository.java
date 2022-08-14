package academy.scalefocus.timeOffManagement.repository;

import academy.scalefocus.timeOffManagement.model.TimeOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeOffRepository extends JpaRepository<TimeOff, Long> {

}
