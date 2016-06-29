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
package com.github.antennaesdk.messageserver.cli;

/**
 * <code>InputParameters</code> all the command-line parameters are stored in this class for later use.
 * This class is a singleton and values are writable only once.
 *
 */
public class InputParameters {

    private static final InputParameters INSTANCE = new InputParameters();

    private String configFile;
    private String gcmSenderId;
    private String gcmApiKey;
    private String gcmHost  = null;
    private String gcmPreProdEndPoint = null;
    private String gcmProdEndPoint = null;

    private int httpPort = HTTP_PORT;
    private int httpsPort = HTTPS_PORT;
    private int h2port = H2_PORT;

    static final int HTTP_PORT = 8080;
    static final int HTTPS_PORT = 8443;
    static final int H2_PORT = 9090;

    public static InputParameters getInstance(){
        return INSTANCE;
    }

    private InputParameters(){
    }

    public String getConfigFile() {
        return configFile;
    }
    public void setConfigFile(String configFile) {
        if( this.configFile == null ) {
            this.configFile = configFile;
        }else{
            throw new IllegalAccessError("Cannot change value");
        }
    }
    public String getGcmSenderId() {
        return gcmSenderId;
    }
    public void setGcmSenderId(String gcmSenderId) {
        if( this.gcmSenderId == null ){
            this.gcmSenderId = gcmSenderId;
        }else{
            throw new IllegalAccessError("Cannot change value");
        }
    }
    public String getGcmApiKey() {
        return gcmApiKey;
    }
    public void setGcmApiKey(String gcmApiKey) {
        if( this.gcmApiKey == null ) {
            this.gcmApiKey = gcmApiKey;
        }else{
            throw new IllegalAccessError("Cannot change value");
        }
    }
    public String getGcmHost() {
        return gcmHost;
    }
    public void setGcmHost(String gcmHost) {
        if( this.gcmHost == null ) {
            this.gcmHost = gcmHost;
        }else{
            throw new IllegalAccessError("Cannot change value");
        }
    }
    public String getGcmPreProdEndPoint() {
        return gcmPreProdEndPoint;
    }
    public void setGcmPreProdEndPoint(String gcmPreProdEndPoint) {
        if( this.gcmPreProdEndPoint == null ) {
            this.gcmPreProdEndPoint = gcmPreProdEndPoint;
        }else{
            throw new IllegalAccessError("Cannot change value");
        }
    }
    public String getGcmProdEndPoint() {
        return gcmProdEndPoint;
    }
    public void setGcmProdEndPoint(String gcmProdEndPoint) {
        if( this.gcmProdEndPoint == null ) {
            this.gcmProdEndPoint = gcmProdEndPoint;
        }else{
            throw new IllegalAccessError("Cannot change value");
        }
    }
    public int getHttpPort() {
        return httpPort;
    }
    public void setHttpPort(int httpPort) {
        if( this.httpPort == HTTP_PORT ) {
            this.httpPort = httpPort;
        }else{
            throw new IllegalAccessError("Cannot change value");
        }
    }
    public int getHttpsPort() {
        return httpsPort;
    }
    public void setHttpsPort(int httpsPort) {
        if( this.httpsPort == HTTPS_PORT ) {
            this.httpsPort = httpsPort;
        }else{
            throw new IllegalAccessError("Cannot change value");
        }
    }
    public int getH2port() {
        return h2port;
    }
    public void setH2port(int h2port) {
        if( this.h2port == H2_PORT ) {
            this.h2port = h2port;
        }else{
            throw new IllegalAccessError("Cannot change value");
        }
    }
}
