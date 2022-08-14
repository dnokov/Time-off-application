package academy.scalefocus.timeOffManagement.model;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "time_offs")
@Data
@AllArgsConstructor
public class TimeOff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TimeOffType type;

    private String reason;

    @Enumerated(EnumType.STRING)
    private TimeOffStatus status;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    private User creator;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "time_offs_users",
        joinColumns = {@JoinColumn(name = "time_off_id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> awaitingApproval;

    public TimeOff(){
        status = TimeOffStatus.CREATED;
        awaitingApproval = new ArrayList<>();
    }
}
