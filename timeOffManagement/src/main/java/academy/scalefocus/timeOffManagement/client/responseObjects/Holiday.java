package academy.scalefocus.timeOffManagement.client.responseObjects;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Data
public class Holiday{
    private String name;
    private String description;
    private Country country;
    private Date date;
    private List<String> type;
    private String states;

    public boolean isBetween(LocalDate startDate, LocalDate endDate){
        LocalDate date = LocalDate.parse(this.date.getIso());
        return (date.isAfter(startDate) || date.isEqual(startDate)) && (date.isBefore(endDate) || date.isEqual(endDate));
    }

    public boolean isNotWeekend(){
        LocalDate date = LocalDate.parse(this.date.getIso());
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return  dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
}
