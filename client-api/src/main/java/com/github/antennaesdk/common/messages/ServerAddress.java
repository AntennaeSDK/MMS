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
package com.github.antennaesdk.common.messages;

import com.google.gson.Gson;

/**
 * <code>ServerAddress</code> mainly used to direct messages to the clients.
 * It can be a user or an app or user/app/app-version combination.
 *
 * Created by snambi on 6/21/16.
 */
public class ServerAddress {

    private String topic;

    // TODO: use TypeAdapterFactory instead of passing the type.
    private String classType = ServerAddress.class.getName();

    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String toJson(){
        Gson gson = new Gson();
        String result = gson.toJson(this);
        return result;
    }

    public static ServerAddress fromJson(String json ){
        Gson gson = new Gson();
        ServerAddress result = gson.fromJson( json, ServerAddress.class);
        return result;
    }
}
