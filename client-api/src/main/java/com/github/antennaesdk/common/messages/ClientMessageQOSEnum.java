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

/**
 * Created by snambi on 6/24/16.
 */
public enum ClientMessageQOSEnum {

    /**
     * Deliver the message via direct connection or backends such as GCM/APNS
     * If direct connection is available then deliver it via that connection
     * or send the message via GCM/APNS/etc.
     *
     * This is an important message.
     */
    DIRECT_OR_THIRDPARTY,

    /**
     * Deliver the message only via direct connection.
     * The app is offline, then wait for the app to come online (direct-connection).
     *
     * The message is medium importance.
     */
    DIRECT_CONNECTION_ONLY,

    /**
     * Deliver the message if there is an active connection when the message arrived.
     * If the app is offline and/or there is no direct connection, simply discard the message.
     *
     * This is not an important message.
     */
    DISCARD;
}
