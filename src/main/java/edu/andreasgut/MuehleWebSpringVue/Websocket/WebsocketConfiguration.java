package edu.andreasgut.MuehleWebSpringVue.Websocket;

import edu.andreasgut.MuehleWebSpringVue.Websocket.Game.AdminWebsocketHandler;
import edu.andreasgut.MuehleWebSpringVue.Websocket.Game.GameWebsocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebsocketConfiguration implements WebSocketConfigurer {

    @Autowired
    GameWebsocketHandler gameWebsocketHandler;

    @Autowired
    AdminWebsocketHandler adminDatabaseHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(gameWebsocketHandler, "/websocket/game").setAllowedOrigins("*");
        webSocketHandlerRegistry.addHandler(adminDatabaseHandler, "/websocket/admin").setAllowedOrigins("*");
    }

}
