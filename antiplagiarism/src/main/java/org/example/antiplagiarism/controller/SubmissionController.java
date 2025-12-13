package org.example.antiplagiarism.controller;

import jakarta.validation.Valid;
import org.example.antiplagiarism.assemblers.WorkSubmissionModelAssembler;
import org.example.antiplagiarism.dto.WorkSubmissionRequest;
import org.example.antiplagiarism.dto.WorkSubmissionResponse;
import org.example.antiplagiarism.interfaces.SubmissionApi;
import org.example.antiplagiarism.service.SubmissionService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class SubmissionController implements SubmissionApi {

    private final SubmissionService submissionService;
    private final WorkSubmissionModelAssembler workSubmissionModelAssembler;

    public SubmissionController(
            SubmissionService submissionService,
            WorkSubmissionModelAssembler workSubmissionModelAssembler
    ) {
        this.submissionService = submissionService;
        this.workSubmissionModelAssembler = workSubmissionModelAssembler;
    }

    @Override
    public ResponseEntity<EntityModel<WorkSubmissionResponse>> submitWork(@Valid WorkSubmissionRequest request) {
        WorkSubmissionResponse response = submissionService.submitWork(request);
        EntityModel<WorkSubmissionResponse> model = workSubmissionModelAssembler.toModel(response);
        return ResponseEntity.created(linkTo(methodOn(SubmissionController.class).getWorkById(response.getWorkId())).toUri())
                .body(model);
    }

    @Override
    public EntityModel<WorkSubmissionResponse> getWorkById(String workId) {
        WorkSubmissionResponse response = submissionService.getWorkById(workId);
        return workSubmissionModelAssembler.toModel(response);
    }

    @Override
    public CollectionModel<EntityModel<WorkSubmissionResponse>> getAllSubmissions() {
        List<WorkSubmissionResponse> allSubmissions = submissionService.getAllWorks();
        List<EntityModel<WorkSubmissionResponse>> submissionModels = allSubmissions.stream()
                .map(workSubmissionModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(submissionModels)
                .add(linkTo(methodOn(SubmissionController.class).getAllSubmissions()).withSelfRel());
    }

    @Override
    public ResponseEntity<Void> deleteWorkById(String workId) {
        submissionService.deleteWork(workId);
        return ResponseEntity.noContent().build();
    }
}
