package com.github.antennaesdk.common.messages;

import com.github.antennaesdk.common.messages.ClientAddress;
import com.github.antennaesdk.common.messages.ServerRestMessage;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by snambi on 6/24/16.
 */
public class ServerRestMessageTest {

    public static ServerRestMessage create(){
        ClientAddress from = new ClientAddress();
        from.setUser("test-user");
        from.setDeviceId("66594773-84663");
        from.setAppName("mobile-maps");
        from.setAppVersion("4.3.2");

        ServerRestMessage message = new ServerRestMessage();

        message.setPayLoad(null);
        message.setMethod("GET");
        message.setHost("https://api.com");
        message.setPath("/car/4444");
        message.setFrom( from );

        return message;
    }

    @Test
    public void toJson(){

        ServerRestMessage message = create();

        String json = message.toJsonPretty();
        System.out.println("JSON:\n"+ json );

        Assert.assertNotNull( json);
    }

    @Test
    public void fromJson(){

        ServerRestMessage message = create();

        String json = message.toJsonPretty();
        System.out.println("JSON:\n"+ json );

        ServerRestMessage msg = ServerRestMessage.fromJson(json);

        Assert.assertNotNull( msg );
        Assert.assertEquals( message.getRequestId(), msg.getRequestId() );
    }
}
