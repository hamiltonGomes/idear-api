package com.unicap.idear.idear.controllers;

import com.unicap.idear.idear.models.IdeaModel;
import com.unicap.idear.idear.services.IdeaService;
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
@RequestMapping("ideas")
public class IdeaController {

    private final IdeaService ideaService;

    @Autowired
    public IdeaController(IdeaService ideaService) {
        this.ideaService = ideaService;
    }

    @GetMapping("/")
    public ResponseEntity<List<IdeaModel>> getAllIdeas() {
        List<IdeaModel> ideasList = ideaService.getAllIdeas();
        if (!ideasList.isEmpty()) {
            for (IdeaModel idea : ideasList) {
                Long idIdea = idea.getIdIdea();
                idea.add(linkTo(methodOn(IdeaController.class).getOneIdea(idIdea)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(ideasList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneIdea(@PathVariable(value = "id") Long idIdea) {
        Optional<IdeaModel> idea = ideaService.getOneIdea(idIdea);
        if (idea.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Idea not found, the specified idea ID does not exist.");
        }
        idea.get().add(linkTo(methodOn(IdeaController.class).getAllIdeas()).withSelfRel());

        return ResponseEntity.status(HttpStatus.OK).body(idea.get());
    }
}
