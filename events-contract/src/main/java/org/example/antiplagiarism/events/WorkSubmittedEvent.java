package org.example.antiplagiarism.events;

import java.io.Serializable;
import java.time.LocalDateTime;

public record WorkSubmittedEvent(
        String workId,
        String title,
        String studentId,
        LocalDateTime submittedAt
) implements Serializable {}
