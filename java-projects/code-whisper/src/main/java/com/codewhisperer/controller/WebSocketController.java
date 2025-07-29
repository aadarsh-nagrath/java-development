package com.codewhisperer.controller;

import com.codewhisperer.service.CodeWhispererService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class WebSocketController {

    @Autowired
    private CodeWhispererService codeWhispererService;

    @MessageMapping("/subscribe")
    @SendToUser("/topic/subscription")
    public SubscriptionResponse subscribe(String sessionId) {
        log.info("Client subscribed to session: {}", sessionId);
        return new SubscriptionResponse("Subscribed to session: " + sessionId, "SUCCESS");
    }

    @MessageMapping("/ping")
    @SendTo("/topic/pong")
    public PongResponse ping() {
        return new PongResponse(System.currentTimeMillis());
    }

    public static class SubscriptionResponse {
        private String message;
        private String status;

        public SubscriptionResponse(String message, String status) {
            this.message = message;
            this.status = status;
        }

        public String getMessage() { return message; }
        public String getStatus() { return status; }
    }

    public static class PongResponse {
        private long timestamp;

        public PongResponse(long timestamp) {
            this.timestamp = timestamp;
        }

        public long getTimestamp() { return timestamp; }
    }
} 