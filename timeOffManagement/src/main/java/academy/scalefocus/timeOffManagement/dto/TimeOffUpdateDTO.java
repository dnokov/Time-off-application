package academy.scalefocus.timeOffManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeOffUpdateDTO implements Serializable {
    private Long id;
    /**
     * Type of timeoff request
     */
    private String type;

    private String reason;

    private LocalDate startDate;

    private LocalDate endDate;
}
