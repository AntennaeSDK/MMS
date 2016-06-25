
/**
 * Created by snambi on 6/23/16.
 */

package com.github.antennaesdk.common.messages;

import com.github.antennaesdk.common.messages.ClientAddress;
import com.github.antennaesdk.common.messages.ClientMessage;
import org.junit.Assert;
import org.junit.Test;

public class ClientMessageTest {

    @Test
    public void test(){
        ClientMessage clientMessage = new ClientMessage();

        clientMessage.setPayLoad("Awesome payload");

        ClientAddress clientAddress = new ClientAddress();
        clientAddress.setAppName("com.myapp.testapp");
        clientAddress.setAppVersion("0.1.2");
        clientAddress.setDeviceId("t656-0099");
        clientAddress.setUser("test8");

        clientMessage.setTo( clientAddress );

        String json = clientMessage.toJsonPretty();

        System.out.println("JSON: \n" + json );

        Assert.assertNotNull(json);
    }
}
