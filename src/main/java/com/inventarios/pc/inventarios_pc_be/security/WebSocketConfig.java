package com.inventarios.pc.inventarios_pc_be.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(
            "/topic",  // Para mensajes de difusión
            "/queue"   // Para mensajes específicos de usuario
        );
        config.setApplicationDestinationPrefixes("/app");
        config.setPreservePublishOrder(true); // Mantiene el orden de los mensajes
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")

            .setAllowedOrigins(
                "http://192.168.9.152",
                "http://192.168.9.152:80",
                "http://localhost:4200",
                "http://192.168.1.5:83"
            )
            .setAllowedOriginPatterns("*")
            .withSockJS()
            .setWebSocketEnabled(true)
            .setSessionCookieNeeded(true)
            .setHeartbeatTime(25000)
            .setDisconnectDelay(5000);
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(128 * 1024)     // Límite de mensaje: 128KB
            .setSendBufferSizeLimit(512 * 1024)          // Buffer de envío: 512KB
            .setSendTimeLimit(20000)                     // Timeout de envío: 20 segundos
            .setTimeToFirstMessage(30000);               // Tiempo máximo para primer mensaje: 30 segundos
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = 
                    MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    // Aquí puedes agregar lógica de autenticación si es necesario
                    // Por ejemplo, validar tokens JWT
                    // accessor.setUser(new Principal(...));
                }
                
                // Agregar headers CORS
                if (accessor != null) {
                    accessor.addNativeHeader("Access-Control-Allow-Origin", 
                        "http://192.168.9.152");
                    accessor.addNativeHeader("Access-Control-Allow-Credentials", 
                        "true");
                }
                
                return message;
            }
        });
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = 
                    MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                
                if (accessor != null) {
                    accessor.addNativeHeader("Access-Control-Allow-Origin", 
                        "http://192.168.9.152");
                    accessor.addNativeHeader("Access-Control-Allow-Credentials", 
                        "true");
                }
                
                return message;
            }
        });
        
        // Configurar tamaño del pool de hilos para mensajes salientes
        registration.taskExecutor()
            .corePoolSize(4)
            .maxPoolSize(10)
            .queueCapacity(25);
    }

}
