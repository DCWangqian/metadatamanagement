package eu.dzhw.fdz.metadatamanagement.common.config;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * Configuration for websockets.
 * 
 * @author René Reitmann
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

  private final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);

  public static final String IP_ADDRESS = "IP_ADDRESS";

  @Autowired
  private MetadataManagementProperties properties;

  @Autowired
  private Environment env;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    if (env.acceptsProfiles(Constants.SPRING_PROFILE_LOCAL, Constants.SPRING_PROFILE_UNITTEST)) {
      config.enableSimpleBroker("/topic", "/queue");
    } else {
      config.enableStompBrokerRelay("/topic", "/queue")
          .setRelayHost(properties.getRabbitmq().getHost())
          .setVirtualHost(properties.getRabbitmq().getVirtualHost())
          .setSystemPasscode(properties.getRabbitmq().getPassword())
          .setSystemLogin(properties.getRabbitmq().getUsername())
          .setClientLogin(properties.getRabbitmq().getUsername())
          .setClientPasscode(properties.getRabbitmq().getPassword());
    }
    config.setApplicationDestinationPrefixes("/metadatamanagement");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/websocket").setAllowedOrigins("*").withSockJS()
        .setSessionCookieNeeded(false)
        .setClientLibraryUrl("/bower_components/sockjs-client/dist/sockjs.min.js")
        .setInterceptors(websocketHandshakeInterceptor());
  }

  @EventListener
  public void onSessionConnectEvent(SessionConnectEvent event) {
    StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
    log.debug("New websocket connection {}", sha.getSessionAttributes().get(IP_ADDRESS));
  }

  @EventListener
  public void onSessionDisconnectEvent(SessionDisconnectEvent event) {
    StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
    log.debug("Closed websocket connection {}", sha.getSessionAttributes().get(IP_ADDRESS));
  }

  @Bean
  public HandshakeInterceptor websocketHandshakeInterceptor() {
    return new WebsocketHandshakeInterceptor();
  }
  
  /**
   * Add remote ip address to websocket session.
   */
  private static class WebsocketHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
        WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
      if (request instanceof ServletServerHttpRequest) {
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        attributes.put(IP_ADDRESS, servletRequest.getRemoteAddress());
      }
      return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
        WebSocketHandler wsHandler, Exception exception) {
    }
  }
}
