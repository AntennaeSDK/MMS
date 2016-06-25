package org.antennae.common.messages.util;

import org.antennae.common.messages.ServerRestMessage;
import org.antennae.common.messages.ServerRestMessageTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by snambi on 6/24/16.
 */
public class JsonUtilTest {

    @Test
    public void test(){
        ServerRestMessage message = ServerRestMessageTest.create();

        String json = message.toJsonPretty();

        String clazzName = JsonUtil.identifyClassType(json);

        System.out.println("Identified classname : " + clazzName);

        Assert.assertEquals( ServerRestMessage.class.getName(), clazzName);
    }
}
