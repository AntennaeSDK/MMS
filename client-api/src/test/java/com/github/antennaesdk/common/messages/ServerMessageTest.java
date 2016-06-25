package com.github.antennaesdk.common.messages;

import com.github.antennaesdk.common.messages.ClientAddress;
import com.github.antennaesdk.common.messages.ServerMessage;
import com.github.antennaesdk.common.messages.ServerMessageTypeEnum;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by snambi on 6/24/16.
 */
public class ServerMessageTest {

    @Test
    public void test(){

        ClientAddress from = new ClientAddress();
        from.setUser("test-user");
        from.setDeviceId("66594773-84663");
        from.setAppName("mobile-maps");
        from.setAppVersion("4.3.2");

        ServerMessage serverMessage = new ServerMessage();

        serverMessage.setFrom( from );
        serverMessage.setMessageType( ServerMessageTypeEnum.REQUEST_RESPONSE);
        serverMessage.setTopic("/cars");
        serverMessage.setPayLoad("{ 'car': 'ford'}");

        String json = serverMessage.toJsonPretty();

        System.out.println( "JSON: \n"+ json );

        Assert.assertNotNull(json);
    }
}
