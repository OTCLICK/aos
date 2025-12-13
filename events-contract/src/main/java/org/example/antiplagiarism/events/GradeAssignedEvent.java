package org.example.antiplagiarism.events;

import java.io.Serializable;
import java.time.LocalDateTime;

public record GradeAssignedEvent(
        String workId,
        int grade,
        String reviewerId,
        LocalDateTime gradedAt
) implements Serializable {}
