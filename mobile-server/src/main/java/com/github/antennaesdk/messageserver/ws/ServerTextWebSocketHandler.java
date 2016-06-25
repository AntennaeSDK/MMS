/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.antennaesdk.messageserver.ws;


import com.github.antennaesdk.common.messages.ServerMessage;
import com.github.antennaesdk.server.messages.ClientMessageWrapper;
import com.github.antennaesdk.server.messages.ServerMessageWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by snambi on 6/22/16.
 */
public class ServerTextWebSocketHandler extends TextWebSocketHandler implements IServerHandler{

    Logger logger = LoggerFactory.getLogger(ServerTextWebSocketHandler.class);

    private Map<String,WebSocketSession> serverSessions = new ConcurrentHashMap<String,WebSocketSession>();

    @Inject
    ClientTextWebSocketHandler clientTextWebSocketHandler;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Server connection established " + session.getId());
        serverSessions.put(session.getId(), session );
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("Server message received: " + message.toString());

        if( message == null ){
            return;
        }

        String wsPayload = message.getPayload();
        ClientMessageWrapper clientMessage = ClientMessageWrapper.fromJson(wsPayload);


        clientTextWebSocketHandler.sendToClient( clientMessage );
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("Server connection closed " + session.getId());
        serverSessions.remove(session.getId());
    }

    @Override
    public void processRequestResponse(String wsSessionId, ServerMessage message) {

        // TODO: get the right session that processes this message
        ServerMessageWrapper trackedMessage = new ServerMessageWrapper();
        trackedMessage.setServerMessage(message);
        trackedMessage.setSessionId(wsSessionId);

        // TODO: set the nodeId ( which uniquely identifies a antennae node )
        // trackedMessage.setNodeId();

        Set<String> sessionKeys = serverSessions.keySet();
        for( String key : sessionKeys ){

            WebSocketSession serverSession =  serverSessions.get(key);

            if( serverSession.isOpen() ){

                TextMessage wsMessage = new TextMessage( trackedMessage.toJson());
                try {
                    serverSession.sendMessage( wsMessage );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void processPubSub(ServerMessage message) {

    }

}
