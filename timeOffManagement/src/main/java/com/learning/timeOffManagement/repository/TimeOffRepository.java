package com.learning.timeOffManagement.repository;

import com.learning.timeOffManagement.model.TimeOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeOffRepository extends JpaRepository<TimeOff, Long> {

}
