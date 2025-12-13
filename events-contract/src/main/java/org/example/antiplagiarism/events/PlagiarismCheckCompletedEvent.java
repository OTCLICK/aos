package org.example.antiplagiarism.events;

import java.io.Serializable;

public record PlagiarismCheckCompletedEvent(
        String workId,
        int similarityScore,
        boolean isFlagged,
        String checkedAt
) implements Serializable {
}
