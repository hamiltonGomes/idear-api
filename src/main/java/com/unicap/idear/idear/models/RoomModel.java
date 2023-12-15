package com.unicap.idear.idear.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "ROOMS")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idRoom")
public class RoomModel extends RepresentationModel<RoomModel> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idRoom;

    @Column(unique = true)
    private Long accessCode;
    private String roomName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_method")
    private MethodModel methodModel;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_problem")
    private ProblemModel problemModel;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_team")
    private TeamModel teamModel;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<IdeaModel> ideas;

    private String solution;

    public RoomModel(String roomName, ProblemModel problemModel, TeamModel teamModel, Long accessCode) {
        this.roomName = roomName;
        this.problemModel = problemModel;
        this.teamModel = teamModel;
        this.accessCode = accessCode;
        this.methodModel = null;
        this.ideas = new ArrayList<>();
    }
}