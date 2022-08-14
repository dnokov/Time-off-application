package academy.scalefocus.timeOffManagement.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Year;

@Component
public class HolidayApiClient {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${spring.holiday.api.url}")
    private String url;
    @Value("${spring.holiday.api.country}")
    private String country;
    @Value("${spring.holiday.api.key}")
    private String key;
    private String year;

    @Autowired
    public HolidayApiClient(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
        this.year = Year.now().toString();
    }

    /**
     * Used for getting holidays occurring on work days
     * @param start LocalDate, start of segment to check
     * @param end LocalDate, end of segment to check
     * @return LocalDate end of given segment
     */
    public int getHolidaysBetween(LocalDate start, LocalDate end){
        ApiResponse apiResponse = getHolyDays();
        return apiResponse.getResponse().getNumberOfHolidaysOccuringOnWorkDays(start, end);
    }

    @Cacheable("apiResponse")
    private ApiResponse getHolyDays(){
        ApiResponse apiResponse = restTemplate.getForObject(url, ApiResponse.class, key, country, year);
        return apiResponse;
    }
}
