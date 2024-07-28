package edu.andreasgut.MuehleWebSpringVue.Websocket;

import edu.andreasgut.MuehleWebSpringVue.Websocket.Admin.AdminWebsocketWebsocketHandler;
import edu.andreasgut.MuehleWebSpringVue.Websocket.Game.GameWebsocketWebsocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    @Autowired
    GameWebsocketWebsocketHandler gameWebsocketWebsocketHandler;

    @Autowired
    AdminWebsocketWebsocketHandler adminDatabaseHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(gameWebsocketWebsocketHandler, "/websocket/game").setAllowedOrigins("*");
        webSocketHandlerRegistry.addHandler(adminDatabaseHandler, "/websocket/admin").setAllowedOrigins("*");
    }

}
