package com.unicap.idear.idear.controllers;

import com.unicap.idear.idear.models.ProblemModel;
import com.unicap.idear.idear.services.ProblemService;
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
@RequestMapping("problems")
public class ProblemController {

    private final ProblemService problemService;

    @Autowired
    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ProblemModel>> getAllProblems() {
        List<ProblemModel> problemsList = problemService.getAllProblems();
        if (!problemsList.isEmpty()) {
            for (ProblemModel problem : problemsList) {
                Long idProblem = problem.getIdProblem();
                problem.add(linkTo(methodOn(ProblemController.class).getOneProblem(idProblem)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(problemsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneProblem(@PathVariable(value = "id") Long idProblem) {
        Optional<ProblemModel> problem = problemService.getOneProblem(idProblem);

        if (problem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Problem not found, the specified problem ID does not exist.");
        }
        problem.get().add(linkTo(methodOn(ProblemController.class).getAllProblems()).withSelfRel());

        return ResponseEntity.status(HttpStatus.OK).body(problem.get());
    }
}
