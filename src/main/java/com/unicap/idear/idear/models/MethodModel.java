package com.unicap.idear.idear.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unicap.idear.idear.dtos.MethodRecordDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "idMethod")
@Table(name = "METHODS")
@NoArgsConstructor
@AllArgsConstructor
public class MethodModel extends RepresentationModel<MethodModel> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long methodId;

    private String methodName;
    private String methodDescription;
    private String methodImage;

    @JsonIgnore
    @OneToMany(mappedBy = "methodModel")
    private List<RoomModel> roomList;

    public MethodModel(MethodRecordDto methodRecordDto) {
        this.methodName = methodRecordDto.methodName();
        this.methodDescription = methodRecordDto.methodDescription();
        this.methodImage = methodRecordDto.methodImage();
    }
}
