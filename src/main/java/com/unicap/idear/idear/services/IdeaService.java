package com.unicap.idear.idear.services;

import com.unicap.idear.idear.models.IdeaModel;
import com.unicap.idear.idear.repositories.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IdeaService {

    private final IdeaRepository ideaRepository;

    @Autowired
    public IdeaService(IdeaRepository ideaRepository) {
        this.ideaRepository = ideaRepository;
    }

    public List<IdeaModel> getAllIdeas(){
        return ideaRepository.findAll();
    }

    public Optional<IdeaModel> getOneIdea(long idIdea) {
        return ideaRepository.findById(idIdea);
    }
}
