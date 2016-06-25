/*
 * Copyright 2015 the original author or authors.
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

package com.github.antennaesdk.messageserver.gcm.xmpp;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Stanza;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.util.Map;

public class GcmStanzaListener implements StanzaListener {
	
	//private static final Logger logger = Logger.getLogger(name)(GcmStanzaListener.class);
	private static final Logger logger = Logger.getLogger(GcmStanzaListener.class);

	@Override
	public void processPacket(Stanza packet) throws NotConnectedException {
		
		logger.info("processing packet : " + packet );
		//EmptyResultIQ incomingMessage = packet;
		//ServerMessage incomingMessage = (ServerMessage) packet;
		
		Stanza incomingMessage = ( Stanza) packet;
        GcmPacketExtension gcmPacket =(GcmPacketExtension) incomingMessage.getExtension(GcmXmppClient.GCM_NAMESPACE);
        
        if( gcmPacket == null ){
            logger.info("incoming gcm  message is null. returning ");
            return;
        }
        
        String json = gcmPacket.getJson();
        
        logger.info("processing [" + json + "]");
		
        try {
        	
            @SuppressWarnings("unchecked")
            Map<String, Object> jsonObject = (Map<String, Object>) JSONValue.parseWithException(json);

            // present for "ack"/"nack", null otherwise
            Object messageType = jsonObject.get("message_type");

            if (messageType == null) {
                // Normal upstream data message
                //handleUpstreamMessage(jsonObject);

                // Send ACK to CCS
                // String messageId = (String) jsonObject.get("message_id");
                // String from = (String) jsonObject.get("from");
                //String ack = createJsonAck(from, messageId);
                //send(ack);
            	logger.info("Normal Upstream message : " + jsonObject);
                
            } else if ("ack".equals(messageType.toString())) {
                  // Process Ack
                  // handleAckReceipt(jsonObject);
            	logger.info("ACK message : " + jsonObject);
                  
            } else if ("nack".equals(messageType.toString())) {
                  // Process Nack
                  // handleNackReceipt(jsonObject);
            	logger.info("NACK message : " + jsonObject);
                  
            } else if ("control".equals(messageType.toString())) {
                  // Process control message
                  // handleControlMessage(jsonObject);
            		logger.info("NACK message : " + jsonObject);
            } else {
                  logger.warn("Unrecognized message type (%s) " + messageType.toString());
            }
            
            
        } catch (ParseException e) {
            logger.error("Error parsing JSON " + json +", error: " + e);
        } catch (Exception e) {
            logger.error("Failed to process packet" + e);
        }
	}

}
