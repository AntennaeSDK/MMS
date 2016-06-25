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

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;

/**
 * <code>SampleContrller</code> provides the date-string as a response to GET method.
 */
@Controller
public class SampleController {

    @RequestMapping(value="/api/sample", method= RequestMethod.GET )
    @ResponseBody
    public String getSample(){

        Message message = new Message();
        message.id = "243522";
        message.type = "TEXT";
        message.sender = "N1";

        Message.Body body = new Message.Body();
        body.text = "Response from API :\n" + MicroTimestamp.INSTANCE.getMillis();

        message.body = body;

        return message.toJson();
    }

    // DONT USE IT.
    // ONLY USED FOR TESTING
    @Deprecated
    public static class Message{
        String id;
        String type;
        String version;
        String sender;
        String username;
        Body body;

        public static class Body{
            String text;
        }

        public String toJson(){
            Gson gson = new Gson();
            String result = gson.toJson(this);
            return result;
        }
        public static Message fromJson( String json ){
            Gson gson = new Gson();
            Message message = gson.fromJson( json, Message.class);
            return message;
        }
    }

    /**
     * Class to generate timestamps with microsecond precision
     * For example: MicroTimestamp.INSTANCE.get() = "2012-10-21 19:13:45.267128"
     */
    public enum MicroTimestamp
    {  INSTANCE ;

        private long              startDate ;
        private long              startNanoseconds ;
        private SimpleDateFormat dateFormat ;

        private MicroTimestamp()
        {  this.startDate = System.currentTimeMillis() ;
            this.startNanoseconds = System.nanoTime() ;
            this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") ;
        }

        public String get()
        {   long microSeconds = (System.nanoTime() - this.startNanoseconds) / 1000 ;
            long date = this.startDate + (microSeconds/1000) ;
            return this.dateFormat.format(date) + String.format("%03d", microSeconds % 1000) ;
        }

        public String getMillis(){
            return this.dateFormat.format(startDate);
        }
    }
}
