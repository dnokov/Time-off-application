package academy.scalefocus.timeOffManagement.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(exclude="teams")
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDate dateOfCreation;

    @Column(name = "creator_id")
    private Long creatorId;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDate dateOfLastChange;

    @Column(name = "updated_by")
    private Long editedBy;

    @Column(name = "is_admin", columnDefinition = "boolean default true")
    private Boolean isAdmin;

    @Column(name = "paid_leave")
    int paidLeave;

    @Column(name = "unpaid_leave")
    int unpaidLeave;

    @Column(name = "sick_leave")
    int sickLeave;

    @Email
    @NotEmpty
    @Column(name = "email", unique = true)
    private String email;

    @ManyToMany(mappedBy = "members", fetch = FetchType.EAGER)
    private Set<Team> teams;

    @OneToMany(mappedBy = "creator", fetch = FetchType.EAGER)
    private List<TimeOff> requests;

    @Column(columnDefinition = "boolean default true")
    private boolean employed;

    @Column(columnDefinition = "boolean default true")
    private boolean inOffice;


    public User(){
        this.paidLeave = 20;
        this.unpaidLeave = 90;
        this.sickLeave = 40;
        this.dateOfCreation = LocalDate.now();
        this.dateOfLastChange = LocalDate.now();
        this.inOffice = true;
        this.employed = true;
        this.requests = new ArrayList<>();
        this.teams = new HashSet<>();
    }
    /**
     * Used for creating a user relation
     *
     * @param username
     * @param password
     * @param firstName
     * @param lastName
     * @param creatorId
     * @param isAdmin
     * @param email
     */
    public User(String username, String password, String firstName, String lastName,
                Long creatorId, Boolean isAdmin, String email) {
        this();
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.creatorId = creatorId;
        this.editedBy = creatorId;
        this.isAdmin = isAdmin;
        this.email = email;
    }

    public List<User> getTeamLeaders(){
        return this.getTeams().stream().map(Team::getTeamLeader).collect(Collectors.toList());
    }
}
