package com.unicap.idear.idear.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "idTeam")
@Table(name = "teams")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TeamModel extends RepresentationModel<TeamModel> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idTeam;

    private String teamName;
    @ManyToMany
    @JoinTable(name = "teams_user", joinColumns = @JoinColumn(name = "id_team"), inverseJoinColumns = @JoinColumn(name = "id_user"))
    private List<UserModel> participants;

    @JsonIgnore
    @OneToOne(mappedBy = "teamModel")
    private RoomModel roomModel;

    public TeamModel(String teamName, List<UserModel> participants) {
        this.teamName = teamName;
        this.participants = participants;
    }
}
