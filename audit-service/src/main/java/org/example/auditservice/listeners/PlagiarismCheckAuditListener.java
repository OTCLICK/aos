 package org.example.auditservice.listeners;

import org.example.antiplagiarism.events.PlagiarismCheckCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PlagiarismCheckAuditListener {

    private static final Logger log = LoggerFactory.getLogger(PlagiarismCheckAuditListener.class);

    @RabbitListener(
            containerFactory = "rabbitListenerContainerFactory",
            bindings = @QueueBinding(
                    value = @Queue(
                            name = "q.audit.plagiarism-check",
                            durable = "true",
                            arguments = @org.springframework.amqp.rabbit.annotation.Argument(
                                    name = "x-dead-letter-exchange",
                                    value = "dlx-exchange"
                            )
                    ),
                    exchange = @Exchange(name = "plagiarism-fanout", type = "fanout")
            )
    )
    public void handlePlagiarismCheckCompleted(PlagiarismCheckCompletedEvent event) {
        log.info("AUDIT SERVICE: Plagiarism check completed for workId={}, score={}, flagged={}, checkedAt={}",
                event.workId(),
                event.similarityScore(),
                event.isFlagged(),
                event.checkedAt());
    }
}