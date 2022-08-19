package com.learning.timeOffManagement.model;

import com.learning.timeOffManagement.exception.UserNotFoundException;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "teams")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"members", "teamLeader"})
@RequiredArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private Long id;

    @Column(name = "title", unique = true)
    @NonNull
    private String title;

    @Column(name = "description")
    @NonNull
    private String description;

    @CreationTimestamp
    @Column(name = "team_creation_date")
    private LocalDate dateOfCreation;

    @UpdateTimestamp
    @Column(name = "team_updated_date")
    private LocalDate dateOfLastChange;

    @Column(name = "team_leader_id")
    private Long teamLeader;

    //TODO: Fetchtype should not be eager, refactor with join fetch
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_teams",
            joinColumns = {@JoinColumn(name = "team_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> members = new HashSet<>();

    public User getTeamLeader() {
        return this.members.stream().filter(member -> member.getId().equals(teamLeader)).findFirst().get();
    }

    public void setTeamLeader(Long id) {
        boolean foundLeader = false;

        for (User member : members) {
            if (member.getId().equals(id)) {
                this.teamLeader = member.getId();
                foundLeader = true;
            }
        }

        if (!foundLeader) {
            throw new UserNotFoundException(id);
        }
    }

    public void addMember(User member) {
        this.members.add(member);
    }
}
