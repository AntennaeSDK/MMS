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
package com.github.antennaesdk.messageserver.rest;


import com.github.antennaesdk.common.messages.ClientMessage;
import com.github.antennaesdk.messageserver.ws.ClientTextWebSocketHandler;
import com.github.antennaesdk.server.messages.ClientMessageWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by snambi on 6/23/16.
 */
@Controller
public class MessageController {

    @Inject
    ClientTextWebSocketHandler clientTextWebSocketHandler;

    @RequestMapping(value="/api/messages", method= RequestMethod.POST )
    @ResponseBody
    public ClientMessage sendDownstreamMessage(@RequestBody String json ){
        ClientMessage clientMessage=null;

        try {

            String jsonStr = URLDecoder.decode(json, "UTF8");
            if( jsonStr.endsWith("}=") ){
                jsonStr = jsonStr.replace("}=", "}");
            }

            clientMessage = ClientMessage.fromJson(jsonStr);

            ClientMessageWrapper wrapper = new ClientMessageWrapper();
            wrapper.setClientMessage( clientMessage );

            clientTextWebSocketHandler.sendToClient( wrapper );

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return clientMessage;
    }
}
