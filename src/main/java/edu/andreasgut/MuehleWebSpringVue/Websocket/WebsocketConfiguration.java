package edu.andreasgut.MuehleWebSpringVue.Websocket;

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
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(gameWebsocketHandler, "/websocket/game").setAllowedOrigins("*");
        webSocketHandlerRegistry.addHandler(gameWebsocketHandler, "/websocket/admin").setAllowedOrigins("*");
    }

}
