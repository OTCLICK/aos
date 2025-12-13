package org.example.antiplagiarism.assemblers;

import org.example.antiplagiarism.controller.GradingController;
import org.example.antiplagiarism.controller.SubmissionController;
import org.example.antiplagiarism.dto.GradeResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GradeModelAssembler implements RepresentationModelAssembler<GradeResponse, EntityModel<GradeResponse>> {

    @Override
    public EntityModel<GradeResponse> toModel(GradeResponse grade) {
        return EntityModel.of(grade,
                linkTo(methodOn(GradingController.class).getGradeByWorkId(grade.getWorkId())).withSelfRel(),
                linkTo(methodOn(SubmissionController.class).getWorkById(grade.getWorkId())).withRel("work")
        );
    }
}
