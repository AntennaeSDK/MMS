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

package com.github.antennaesdk.messageserver.services.entity.impl;


import com.github.antennaesdk.common.beans.AppInfo;
import com.github.antennaesdk.messageserver.db.dao.IAppInfoDao;
import com.github.antennaesdk.messageserver.services.entity.IAppInfoService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class AppInfoServiceImpl implements IAppInfoService {
	
	@Inject
	private IAppInfoDao appInfoDao;

	@Override
	public void addAppInfo(AppInfo appInfo) {
		if( appInfo != null ){
			appInfoDao.addAppInfo(appInfo);
		}
	}

	@Override
	public AppInfo getAppInfo(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAppInfo(AppInfo appInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAppInfo(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<AppInfo> getAllAppInfos() {
		return appInfoDao.getAllAppInfos();
	}

}
