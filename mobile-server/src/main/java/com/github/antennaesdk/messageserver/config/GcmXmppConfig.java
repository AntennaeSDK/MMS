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

package com.github.antennaesdk.messageserver.config;


import com.github.antennaesdk.messageserver.gcm.xmpp.GcmXmppClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GcmXmppConfig {

	// TODO: ability to read the config values from a file or CLI

	@Bean
	public GcmXmppClient getGcmConnection() {

		// username = GCM_PROJECT_ID + @gcm.googleapis.com
		//final String user = "221875529200" + "@gcm.googleapis.com";
		final String user = "981962933635" + "@gcm.googleapis.com";

		// password = GCM_SERVER_KEY
		final String password = "AIzaSyCvUF4p_h1P88qvNDkLMPtjBizAbzrtaxA";
		
		// Gcm Project Number
		final String projectId = "981962933635";

		GcmXmppClient client = new GcmXmppClient(user, password, projectId);
		client.connect();
		
		return client;
	}
}
