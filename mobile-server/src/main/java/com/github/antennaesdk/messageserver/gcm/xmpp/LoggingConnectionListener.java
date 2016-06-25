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
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

public class LoggingConnectionListener implements ConnectionListener {
	
	private Logger logger = Logger.getLogger(LoggingConnectionListener.class);

	@Override
	public void connected(XMPPConnection connection) {
		logger.info("Connected : " + connection.toString() );
	}

	@Override
	public void authenticated(XMPPConnection connection, boolean resumed) {
		logger.info( "Authenticated = " + resumed + " for connection: "+ connection);
	}

	@Override
	public void connectionClosed() {
		logger.info("connection closed");	
	}

	@Override
	public void connectionClosedOnError(Exception e) {
		logger.error("connection closed on error " + e );
	}

	@Override
	public void reconnectionSuccessful() {
		logger.info("reconnection successful");
	}

	@Override
	public void reconnectingIn(int seconds) {
		logger.info("reconnecting in \"" + seconds + "\" seconds");
		
	}

	@Override
	public void reconnectionFailed(Exception e) {
		logger.error("reconnection failed : " + e );
	}

}
