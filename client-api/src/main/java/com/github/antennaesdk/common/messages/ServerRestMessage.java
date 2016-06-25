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
import com.google.gson.GsonBuilder;

import java.util.UUID;

/**
 * <code>ServerRestMessage</code> carries a REST api call to the mobile-broker.
 * Broker executes this call, and returns the result via <code>ClientMessage</code>.
 *
 * @see ClientMessage
 */
public class ServerRestMessage {

    // from where the message originates.
    // it can be from a user or from a server (bot)
    private ClientAddress from;

    // rest resource path such as "/api/books"
    // another example would be "/api/books?id=383763"
    // another example would be "/api/books/383763"
    private String path;

    // represents the "protocol//host:port" such as "https://toys.company.com:8443"
    // or port can be optional such as "https://toys.company.com"
    private String host;

    // represents REST method such as "GET", "POST", "PUT", "DELETE" etc
    // TODO: use an enum instead of string
    private String method;

    // actual message ( this the payload if its POST/PUT call )
    // this is optional
    private String payLoad;

    // unique identified to track the request on the client side.
    private String requestId;

    // TODO: use TypeAdapterFactory instead of passing the type.
    private String classType = ServerRestMessage.class.getName();

    // getters and setters
    public ServerRestMessage(){
        requestId = UUID.randomUUID().toString();
    }
    public ServerRestMessage( String requestId ){
        this.requestId = requestId;
    }
    public ClientAddress getFrom() {
        return from;
    }
    public void setFrom(ClientAddress from) {
        this.from = from;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public String getPayLoad() {
        return payLoad;
    }
    public void setPayLoad(String payLoad) {
        this.payLoad = payLoad;
    }
    public String getRequestId() {
        return requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    // utility methods
    public String toJson(){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }
    public String toJsonPretty(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);
        return json;
    }
    public static ServerRestMessage fromJson(String json ){
        Gson gson = new Gson();
        ServerRestMessage result = gson.fromJson( json, ServerRestMessage.class);
        return result;
    }
}
