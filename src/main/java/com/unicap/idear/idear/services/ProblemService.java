package com.unicap.idear.idear.services;

import com.unicap.idear.idear.models.ProblemModel;
import com.unicap.idear.idear.models.TeamModel;
import com.unicap.idear.idear.repositories.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemService {

    private final ProblemRepository problemRepository;

    @Autowired
    public ProblemService(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    public List<ProblemModel> getAllProblems() {
        return problemRepository.findAll();
    }

    public Optional<ProblemModel> getOneProblem(long idProblem) {
        return problemRepository.findById(idProblem);
    }
}
