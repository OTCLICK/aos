package org.example.antiplagiarism.controller;

import jakarta.validation.Valid;
import org.example.antiplagiarism.assemblers.GradeModelAssembler;
import org.example.antiplagiarism.dto.GradeRequest;
import org.example.antiplagiarism.dto.GradeResponse;
import org.example.antiplagiarism.interfaces.GradingApi;
import org.example.antiplagiarism.service.GradingService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class GradingController implements GradingApi {

    private final GradingService gradingService;
    private final GradeModelAssembler gradeModelAssembler;

    public GradingController(
            GradingService gradingService,
            GradeModelAssembler gradeModelAssembler
    ) {
        this.gradingService = gradingService;
        this.gradeModelAssembler = gradeModelAssembler;
    }

    @Override
    public EntityModel<GradeResponse> gradeWork(String workId, @Valid GradeRequest request) {
        GradeResponse response = gradingService.gradeWork(workId, request);
        return gradeModelAssembler.toModel(response);
    }

    @Override
    public EntityModel<GradeResponse> getGradeByWorkId(String workId) {
        GradeResponse response = gradingService.getGradeByWorkId(workId);
        return gradeModelAssembler.toModel(response);
    }

    @Override
    public CollectionModel<EntityModel<GradeResponse>> getAllGrades() {
        List<GradeResponse> allGrades = gradingService.getAllGrades();
        List<EntityModel<GradeResponse>> gradeModels = allGrades.stream()
                .map(gradeModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(gradeModels)
                .add(linkTo(methodOn(GradingController.class).getAllGrades()).withSelfRel());
    }
}
