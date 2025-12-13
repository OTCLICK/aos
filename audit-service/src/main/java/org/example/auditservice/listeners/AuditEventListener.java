package org.example.auditservice.listeners;

import com.rabbitmq.client.Channel;
import org.example.antiplagiarism.events.GradeAssignedEvent;
import org.example.antiplagiarism.events.WorkSubmittedEvent;
import org.example.antiplagiarism.events.WorkDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuditEventListener {

    private static final Logger log = LoggerFactory.getLogger(AuditEventListener.class);

    private final Set<String> processedWorkIds = ConcurrentHashMap.newKeySet();

    private final static String EXCHANGE_NAME = "antiplagiarism-exchange";
    private final static String DLX_NAME = "dlx-exchange";
    private final static String DLQ_ROUTING_KEY = "dlq.audit";

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "audit-work-submitted-queue", durable = "true", arguments = {
                    @Argument(name = "x-dead-letter-exchange", value = DLX_NAME),
                    @Argument(name = "x-dead-letter-routing-key", value = DLQ_ROUTING_KEY)
            }),
            exchange = @Exchange(name = EXCHANGE_NAME, type = "topic", durable = "true"),
            key = "work.submitted"
    ))
    public void handleWorkSubmittedEvent(@Payload WorkSubmittedEvent event, Channel channel,
                                         @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        if (!processedWorkIds.add(event.workId())) {
            log.warn("Duplicate WorkSubmittedEvent for workId: {}", event.workId());
            channel.basicAck(deliveryTag, false);
            return;
        }
        try {
            log.info("AUDIT: Received WorkSubmittedEvent: {}", event);
            // if ("CRASH".equals(event.title())) throw new RuntimeException("Simulated crash");
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Failed to process WorkSubmittedEvent: {}. Sending to DLQ", event, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "audit-grade-assigned-queue", durable = "true", arguments = {
                    @Argument(name = "x-dead-letter-exchange", value = DLX_NAME),
                    @Argument(name = "x-dead-letter-routing-key", value = DLQ_ROUTING_KEY)
            }),
            exchange = @Exchange(name = EXCHANGE_NAME, type = "topic", durable = "true"),
            key = "grade.assigned"
    ))
    public void handleGradeAssignedEvent(@Payload GradeAssignedEvent event, Channel channel,
                                         @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        if (!processedWorkIds.add(event.workId())) {
            log.warn("Duplicate GradeAssignedEvent for workId: {}", event.workId());
            channel.basicAck(deliveryTag, false);
            return;
        }
        try {
            log.info("AUDIT: Received GradeAssignedEvent: {}", event);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Failed to process GradeAssignedEvent: {}. Sending to DLQ", event, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "audit-work-deleted-queue", durable = "true", arguments = {
                    @Argument(name = "x-dead-letter-exchange", value = DLX_NAME),
                    @Argument(name = "x-dead-letter-routing-key", value = DLQ_ROUTING_KEY)
            }),
            exchange = @Exchange(name = EXCHANGE_NAME, type = "topic", durable = "true"),
            key = "work.deleted"
    ))
    public void handleWorkDeletedEvent(@Payload WorkDeletedEvent event, Channel channel,
                                       @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        if (!processedWorkIds.add(event.workId())) {
            log.warn("Duplicate WorkDeletedEvent for workId: {}", event.workId());
            channel.basicAck(deliveryTag, false);
            return;
        }
        try {
            log.info("AUDIT: Received WorkDeletedEvent: {}", event);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Failed to process WorkDeletedEvent: {}. Sending to DLQ", event, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "notification-queue.dlq", durable = "true"),
                    exchange = @Exchange(name = "dlx-exchange", type = "topic", durable = "true"),
                    key = "dlq.notifications"
            )
    )
    public void handleDlqMessages(Object failedMessage) {
        log.error("!!!!! Received DLQ messages: {}", failedMessage);
    }
}
