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

package com.github.antennaesdk.messageserver.rest;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class RestClient {

	private String server=null;
	private RestTemplate rest;
	private HttpHeaders headers;
	private HttpStatus status;
	
	public RestClient(String hostname){
		this(hostname, 0);
	}
	
	public RestClient(String hostname, int portnumer){
				
		if( portnumer == 0 || portnumer == 80){
			this.server = hostname;
		}else{
			this.server = hostname + ":" + portnumer;
		}
		
		this.rest = new RestTemplate();
		this.headers = new HttpHeaders();
	}
	
	public void addDefaultRestHeaders(){
		headers.add("Content-Type", "application/json");
		headers.add("Accept", "*/*");
	}
	
	public void addHeader(String header, String value){
		headers.add(header, value);
	}
	
	public String GET(String url){
		
		HttpEntity<String> requestEntity = new HttpEntity<String>("",headers);
		String fullurl = server + url;
		ResponseEntity<String> responseEntity = rest.exchange(fullurl, HttpMethod.GET, requestEntity, String.class);
		
		this.setStatus(responseEntity.getStatusCode());
		
		return responseEntity.getBody();
	}
	
	public String POST(String url, String json){
		
		HttpEntity<String> requestEntity = new HttpEntity<String>(json,headers);
		ResponseEntity<String> responseEntity = rest.exchange(server+url, HttpMethod.POST, requestEntity, String.class);
		
		this.setStatus(responseEntity.getStatusCode());
		
		return responseEntity.getBody();
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public void setHeaders(HttpHeaders headers) {
		this.headers = headers;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
