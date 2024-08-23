package edu.andreasgut.MuehleWebSpringVue.Websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.andreasgut.MuehleWebSpringVue.Services.SenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    SenderService senderService;

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    public ChatController(SenderService senderService) {
        this.senderService = senderService;
    }


    @MessageMapping("/chat/{gameCode}/messages")
    @SendTo("/topic/chat/{gameCode}/messages")
    public String handleMessage(@Payload String data, @DestinationVariable("gameCode") String gameCode) {
        // Hier kannst du die Nachricht entsprechend verarbeiten
        System.out.println(data);
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String message = jsonObject.get("message").getAsString();
        logger.info("Nachricht von " + name + " in Game " + gameCode + ": " + message);


        // Die Nachricht wird an alle Abonnenten dieses Channels gesendet
        return data;
    }




}
