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

import java.util.Random;

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

        return data;
    }


    @MessageMapping("/chat/{gameCode}/offend")
    @SendTo("/topic/chat/{gameCode}/messages")
    public String offend(@Payload String data, @DestinationVariable("gameCode") String gameCode){

        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String offend = generateRandomOffend();
        jsonObject.addProperty("message", offend);
        logger.info("Beleidigung von " + name + " in Game " + gameCode + ": " + offend);

        return jsonObject.toString();
    }

    @MessageMapping("/chat/{gameCode}/compliment")
    @SendTo("/topic/chat/{gameCode}/messages")
    public String compliment(@Payload String data, @DestinationVariable("gameCode") String gameCode){

        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String compliment = generateRandomCompliment();
        jsonObject.addProperty("message", compliment);
        logger.info("Beleidigung von " + name + " in Game " + gameCode + ": " + compliment);

        return jsonObject.toString();
    }


    private String generateRandomOffend(){

        String[] offends = {"Uuuuuh, das war blöd...",
                "Mein Cousin spielt besser. ...und der ist 3.",
                "Das war ja gar nix...",
                "Und so willst du gewinnen?",
                "Deine Strategie ist... ...speziell.",
                "Effiziente Strategie, um zu verlieren.",
                "Also so gewinnst du garantiert nicht!",
                "Meine Grossmutter gewinnt gegen dich im Schlaf!",
                "Meinst du diesen Zug wirklich ernst?",
                "Hoffentlich gibt's Spiele, die du besser spielst!",
                "Hoffentlich hast du ein anderes Talent!",
                "Ist das wirklich alles, was du kannst?",
                "Ist dein Gehirn schon an?",
                "Weisst du wirklich, was das Ziel des Spiels ist?",
                "Fährst du nebenbei noch Auto?",
                "Du bist ein guter Egobooster für mich!",
                "Soll ich dir das Ziel des Spiels nochmals erklären?"};

        Random random = new Random();
        return offends[random.nextInt(offends.length)];

    }


    private String generateRandomCompliment(){

        String[] compliments = {"Cleverer Zug!",
                "Du spielst beeindruckend!",
                "Gute Strategie!",
                "Du bist ein wirklich harter Gegner!",
                "Wow, der war gut!",
                "Echt stark gespielt!",
                "Saubere Leistung!",
                "Du spielst gut!",
                "Du machst mir das Leben schwer!",
                "Gut gespielt!"};

        Random random = new Random();
        return compliments[random.nextInt(compliments.length)];

    }





}
