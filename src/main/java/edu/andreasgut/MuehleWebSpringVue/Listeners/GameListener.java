package edu.andreasgut.MuehleWebSpringVue.Listeners;

import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Services.GameManagerService;
import edu.andreasgut.MuehleWebSpringVue.Websocket.MessagingTool;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameListener {

    private final MessagingTool messagingTool;
    private final GameManagerService gameManagerService;
    private static final Logger logger = LoggerFactory.getLogger(GameListener.class);

    // Parameterloser Konstruktor, erforderlich für JPA/Hibernate
    public GameListener() {
        this.messagingTool = null; // oder eine Standardinitialisierung
        this.gameManagerService = null; // oder eine Standardinitialisierung
    }

    // Constructor Injection
    @Autowired
    public GameListener(MessagingTool messagingTool, GameManagerService gameManagerService) {
        this.messagingTool = messagingTool;
        this.gameManagerService = gameManagerService;
    }

    @PrePersist
    void onPrePersist(Game game) {
        logger.info("onPrePersist " + game.getGameCode());
    }

    @PostPersist
    void onPostPersist(Game game) {
        logger.info("onPostPersist " + game.getGameCode());
        if (messagingTool != null && gameManagerService != null) {
            messagingTool.sendMessageToTopic("/topic/manager", gameManagerService.getActiveGames());
        }
        else {
            logger.error("messagingTool oder gameManagerService ist null");
            //TODO: Landet immer hier wegen Lifecycle Problemen... @EntityListener macht Probleme
        }
    }

    @PostLoad
    void onPostLoad(Game game) {
        logger.info("onPostLoad " + game.getGameCode());
    }

    @PreUpdate
    void onPreUpdate(Game game) {
        logger.info("onPreUpdate " + game.getGameCode());
    }

    @PostUpdate
    void onPostUpdate(Game game) {
        logger.info("onPostUpdate " + game.getGameCode());
    }

    @PreRemove
    void onPreRemove(Game game) {
        logger.info("onPreRemove " + game.getGameCode());
    }

    @PostRemove
    void onPostRemove(Game game) {
        logger.info("onPostRemove " + game.getGameCode());
    }
}
