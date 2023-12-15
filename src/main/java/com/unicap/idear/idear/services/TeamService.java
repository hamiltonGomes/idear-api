package com.unicap.idear.idear.services;

import com.unicap.idear.idear.models.TeamModel;
import com.unicap.idear.idear.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<TeamModel> getAllTeams(){
        return teamRepository.findAll();
    }

    public Optional<TeamModel> getOneTeam(long idTeam) {
        return teamRepository.findById(idTeam);
    }
}
