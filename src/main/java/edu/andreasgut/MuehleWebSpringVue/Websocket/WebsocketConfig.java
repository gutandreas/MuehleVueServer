package edu.andreasgut.MuehleWebSpringVue.Websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Konfiguriere den Nachrichtendienst
        registry.enableSimpleBroker("/queue", "/topic"); // Definiere die Broker-Ziele
        //registry.setApplicationDestinationPrefixes("/app"); // Präfix für Anwendungsnachrichten
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registriere STOMP-Endpunkte
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:8080").withSockJS();
    }
}
