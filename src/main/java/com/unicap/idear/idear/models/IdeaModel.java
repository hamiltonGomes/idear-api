package com.unicap.idear.idear.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Data
@Entity
@EqualsAndHashCode(of = "idIdea")
@Table(name = "IDEAS")
@NoArgsConstructor
@AllArgsConstructor
public class IdeaModel extends RepresentationModel<MethodModel> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIdea;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonManagedReference
    @JsonIgnore
    private RoomModel room;

    private String ideaContent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    public IdeaModel(String ideaContent, UserModel user, RoomModel room) {
        this.ideaContent = ideaContent;
        this.user = user;
        this.room = room;
    }
}
