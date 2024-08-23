package edu.andreasgut.MuehleWebSpringVue.Websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.andreasgut.MuehleWebSpringVue.Services.SenderService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    SenderService senderService;

    public ChatController(SenderService senderService) {
        this.senderService = senderService;
    }

    @MessageMapping("/chat/{gameCode}/messages")
    @SendTo("/topic/chat/{gameCode}/messages")
    public String handleMessage(@Payload String message, @DestinationVariable("gameCode") String gameCode) {
        // Hier kannst du die Nachricht entsprechend verarbeiten
        System.out.println(message);
        //JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();



        // Die Nachricht wird an alle Abonnenten dieses Channels gesendet
        return message;
    }




}
