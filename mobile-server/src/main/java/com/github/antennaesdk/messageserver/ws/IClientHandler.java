package com.github.antennaesdk.messageserver.ws;


import com.github.antennaesdk.common.messages.ServerMessage;
import com.github.antennaesdk.server.messages.ClientMessageWrapper;

/**
 * Created by snambi on 6/22/16.
 */
public interface IClientHandler {

    // mostly received from clients to be processed in the server
    public void receiveFromClient(ServerMessage serverMessage);

    // to be sent to the client
    public void sendToClient(ClientMessageWrapper clientMessage);

}
