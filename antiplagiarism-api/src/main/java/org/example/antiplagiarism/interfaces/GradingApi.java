package org.example.antiplagiarism.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.antiplagiarism.dto.GradeRequest;
import org.example.antiplagiarism.dto.GradeResponse;
import org.example.antiplagiarism.dto.StatusResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "grading", description = "Выставление и получение оценок")
public interface GradingApi {

    @Operation(summary = "Выставить оценку за работу")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Оценка выставлена",
                    content = @Content(mediaType = "application/hal+json",
                            schema = @Schema(implementation = GradeResponse.class))),
            @ApiResponse(responseCode = "404", description = "Работа не найдена",
                    content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    })
    @PutMapping(value = "/api/grading/{workId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    EntityModel<GradeResponse> gradeWork(
            @Parameter(description = "ID работы") @PathVariable String workId,
            @Valid @RequestBody GradeRequest request
    );

    @Operation(summary = "Получить оценку по ID работы")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Оценка найдена",
                    content = @Content(mediaType = "application/hal+json",
                            schema = @Schema(implementation = GradeResponse.class))),
            @ApiResponse(responseCode = "404", description = "Оценка не найдена",
                    content = @Content(schema = @Schema(implementation = StatusResponse.class)))
    })
    @GetMapping("/api/grading/{workId}")
    EntityModel<GradeResponse> getGradeByWorkId(
            @Parameter(description = "ID работы") @PathVariable String workId
    );

    @Operation(summary = "Получить список всех выставленных оценок")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список оценок успешно получен",
                    content = @Content(mediaType = "application/hal+json"))
    })
    @GetMapping("/api/grading")
    CollectionModel<EntityModel<GradeResponse>> getAllGrades();
}
