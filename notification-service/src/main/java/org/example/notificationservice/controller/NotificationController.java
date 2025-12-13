package org.example.notificationservice.controller;

import org.example.notificationservice.handler.NotificationWebSocketHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationWebSocketHandler handler;

    public NotificationController(NotificationWebSocketHandler handler) {
        this.handler = handler;
    }

    @PostMapping("/broadcast")
    public ResponseEntity<Map<String, Object>> broadcast(@RequestBody String message) {
        int sent = handler.broadcast(message);
        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "sentTo", sent,
                "message", message
        ));
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendToUser(@RequestBody Map<String, String> payload) {
        String userId = payload.get("userId");
        String message = payload.get("message");

        if (userId == null || message == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "userId userId and message are required"));
        }

        boolean sent = handler.sendToUser(userId, message);

        return ResponseEntity.ok(Map.of(
                "status", sent ? "sent" : "user_offline",
                "userId", userId,
                "sent", sent,
                "message", message
        ));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        return ResponseEntity.ok(Map.of(
                "activeConnections", handler.getActiveConnections()
        ));
    }
}