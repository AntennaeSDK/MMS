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

package com.github.antennaesdk.messageserver.services.impl;


import com.github.antennaesdk.common.beans.AppDetails;
import com.github.antennaesdk.common.beans.AppInfo;
import com.github.antennaesdk.common.beans.DeviceInfo;
import com.github.antennaesdk.messageserver.services.IRegistrationService;
import com.github.antennaesdk.messageserver.services.entity.IAppInfoService;
import com.github.antennaesdk.messageserver.services.entity.IDeviceInfoService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegistrationServiceImpl implements IRegistrationService {

	@Inject
	private IAppInfoService appInfoSvc;
	
	@Inject
	private IDeviceInfoService deviceInfoSvc;

	/**
	 *  1. check whether the device is registered
	 *  2. if not register the device
	 *  3. check whether the app is registered
	 *  4. if not register the app
	 *  5. if the app is registered, update the app info if needed
	 */
	@Override
	public void register(AppDetails appDetails) {
		
		if( appDetails == null ){
			throw new NullPointerException("AppDetails cannot be null");
		}

		deviceInfoSvc.addDeviceInfo(appDetails.getDeviceInfo());
		
		// set the deviceId 
		int deviceId = appDetails.getDeviceInfo().getId();
		appDetails.getAppInfo().setDeviceId(deviceId);
		
		appInfoSvc.addAppInfo( appDetails.getAppInfo()); 
	}

	@Override
	public List<AppDetails> getAllRegistrations() {
		
		List<AppInfo> allApps = appInfoSvc.getAllAppInfos();
		
		List<Integer> deviceIds = new ArrayList<Integer>();
		for( AppInfo appinfo : allApps){
			deviceIds.add(appinfo.getDeviceId());
		}
		
		List<DeviceInfo> deviceInfos = deviceInfoSvc.getDeviceInfos(deviceIds);
		
		// create the final list
		List<AppDetails> appDetails = new ArrayList<AppDetails>();
		
		for( AppInfo appinfo : allApps ){
			
			AppDetails app = new AppDetails();
			app.setAppInfo(appinfo);
			
			for( DeviceInfo device : deviceInfos ){
				if( device.getId() == appinfo.getDeviceId() ){
					app.setDeviceInfo(device);
					break;
				}
			}
			
			appDetails.add(app);
		}
		
		return appDetails;
	}
}
