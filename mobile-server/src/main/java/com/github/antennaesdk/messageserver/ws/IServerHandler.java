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
package com.github.antennaesdk.messageserver.ws;


import com.github.antennaesdk.common.messages.ServerMessage;

/**
 * <code>IServerHandler</code> handles the messages from Server-side.
 *
 * Clients from datacenter/in-house connect to "/server" end-point.
 * All traffic that goes through "/server" end-point will be handled by <code>IServerHandler</code>
 *
 */
public interface IServerHandler {

    // Response is expected by the client
    public void processRequestResponse(String wsSessionId, ServerMessage message);

    // Response is not expected by the client
    public void processPubSub(ServerMessage message);

}
