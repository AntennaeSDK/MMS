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


import com.github.antennaesdk.common.beans.AppDetails;
import com.github.antennaesdk.messageserver.services.IRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AppRegistrationController {

	private static Logger logger = LoggerFactory.getLogger(AppRegistrationController.class);
	
	@Inject
	private IRegistrationService registrationSvc;

	/**
	 * The payload will look something similar
	 * {
		    "appInfo": {
		        "appId": "org.antennae.gcmtests.gcmtest",
		        "gcmRegistrationId": "gcm-registrationId-Gelk9jtQ90kphtdQ",
		        "appVersion": 1,
		        "firstInstallTime": 1437629332302,
		        "lastUpdateTime": 1437629332302
		    },
		    "deviceInfo": {
		        "deviceId": "000000000000000",
		        "networkCountryIso": "",
		        "networkOperatorId": "",
		        "networkOperatorName": ""
		    }
		}
	 * @param json
	 */
	@RequestMapping(value="/api/registration", method=RequestMethod.POST)
	@ResponseBody
	public void register( @RequestBody String json){
		logger.debug("JSON : " + json );
		
		AppDetails appDetails = AppDetails.fromJson(json);
		
		registrationSvc.register(appDetails);
	}
	
	/**
	 * Displays all the registerations
	 * @return
	 */
	@RequestMapping(value="/api/registration", method=RequestMethod.GET)
	@ResponseBody
	public List<AppDetails> get(){
		
		List<AppDetails> appDetails = new ArrayList<AppDetails>();
		logger.debug("Received the GET Api call");
		
		return registrationSvc.getAllRegistrations();
	}
}
