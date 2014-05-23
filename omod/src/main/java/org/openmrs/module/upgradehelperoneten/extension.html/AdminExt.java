/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.upgradehelperoneten.extension.html;

import java.util.LinkedHashMap;
import java.util.Map;

import org.openmrs.Order;
import org.openmrs.module.Extension;
import org.openmrs.module.web.extension.AdministrationSectionExt;

public class AdminExt extends AdministrationSectionExt {
	
	public Extension.MEDIA_TYPE getMediaType() {
		return Extension.MEDIA_TYPE.html;
	}
	
	public String getTitle() {
		return "upgradehelperoneten.title";
	}
	
	public Map<String, String> getLinks() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		//No need to show the mapping page if they are have a already upgraded to a 1.10+ version
		//Can't use version comparison in older versions of openmrs because it is buggy
		//So we use some hacky code to check if this is a 1.10.0+ version
		if (!isOneTenOrLater()) {
			map.put("module/upgradehelperoneten/MapDoseToConcepts.form", "upgradehelperoneten.mapDoseToConcepts");
		}
		map.put("http://bit.ly/1kaQRLT", "upgradehelperoneten.howtoimportpackages");
		return map;
	}
	
	private boolean isOneTenOrLater() {
		try {
			return Order.class.getDeclaredMethod("getOrderNumber") != null;
		}
		catch (NoSuchMethodException e) {
			//ignore
		}
		return false;
	}
}
