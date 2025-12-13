package org.example.statisticsservice.listeners;

import com.rabbitmq.client.Channel;
import org.example.antiplagiarism.events.WorkSubmittedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StatisticsListener {

    private final Map<String, String> submittedWorks = new ConcurrentHashMap<>();
    private final Set<String> processedWorkIds = ConcurrentHashMap.newKeySet();
    private static final Logger log = LoggerFactory.getLogger(StatisticsListener.class);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    name = "statistics-queue",
                    durable = "true",
                    arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = "dlx-exchange"),
                            @Argument(name = "x-dead-letter-routing-key", value = "dlq.statistics")
                    }
            ),
            exchange = @Exchange(name = "antiplagiarism-exchange", type = "topic", durable = "true"),
            key = "work.submitted"
    ))
    public void onWorkSubmitted(
            @Payload WorkSubmittedEvent event,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {

        if (!processedWorkIds.add(event.workId())) {
            log.warn("Duplicate WorkSubmittedEvent for workId: {}", event.workId());
            channel.basicAck(deliveryTag, false);
            return;
        }

        try {
            submittedWorks.put(event.workId(), event.title());
            log.info("STATISTICS: New work submitted. Total count: {}", submittedWorks.size());
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Failed to process WorkSubmittedEvent: {}. Sending to DLQ.", event, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "statistics.dlq", durable = "true"),
            exchange = @Exchange(name = "dlx-exchange", type = "topic", durable = "true"),
            key = "dlq.statistics"
    ))
    public void handleDlq(@Payload Object message) {
        log.error("!!! DLQ: Failed message in statistics-service: {}", message);
    }
}
