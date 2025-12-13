package org.example.notificationservice.rabbitmq;

import org.example.antiplagiarism.events.PlagiarismCheckCompletedEvent;
import org.example.notificationservice.handler.NotificationWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Component
public class PlagiarismEventListener {

    private static final Logger log = LoggerFactory.getLogger(PlagiarismEventListener.class);
    private final NotificationWebSocketHandler webSocketHandler;

    public PlagiarismEventListener(NotificationWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @RabbitListener(
            containerFactory = "rabbitListenerContainerFactory",
            bindings = @QueueBinding(
                    value = @Queue(name = "q.notification.plagiarism", durable = "true"),
                    exchange = @Exchange(name = "plagiarism-fanout", type = "fanout")
            )
    )
    public void handlePlagiarismEvent(PlagiarismCheckCompletedEvent event) {
        log.info("Получено событие из RabbitMQ: workId={}, score={}, flagged={}",
                event.workId(), event.similarityScore(), event.isFlagged());

        String jsonMessage = String.format(
                "{\"type\":\"PLAGIARISM_RESULT\",\"workId\":\"%s\",\"score\":%d,\"flagged\":%s,\"checkedAt\":\"%s\"}",
                event.workId(),
                event.similarityScore(),
                event.isFlagged(),
                event.checkedAt()
        );

        webSocketHandler.broadcast(jsonMessage);
    }
}
