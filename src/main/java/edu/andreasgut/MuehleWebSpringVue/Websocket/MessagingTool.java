package edu.andreasgut.MuehleWebSpringVue.Websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessagingTool {

    private static SimpMessagingTemplate messageTemplate;

    @Autowired
    public MessagingTool(SimpMessagingTemplate messageTemplate) {
        MessagingTool.messageTemplate = messageTemplate;
    }

    public void sendMessageToTopic(String destination, Object message) {
        messageTemplate.convertAndSend(destination, message);
    }

    public void sendMessageToQueue(String destination, Object message) {
        messageTemplate.convertAndSend(destination, message);
    }
}
