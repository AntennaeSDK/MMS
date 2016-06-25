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
package com.github.antennaesdk.server.messages;

import com.google.gson.Gson;
import com.github.antennaesdk.common.messages.ServerMessage;

/**
 * <code>ServerMessageWrapper</code> encapsulates a <code>ServerMessage</code> on the server + messaging-broker side.
 * It contains additional data to identify the broker-node that has the persistent connection to the client.
 *
 * @see ServerMessage
 */
public class ServerMessageWrapper {

    private ServerMessage serverMessage;

    // these identify the nodeId + sessionId that has the persistent connection to the client
    // if these are null, then a discovery needs to be made
    private String sessionId;
    private String nodeId;


    // utility methods
    public String toJson(){
        Gson gson = new Gson();
        String result = gson.toJson(this);
        return result;
    }
    public static ServerMessageWrapper fromJson(String value ){
        Gson gson = new Gson();
        ServerMessageWrapper message = gson.fromJson( value, ServerMessageWrapper.class);
        return message;
    }


    // getters and setters
    public ServerMessage getServerMessage() {
        return serverMessage;
    }
    public void setServerMessage(ServerMessage serverMessage) {
        this.serverMessage = serverMessage;
    }
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public String getNodeId() {
        return nodeId;
    }
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
}
