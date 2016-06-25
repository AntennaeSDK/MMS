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

package com.github.antennaesdk.common.beans;

import com.google.gson.Gson;

/**
 * <code>AppDetails</code> contains the complete details about the mobile/iot application.
 *
 * It contains,
 *
 * <ol>
 *     <li>Device information</li>
 *     <li>Application information</li>
 * </ol>
 *
 *
 */
public class AppDetails {

    private DeviceInfo deviceInfo;
    private AppInfo appInfo;


	// utility methods
	public String toJson(){
		Gson gson = new Gson();
		return gson.toJson( this);
	}
	public static AppDetails fromJson(String json ){
		Gson gson = new Gson();
		return gson.fromJson(json, AppDetails.class);
	}


	// getters and setters
	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	public AppInfo getAppInfo() {
		return appInfo;
	}
	public void setAppInfo(AppInfo appInfo) {
		this.appInfo = appInfo;
	}



}
