package org.example.antiplagiarism.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.antiplagiarism.dto.NotificationResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "notifications", description = "Просмотр уведомлений")
@RequestMapping("/api/notifications")
public interface NotificationApi {

    @Operation(summary = "Получить все уведомления для студента")
    @ApiResponse(responseCode = "200", description = "Список уведомлений",
            content = @Content(mediaType = "application/hal+json",
                    array = @ArraySchema(schema = @Schema(implementation = NotificationResponse.class))))
    @GetMapping("/student/{studentId}")
    CollectionModel<EntityModel<NotificationResponse>> getNotificationsForStudent(
            @Parameter(description = "ID студента") @PathVariable String studentId);
}
