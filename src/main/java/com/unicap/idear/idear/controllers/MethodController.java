package com.unicap.idear.idear.controllers;

import com.unicap.idear.idear.dtos.MethodRecordDto;
import com.unicap.idear.idear.models.MethodModel;
import com.unicap.idear.idear.services.MethodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class MethodController {

    private final MethodService methodService;

    @Autowired
    public MethodController(MethodService methodService) {
        this.methodService = methodService;
    }

    @GetMapping("/methods")
    public ResponseEntity<List<MethodModel>> getAllMethods() {
        List<MethodModel> methodsList = methodService.getAllMethods();
        if (!methodsList.isEmpty()) {
            for (MethodModel method : methodsList) {
                Long idMethod = method.getMethodId();
                method.add(linkTo(methodOn(MethodController.class).getOneMethod(idMethod)).withSelfRel());
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(methodsList);
    }

    @GetMapping("/methods/{id}")
    public ResponseEntity<Object> getOneMethod(@PathVariable(value = "id") long idMethod) {
        Optional<MethodModel> method = methodService.getOneMethod(idMethod);
        if (method.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Method not found, the specified method ID does not exist.");
        }
        method.get().add(linkTo(methodOn(MethodController.class).getAllMethods()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(method.get());
    }

    @PostMapping("/methods")
    public ResponseEntity<MethodModel> saveMethod(@RequestBody @Valid MethodRecordDto methodRecordDto) {
        MethodModel savedMethod = methodService.saveMethod(methodRecordDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMethod);
    }

    @PutMapping("/methods/{id}")
    public ResponseEntity<Object> updateMethod(@PathVariable(value = "id") long idMethod,
                                               @RequestBody @Valid MethodRecordDto methodRecordDto) {
        Optional<MethodModel> updatedMethod = methodService.updateMethod(idMethod, methodRecordDto);

        if (updatedMethod.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedMethod.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Method not found, the specified method ID does not exist.");
        }
    }

    @DeleteMapping("/methods/{id}")
    public ResponseEntity<Object> deleteMethod(@PathVariable(value = "id") long idMethod) {
        boolean deleted = methodService.deleteMethod(idMethod);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Method deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Method not found, the specified method ID does not exist.");
    }
}
