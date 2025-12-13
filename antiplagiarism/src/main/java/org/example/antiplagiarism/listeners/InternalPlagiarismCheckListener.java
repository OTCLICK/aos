package org.example.antiplagiarism.listeners;

import org.example.antiplagiarism.events.PlagiarismCheckCompletedEvent;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class InternalPlagiarismCheckListener {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "q.antiplagiarism.plagiarism.log", durable = "true"),
                    exchange = @Exchange(name = "plagiarism-fanout", type = "fanout")
            )
    )
    public void logPlagiarismResult(PlagiarismCheckCompletedEvent event) {
        System.out.println("ANTIPLAGIARISM SERVICE (internal): Plagiarism check completed → " +
                "workId=" + event.workId() +
                ", score=" + event.similarityScore() +
                ", flagged=" + event.isFlagged() +
                ", checkedAt=" + event.checkedAt());
    }
}