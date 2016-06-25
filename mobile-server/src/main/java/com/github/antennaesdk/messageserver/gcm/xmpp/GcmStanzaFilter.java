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

import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Stanza;

public class GcmStanzaFilter implements StanzaFilter {

	private String projectId;
	
	public GcmStanzaFilter( String projectId ) {
		this.projectId = projectId;
	}
	
	@Override
	public boolean accept(Stanza stanza) {
		
		boolean result = false;
		
		if(stanza.getClass() == Stanza.class ){
			result = true;
		}else{
			if(stanza.getTo()!= null){
				if(stanza.getTo().startsWith(projectId) ){
					result = true;
				}
			}
		}
		
		return result;
	}
}
