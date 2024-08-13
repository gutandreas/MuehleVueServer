package edu.andreasgut.MuehleWebSpringVue.Listeners;

import edu.andreasgut.MuehleWebSpringVue.Models.Game;
import edu.andreasgut.MuehleWebSpringVue.Websocket.GameManagerController;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameListener {

    private static final Logger logger = LoggerFactory.getLogger(GameListener.class);

    @PrePersist
    void onPrePersist(Game game) {
        logger.info("onPrePersist " + game.getGameCode());
    }
    @PostPersist
    void onPostPersist(Game game) {
        logger.info("onPostPersist " + game.getGameCode());

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
