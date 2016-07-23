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


import com.github.antennaesdk.common.messages.*;
import com.github.antennaesdk.common.messages.util.JsonUtil;
import com.github.antennaesdk.messageserver.rest.RestClient;
import com.github.antennaesdk.server.messages.ClientMessageWrapper;
import okhttp3.*;
import okio.ByteString;
import org.apache.catalina.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * <code>ClientTextWebSocketHandler</code> receives the incoming web-socket messages.
 *
 * reads the json, identifies the <code>topic</code> and then sends it to the <code>TopicProcessor</code>
 */
public class ClientTextWebSocketHandler extends TextWebSocketHandler implements IClientHandler {

    private static Logger logger = LoggerFactory.getLogger(ClientTextWebSocketHandler.class);
    private Map<String,WebSocketSession> clientSessions = new ConcurrentHashMap<String,WebSocketSession>();
    private Map<String, ClientAddress> clientAddresses = new ConcurrentHashMap<String,ClientAddress>();
    private ExecutorService executorService;

    @Inject
    ServerTextWebSocketHandler serverHandler;

    @PostConstruct
    public void init(){
        executorService = Executors.newCachedThreadPool();
    }

    @PreDestroy
    public void shutdown(){
        executorService.shutdown();
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Client connection established : " + session.getId());
        clientSessions.put( session.getId(), session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("connection error on session " + session.getId() + ". \n" + exception);

        // cleanup the cached sessions
        if( clientSessions.get(session.getId()) != null ){
            clientSessions.remove(session.getId());
        }
        if( clientAddresses.get(session.getId()) != null ){
            clientAddresses.remove(session.getId());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("Client connection closed: " + session.getId() + ", status :" + status.toString());

        // cleanup the cached sessions
        if( clientSessions.get(session.getId()) != null ){
            clientSessions.remove(session.getId());
        }
        if( clientAddresses.get(session.getId()) != null ){
            clientAddresses.remove(session.getId());
        }
    }

    /**
     * All messages from real-clients are received by this method.
     *
     * @param session
     * @param incomingMessage
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage incomingMessage) {

        if (incomingMessage == null) {
            return;
        }

        // TODO: implement rate limiting

        String textmessage = incomingMessage.getPayload();

        // route the message based on the payload
        routeMessage( session, textmessage);
    }

    /**
     * <code>routeMessage</code> routes the message based on the payload to appropriate Processors.
     *
     * @param session
     * @param textMessage
     */
    public void routeMessage( WebSocketSession session, String textMessage ){

        String clazzName = JsonUtil.identifyClassType(textMessage);

        try {

            if( clazzName == null ){

                logger.debug("ClassName is unknown. Unable to parse the incoming message.");
                return;

            }else if (clazzName.equals(ServerMessage.class.getName())) {

                ServerMessage serverMessage = ServerMessage.fromJson(textMessage);
                processServerMessage( session, serverMessage);

            } else if (clazzName.equals(ServerRestMessage.class.getName())) {

                ServerRestMessage serverRestMessage = ServerRestMessage.fromJson(textMessage);
                processRestServerMessage(session, serverRestMessage );
            }

        }catch( Throwable throwable ){
            logger.error("Unable to parse the incoming incomingMessage. ignoring...");
            return;
        }
    }

    private void processServerMessage( WebSocketSession session, ServerMessage serverMessage){
        if( serverMessage == null ){
            return;
        }

        // Cache the client address
        if( serverMessage.getFrom() != null ){
            cacheTheClientAddress( session, serverMessage.getFrom());
        }

        // TODO: Store the message in a DB before proecessing

        // check whether the incomingMessage is meant for the server or a user/app
        // if topic is set, then it is meant for server side processing
        // if "ServerAddress" object is set, then it should be sent to the appropriate user/app.

        switch ( serverMessage.getMessageType()){
            case PUB_SUB:
                // the message is sent to the correct processor (consumer) of that message.
                // no response expected
                processPubSub( serverMessage);
                break;
            case REQUEST_RESPONSE:
                // Request-Response is similar to PUB_SUB.
                // but the "consumer" sends the a response, which will be sent back the original "producer"
                processRequestResponse( session.getId(), serverMessage );
                break;
        }
    }
    
    private void processRestServerMessage( WebSocketSession session, ServerRestMessage serverRestMessage ){

        // Cache the client address
        if( serverRestMessage.getFrom() != null ){
            cacheTheClientAddress( session, serverRestMessage.getFrom());
        }

        // execute the task in a background thread
        executorService.submit( new BackgroundExecutor(session, serverRestMessage));
    }

    // This message should be processed by server side
    private void processPubSub(ServerMessage message) {
        // no response expected from client
        serverHandler.processPubSub(message);
    }

    private void processRequestResponse(String sessionId , ServerMessage message){

        // response expected from client
        serverHandler.processRequestResponse( sessionId, message);

    }

    private void processPointToPoint( ServerMessage message ){

    }



    /**
     * <code>sendToClient</code> sends the message to clients ( real-world clients ).
     * It first tries to send through an existing session (connection).
     * If connection is not found, then sends the message through GCM/APNS.
     *
     * @param clientMessageWrapper Message targetted for the client
     */
    public void sendToClient(ClientMessageWrapper clientMessageWrapper) {

        if( clientMessageWrapper == null || clientMessageWrapper.getClientMessage() == null ){
            logger.debug("incoming client message is null. returning...");
            return;
        }

        ClientMessage clientMessage = clientMessageWrapper.getClientMessage();

        // find a session
        WebSocketSession session = null;
        String sessionId = clientMessageWrapper.getSessionId();
        if( sessionId != null && clientSessions.get(sessionId) != null ){
            session = clientSessions.get(sessionId);
        }else{
            session = getSesssionForClient(clientMessage.getTo());
        }

        // send the message
        sendToClient( session, clientMessage );
    }

    public void sendToClient( WebSocketSession session, ClientMessage clientMessage ){

        // send the message
        if( session != null && session.isOpen() ){

            TextMessage textMessage = new TextMessage( clientMessage.getPayLoad());
            try {
                session.sendMessage( textMessage );
            } catch (IOException e) {
                e.printStackTrace();
                // TODO: send thru GCM or when the app wakes up
            }

        }else{
            // TODO: send the message using GCM, or when the app wakes up
        }
    }



    /**
     * Cache the sessionIds and ClientAddresses
     *
     * @param session
     * @param clientAddress
     */
    private void cacheTheClientAddress( WebSocketSession session, ClientAddress clientAddress){
        ClientAddress stored = clientAddresses.get( session.getId());

        if( stored == null ){
            clientAddresses.put(session.getId(), clientAddress);
        }else if( !clientAddress.equals(stored)) {
            // TODO: handle error situations
        }
    }

    /**
     * Get the SessionId based on the clientAddress
     *
     * @param clientAddress
     * @return
     */
    private WebSocketSession getSesssionForClient(ClientAddress clientAddress ){

        WebSocketSession result=null;

        Set<String> sessionIds = clientAddresses.keySet();
        String found = null;
        for( String sessionId : sessionIds ){
            ClientAddress address = clientAddresses.get(sessionId);
            if( address.getAppName().equals(clientAddress.getAppName()) &&
                    address.getAppVersion().equals(clientAddress.getAppVersion()) &&
                    address.getDeviceId().equals(clientAddress.getDeviceId()) ){
                // TODO: make sure to search based on userId
                found = sessionId;
                break;
            }
        }
        if( found != null ) {
            result = clientSessions.get(found);
        }

        return result;
    }

    /**
     *
     */
    private class BackgroundExecutor implements Runnable{

        private ServerRestMessage serverRestMessage;
        private WebSocketSession session;

        public BackgroundExecutor( WebSocketSession session, ServerRestMessage serverRestMessage ){
            this.session = session;
            this.serverRestMessage = serverRestMessage;
        }

        @Override
        public void run() {
            // construct the REST call
            OkHttpClient client = new OkHttpClient();

            Map<String, String> multipartEntities = serverRestMessage.getMultipartEntities();
            RequestBody body;

            if(multipartEntities != null) {
                MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
                multipartBuilder.setType(MultipartBody.FORM);

                for(Map.Entry<String, String> entity : multipartEntities.entrySet()) {
                    MultipartBody.Part part = MultipartBody.Part.createFormData(entity.getKey(), entity.getValue());
                    multipartBuilder.addPart(part);
                }

                body = multipartBuilder.build();
            } else {
                body = RequestBody.create(MediaType.parse("application/json"), serverRestMessage.getPayLoad());
            }

            Request request = new Request.Builder()
                    .url(serverRestMessage.getHost() + serverRestMessage.getPath())
                    .method(serverRestMessage.getMethod(), body)
                    .build();

            try {
                Response restCallResponse = client.newCall(request).execute();
                String result = restCallResponse.body().string();


//            RestClient client = new RestClient( serverRestMessage.getHost());
//            String result = client.GET( serverRestMessage.getPath());

                ClientMessage response = new ClientMessage(serverRestMessage.getRequestId());
                response.setTo(serverRestMessage.getFrom());
                response.setPayLoad(result);
                response.setMessageQOS(ClientMessageQOSEnum.DIRECT_CONNECTION_ONLY);

                logger.info("background task completed");

                sendToClient(session, response);
            }catch (IOException ex) {
                logger.error("Failed to read response from REST endpoint", ex);
            }
        }
    }
}