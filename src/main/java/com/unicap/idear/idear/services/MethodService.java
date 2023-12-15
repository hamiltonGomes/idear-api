package com.unicap.idear.idear.services;

import com.unicap.idear.idear.dtos.MethodRecordDto;
import com.unicap.idear.idear.models.MethodModel;
import com.unicap.idear.idear.repositories.MethodRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MethodService {

    private final MethodRepository methodRepository;

    @Autowired
    public MethodService(MethodRepository methodRepository) {
        this.methodRepository = methodRepository;
    }

    public List<MethodModel> getAllMethods() {
        return methodRepository.findAll();
    }

    public Optional<MethodModel> getOneMethod(long idMethod) {
        return methodRepository.findById(idMethod);
    }

    public MethodModel saveMethod(MethodRecordDto methodRecordDto) {
        var methodModel = new MethodModel();
        BeanUtils.copyProperties(methodRecordDto, methodModel);
        return methodRepository.save(methodModel);
    }

    public Optional<MethodModel> updateMethod(long idMethod, MethodRecordDto methodRecordDto) {
        Optional<MethodModel> optionalMethod = methodRepository.findById(idMethod);
        if (optionalMethod.isPresent()) {
            MethodModel methodModel = optionalMethod.get();
            BeanUtils.copyProperties(methodRecordDto, methodModel);
            return Optional.of(methodRepository.save(methodModel));
        }
        return Optional.empty();
    }

    public boolean deleteMethod(Long idMethod) {
        Optional<MethodModel> optionalMethod = methodRepository.findById(idMethod);
        if (optionalMethod.isPresent()) {
            methodRepository.delete(optionalMethod.get());
            return true;
        }
        return false;
    }
}
