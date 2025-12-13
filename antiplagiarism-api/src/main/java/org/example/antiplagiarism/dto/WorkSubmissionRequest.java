package org.example.antiplagiarism.dto;

import jakarta.validation.constraints.NotBlank;

public record WorkSubmissionRequest(
        @NotBlank(message = "Название работы обязательно")
        String title,

        @NotBlank(message = "ID студента обязательно")
        String studentId,

        @NotBlank(message = "Содержимое работы обязательно")
        String fileContent
) {
}
