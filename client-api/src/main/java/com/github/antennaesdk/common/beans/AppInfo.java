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

import javax.persistence.*;

/**
 *  <code>AppInfo</code> contains information about the application.
 *
 *  The app may be an android or ios or iot app.
 *  This class encapsulates all information about the application.
 */
@Entity
@Table(name="APP_INFO")
public class AppInfo {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="DEVICE_ID")
	private int deviceId;
	
	@Column(name="GCM_REGID")
	private String gcmRegistrationId;

	@Column(name="APNS_REGID")
	private String apnsRegistrationId;

	@Column(name="APP_VERSION")
    private int appVersion;
	
	@Column(name="APP_ID")
    private String appId;
	
	@Column(name="FIRST_INSTLL_TIME")
    private long firstInstallTime;
	
	@Column(name="LAST_UPDATE_TIME")
    private long lastUpdateTime;


	// utility methods
	public String toJson(){
		Gson gson = new Gson();
		return gson.toJson( this);
	}
	public static AppInfo fromJson( String json ){
		Gson gson = new Gson();
		return gson.fromJson(json, AppInfo.class);
	}


	// getters and setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	public String getGcmRegistrationId() {
		return gcmRegistrationId;
	}
	public void setGcmRegistrationId(String gcmRegistrationId) {
		this.gcmRegistrationId = gcmRegistrationId;
	}
	public String getApnsRegistrationId() {
		return apnsRegistrationId;
	}
	public void setApnsRegistrationId(String apnsRegistrationId) {
		this.apnsRegistrationId = apnsRegistrationId;
	}
	public int getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(int appVersion) {
		this.appVersion = appVersion;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public long getFirstInstallTime() {
		return firstInstallTime;
	}
	public void setFirstInstallTime(long firstInstallTime) {
		this.firstInstallTime = firstInstallTime;
	}
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}


}
