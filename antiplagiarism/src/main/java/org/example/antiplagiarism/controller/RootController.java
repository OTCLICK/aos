package org.example.antiplagiarism.controller;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class RootController {

    @GetMapping
    public RepresentationModel<?> getRoot() {
        return new RepresentationModel<>()
                .add(linkTo(methodOn(SubmissionController.class).getAllSubmissions()).withRel("submissions"))
                .add(linkTo(methodOn(GradingController.class).getAllGrades()).withRel("grades"))
                .add(Link.of("/swagger-ui.html", "documentation"));
    }
}
