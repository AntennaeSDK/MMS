package com.github.antennaesdk.messageserver.ws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler( clientHandler(), "/client");
		registry.addHandler( serverHandler(), "/server");
	}

	@Bean(name = "clientHandler")
    public ClientTextWebSocketHandler clientHandler() {
        return new ClientTextWebSocketHandler();
    }

	@Bean(name = "serverHandler")
	public ServerTextWebSocketHandler serverHandler(){
		return new ServerTextWebSocketHandler();
	}

	@Bean
	public DefaultHandshakeHandler handshakeHandler() {

/*
		WebSocketPolicy policy = new WebSocketPolicy(WebSocketBehavior.SERVER);
		policy.setInputBufferSize(8192);
		policy.setIdleTimeout(600000);

		return new DefaultHandshakeHandler(
				new TomcatRequestUpgradeStrategy(new WebSocketServerFactory(policy)));
*/

		return  new DefaultHandshakeHandler();
	}

	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer() {
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxTextMessageBufferSize(8192);
		container.setMaxBinaryMessageBufferSize(8192);
		container.setMaxSessionIdleTimeout(300000);
		return container;
	}
}
