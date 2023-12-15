package com.unicap.idear.idear.controllers;

import com.unicap.idear.idear.models.TeamModel;
import com.unicap.idear.idear.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("teams")
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/")
    public ResponseEntity<List<TeamModel>> getAllTeams() {
        List<TeamModel> teamsList = teamService.getAllTeams();
        if (!teamsList.isEmpty()) {
            for (TeamModel team : teamsList) {
                Long idTeam = team.getIdTeam();
                team.add(linkTo(methodOn(TeamController.class).getOneTeam(idTeam)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(teamsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneTeam(@PathVariable(value = "id") Long idTeam) {
        Optional<TeamModel> team = teamService.getOneTeam(idTeam);
        if (team.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found, the specified team ID does not exist.");
        }
        team.get().add(linkTo(methodOn(TeamController.class).getAllTeams()).withSelfRel());

        return ResponseEntity.status(HttpStatus.OK).body(team.get());
    }
}
