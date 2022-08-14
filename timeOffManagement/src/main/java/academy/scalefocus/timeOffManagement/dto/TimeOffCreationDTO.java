package academy.scalefocus.timeOffManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeOffCreationDTO implements Serializable {
    /**
     * Type of timeoff request
     */
    @NotEmpty
    private String type;

    @NotEmpty
    private String reason;

    @NotNull(message = "start date must not be empty")
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private LocalDate startDate;

    @NotNull(message = "end date must not be empty")
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private LocalDate endDate;
}
