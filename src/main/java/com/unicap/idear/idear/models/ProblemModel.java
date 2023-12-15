package com.unicap.idear.idear.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unicap.idear.idear.dtos.ProblemDataDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "idProblem")
@NoArgsConstructor
@AllArgsConstructor
public class ProblemModel extends RepresentationModel<ProblemModel> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProblem;

    private String problemTitle;
    private String problemDescription;

    @JsonIgnore
    @OneToOne(mappedBy = "problemModel")
    private RoomModel roomModel;

    public ProblemModel(ProblemDataDto problemDataDto) {
        this.problemTitle = problemDataDto.problemTitle();
        this.problemDescription = problemDataDto.problemDescription();
    }
}
