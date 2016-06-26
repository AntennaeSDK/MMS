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
 * A client can be addressed by many methods.
 * Any one of the following is valid.
 * The messaging broker will attempt to deliver the message based on the address-data and message-quality.
 *
 * <ol>
 *     <li>User</li>
 *     <li>DeviceId, AppName </li>
 *     <li>User, DeviceId, AppName </li>
 *     <li>DeviceId, AppName, AppVersion</li>
 *     <li>User, DeviceId, AppName, AppVersion</li>
 * </ol>
 *
 * @see ClientMessage
 */
public class ClientAddress {

    // TODO: use TypeAdapterFactory instead of passing the type.
    private String classType = ClientAddress.class.getName();

    // fields
    private String registrationId;
    private String user;
    private String appName;
    private String appVersion;
    private String deviceId;


    public ClientAddress(){
    }
    public String getRegistrationId() {
        return registrationId;
    }
    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getAppName() {
        return appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public String getAppVersion() {
        return appVersion;
    }
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String toJson(){
        Gson gson = new Gson();
        String result = gson.toJson(this);
        return result;
    }

    public static ClientAddress fromJson(String json ){
        Gson gson = new Gson();
        ClientAddress result = gson.fromJson( json, ClientAddress.class);
        return result;
    }
}
