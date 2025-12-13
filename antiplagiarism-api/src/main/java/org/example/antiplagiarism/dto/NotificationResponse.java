package org.example.antiplagiarism.dto;

import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.Objects;

@Relation(collectionRelation = "notifications", itemRelation = "notification")
public class NotificationResponse {
    private final String notificationId;
    private final String recipientId;
    private final String message;
    private final String eventType;
    private final LocalDateTime sentAt;

    public NotificationResponse(String notificationId, String recipientId, String message, String eventType, LocalDateTime sentAt) {
        this.notificationId = notificationId;
        this.recipientId = recipientId;
        this.message = message;
        this.eventType = eventType;
        this.sentAt = sentAt;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getMessage() {
        return message;
    }

    public String getEventType() {
        return eventType;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        NotificationResponse that = (NotificationResponse) object;
        return Objects.equals(notificationId, that.notificationId) && Objects.equals(recipientId, that.recipientId) && Objects.equals(message, that.message) && Objects.equals(eventType, that.eventType) && Objects.equals(sentAt, that.sentAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notificationId, recipientId, message, eventType, sentAt);
    }
}

