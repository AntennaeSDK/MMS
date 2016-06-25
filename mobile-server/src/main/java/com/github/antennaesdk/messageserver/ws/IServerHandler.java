package com.github.antennaesdk.messageserver.ws;


import com.github.antennaesdk.common.messages.ServerMessage;

/**
 * <code>IServerHandler</code> handles the messages from Server-side.
 *
 * Clients from datacenter/in-house connect to "/server" end-point.
 * All traffic that goes through "/server" end-point will be handled by <code>IServerHandler</code>
 *
 */
public interface IServerHandler {

    // Response is expected by the client
    public void processRequestResponse(String wsSessionId, ServerMessage message);

    // Response is not expected by the client
    public void processPubSub(ServerMessage message);

}
