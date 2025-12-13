package org.example.antiplagiarism.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record GradeRequest(
        @Min(0)
        @Max(100)
        int grade,

        @NotBlank
        String reviewerId
) {
}
