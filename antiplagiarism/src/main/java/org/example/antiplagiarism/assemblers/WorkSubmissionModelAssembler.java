package org.example.antiplagiarism.assemblers;

import org.example.antiplagiarism.controller.SubmissionController;
import org.example.antiplagiarism.dto.WorkSubmissionResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class WorkSubmissionModelAssembler implements RepresentationModelAssembler<WorkSubmissionResponse, EntityModel<WorkSubmissionResponse>> {
    @Override
    public EntityModel<WorkSubmissionResponse> toModel(WorkSubmissionResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(SubmissionController.class).getWorkById(response.getWorkId())).withSelfRel(),
                linkTo(methodOn(SubmissionController.class).submitWork(null)).withRel("submit")
        );
    }
}
