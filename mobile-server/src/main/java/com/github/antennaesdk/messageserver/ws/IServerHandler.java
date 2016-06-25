package com.github.antennaesdk.messageserver.ws;


import com.github.antennaesdk.common.messages.ServerMessage;

/**
 * Created by snambi on 6/22/16.
 */
public interface IServerHandler {

    // Response is expected by the client
    public void processRequestResponse(String wsSessionId, ServerMessage message);

    // Response is not expected by the client
    public void processPubSub(ServerMessage message);

}
