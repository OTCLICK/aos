package org.example.antiplagiarism.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.antiplagiarism.dto.StatusResponse;
import org.example.antiplagiarism.dto.WorkSubmissionRequest;
import org.example.antiplagiarism.dto.WorkSubmissionResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "submissions", description = "API для приёма студенческих работ")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Ошибка валидации запроса",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class))),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = StatusResponse.class)))
})
public interface SubmissionApi {

    @Operation(summary = "Загрузить новую студенческую работу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Работа успешно принята и поставлена в очередь на проверку",
                    content = @Content(mediaType = "application/hal+json",
                            schema = @Schema(implementation = WorkSubmissionResponse.class))),
            @ApiResponse(responseCode = "409", description = "Работа с таким названием уже была загружена этим студентом",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatusResponse.class)))
    })
    @PostMapping(value = "/api/submissions", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EntityModel<WorkSubmissionResponse>> submitWork(@Valid @RequestBody WorkSubmissionRequest request);

    @Operation(summary = "Получить информацию о работе по её ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Работа найдена",
                    content = @Content(mediaType = "application/hal+json",
                            schema = @Schema(implementation = WorkSubmissionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Работа не найдена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatusResponse.class)))
    })
    @GetMapping("/api/submissions/{workId}")
    EntityModel<WorkSubmissionResponse> getWorkById(@Parameter(description = "Уникальный ID работы") @PathVariable String workId);

    @Operation(summary = "Получить список всех загруженных работ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список работ успешно получен",
                    content = @Content(mediaType = "application/hal+json"))
    })
    @GetMapping("/api/submissions")
    CollectionModel<EntityModel<WorkSubmissionResponse>> getAllSubmissions();

    @Operation(summary = "Удалить работу по её ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Работа удалена",
                    content = @Content(mediaType = "application/hal+json",
                            schema = @Schema(implementation = WorkSubmissionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Работа не найдена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatusResponse.class)))
    })
    @DeleteMapping("api/submissions/{workId}")
    ResponseEntity<Void> deleteWorkById(@Parameter(description = "Уникальный ID работы") @PathVariable String workId);
}
