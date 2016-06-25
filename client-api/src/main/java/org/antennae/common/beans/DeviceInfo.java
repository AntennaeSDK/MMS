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

package org.antennae.common.beans;

import com.google.gson.Gson;
import org.antennae.common.beans.PhoneTypeEnum;

import javax.persistence.*;

/**
 * <code>DeviceInfo</code> holds all information specific to a device.
 * The device can be android, iOS or iOT.
 */
@Entity
@Table(name="DEVICE_INFO")
public class DeviceInfo {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="DEVICE_ID")
    private String deviceId;
	
	@Column(name="SW_VERSION")
    private String softwareVersion;
	
	@Column(name="SIM_SERIAL_NUM")
    private String simSerialNumber;
    
	@Column(name="VOICEMAIL_NUM")
	private String voiceMailNumber;
	
	@Column(name="SIM_OPERATOR")
    private String simOperatorName;

	@Column(name="PHONE_NUM")
    private String phoneNumber;
	
	@Column(name="PHONE_TYPE")
    private PhoneTypeEnum phoneType;
	
	@Column(name="NETWORK_COUNTRY")
    private String networkCountryIso;
	
	@Column(name="NETWORK_OPERATOR_ID")
    private String networkOperatorId;
	
	@Column(name="NETWORK_OPERATOR_NAME")
    private String networkOperatorName;


	// utility methods
	public String toJson(){
		Gson gson = new Gson();
		return gson.toJson( this);
	}
	public static DeviceInfo fromJson( String json ){
		Gson gson = new Gson();
		return gson.fromJson(json, DeviceInfo.class);
	}


    // Generated getters and setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getSoftwareVersion() {
		return softwareVersion;
	}
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
	public String getSimSerialNumber() {
		return simSerialNumber;
	}
	public void setSimSerialNumber(String simSerialNumber) {
		this.simSerialNumber = simSerialNumber;
	}
	public String getVoiceMailNumber() {
		return voiceMailNumber;
	}
	public void setVoiceMailNumber(String voiceMailNumber) {
		this.voiceMailNumber = voiceMailNumber;
	}
	public String getSimOperatorName() {
		return simOperatorName;
	}
	public void setSimOperatorName(String simOperatorName) {
		this.simOperatorName = simOperatorName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public PhoneTypeEnum getPhoneType() {
		return phoneType;
	}
	public void setPhoneType(PhoneTypeEnum phoneType) {
		this.phoneType = phoneType;
	}
	public String getNetworkCountryIso() {
		return networkCountryIso;
	}
	public void setNetworkCountryIso(String networkCountryIso) {
		this.networkCountryIso = networkCountryIso;
	}
	public String getNetworkOperatorId() {
		return networkOperatorId;
	}
	public void setNetworkOperatorId(String networkOperatorId) {
		this.networkOperatorId = networkOperatorId;
	}
	public String getNetworkOperatorName() {
		return networkOperatorName;
	}
	public void setNetworkOperatorName(String networkOperatorName) {
		this.networkOperatorName = networkOperatorName;
	}


}
