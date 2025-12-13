package org.example.antiplagiarism.events;

import java.io.Serializable;
import java.time.LocalDateTime;

public record WorkDeletedEvent(
        String workId,
        String title,
        String studentId,
        LocalDateTime deletedAt
) implements Serializable {
}
