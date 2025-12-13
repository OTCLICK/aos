package org.example.notificationservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificationWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(NotificationWebSocketHandler.class);

    private final Map<String, WebSocketSession> sessionsByUserId = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = extractUserId(session);
        if (userId != null && !userId.isEmpty()) {
            sessionsByUserId.put(userId, session);
            log.info("User connected: {} (session: {}, total active: {})",
                    userId, session.getId(), sessionsByUserId.size());
        } else {
            log.warn("Connecting without a userId - closing the session: {}", session.getId());
            try {
                session.close(CloseStatus.POLICY_VIOLATION.withReason("userId required"));
            } catch (IOException e) {
                log.error("Error closing session without userId", e);
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload().trim();
        log.debug("Message from the session {}: {}", session.getId(), payload);

        if ("PING".equals(payload)) {
            session.sendMessage(new TextMessage("PONG"));
            log.info("Sent PONG in response to PING from session {}", session.getId());
            return;
        }

        log.info("Unknown message from client: {}", payload);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = extractUserId(session);
        if (userId != null) {
            sessionsByUserId.remove(userId);
            log.info("User disconnected: {} (reason: {}, active users remaining: {})",
                    userId, status.getReason(), sessionsByUserId.size());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        String userId = extractUserId(session);
        log.error("Connection error for user {} (session {}): {}",
                userId != null ? userId : "unknown", session.getId(), exception.getMessage());
        if (userId != null) {
            sessionsByUserId.remove(userId);
        }
    }

    public int broadcast(String message) {
        TextMessage textMessage = new TextMessage(message);
        int sent = 0;
        for (WebSocketSession session : sessionsByUserId.values()) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(textMessage);
                    sent++;
                } catch (IOException e) {
                    log.warn("Error sending broadcast to session {}", session.getId());
                    removeSession(session);
                }
            }
        }
        log.info("Broadcast sent to {} clients", sent);
        return sent;
    }

    public boolean sendToUser(String userId, String message) {
        WebSocketSession session = sessionsByUserId.get(userId);
        if (session == null || !session.isOpen()) {
            log.warn("User {} is offline or session has been closed", userId);
            return false;
        }

        try {
            session.sendMessage(new TextMessage(message));
            log.info("Message has been sent to the user {}: {}", userId, message);
            return true;
        } catch (IOException e) {
            log.warn("Error sending to user {}: {}", userId, e.getMessage());
            removeSession(session);
            return false;
        }
    }

    private void removeSession(WebSocketSession session) {
        String userId = extractUserId(session);
        if (userId != null) {
            sessionsByUserId.remove(userId);
        }
    }

    private String extractUserId(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query == null) return null;

        for (String param : query.split("&")) {
            if (param.startsWith("userId=")) {
                return param.substring(7);
            }
        }
        return null;
    }

    public int getActiveConnections() {
        return sessionsByUserId.size();
    }

    public Map<String, WebSocketSession> getAllSessions() {
        return Map.copyOf(sessionsByUserId);
    }
}