package edu.andreasgut.MuehleWebSpringVue.Websocket;

import edu.andreasgut.MuehleWebSpringVue.Websocket.Admin.AdminMainWebsocketHandler;
import edu.andreasgut.MuehleWebSpringVue.Websocket.Game.GameMainWebsocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    @Autowired
    GameMainWebsocketHandler gameMainWebsocketHandler;

    @Autowired
    AdminMainWebsocketHandler adminDatabaseHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(gameMainWebsocketHandler, "/websocket/game").setAllowedOrigins("*");
        webSocketHandlerRegistry.addHandler(adminDatabaseHandler, "/websocket/admin").setAllowedOrigins("*");
    }

}
