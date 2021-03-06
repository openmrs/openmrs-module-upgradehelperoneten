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
package org.openmrs.module.upgradehelperoneten.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.upgradehelperoneten.DoseToConceptMapping;
import org.openmrs.module.upgradehelperoneten.MappingsModel;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This class configured as controller using annotation and mapped with the URL of
 * 'module/upgradehelperoneten/MapDoseToConcepts.form'.
 */
@Controller
@RequestMapping(value = "module/upgradehelperoneten/MapDoseToConcepts.form")
public class MapDoseToConceptsFormController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	/** Success form view name */
	private final String FORM_VIEW = "/module/upgradehelperoneten/MapDoseToConceptsForm";
	
	/** Path for storing properties */
	private final String path = OpenmrsUtil.getApplicationDataDirectory() + "order_entry_upgrade_settings.txt";
	
	/**
	 * Initially called after the formBackingObject method to get the landing form name
	 * 
	 * @return String form view name
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String showForm() {
		return FORM_VIEW;
	}
	
	/**
	 * @param httpSession
	 * @param mm
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(HttpSession httpSession, @ModelAttribute("mm") MappingsModel mm) {
		
		Properties props = new Properties();
		try {
			// create properties file if it doesn't exist
			File f = new File(path);
			if (!f.exists()) {
				f.createNewFile();
			}
			List<DoseToConceptMapping> mappings = mm.getMappings();
			// store the properties
			for (DoseToConceptMapping m : mappings) {
				if (StringUtils.isNotBlank(m.getText()) && StringUtils.isNotBlank(m.getConceptId())) {
					props.setProperty(m.getText(), m.getConceptId().toString());
				}
			}
			props.store(new FileOutputStream(path), null);
		}
		catch (Exception e) {
			httpSession.setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    "An error occurred while storing the properties: " + e.getMessage());
			return FORM_VIEW;
		}
		
		httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "upgradehelperoneten.mappings.saved");
		return FORM_VIEW;
	}
	
	@ModelAttribute("mm")
	protected MappingsModel formBackingObject(HttpServletRequest request) {
		
		// Spring is designed to work with a single form object
		MappingsModel mm = new MappingsModel();
		List<DoseToConceptMapping> mappings = mm.getMappings();
		Properties props = new Properties();
		
		try {
			Set<String> units = getUniqueNonNullColumnValues("units", "drug_order");
			// create properties file if it doesn't exist
			File f = new File(path);
			if (!f.exists()) {
				f.createNewFile();
			}
			// load the properties
			props.load(new FileInputStream(path));
			
			for (String unit : units) {
				DoseToConceptMapping unitsMapping = new DoseToConceptMapping(unit, false);
				String unitsConceptId = props.getProperty(unit);
				if (unitsConceptId != null) {
					//sanity check
					Integer.valueOf(unitsConceptId);
					unitsMapping.setConceptId(unitsConceptId);
				}
				addMapping(mappings, unitsMapping);
			}
			
			Set<String> frequencies = getUniqueNonNullColumnValues("frequency", "drug_order");
			for (String frequency : frequencies) {
				DoseToConceptMapping frequencyMapping = new DoseToConceptMapping(frequency, true);
				String frequencyConceptId = props.getProperty(frequency);
				if (frequencyConceptId != null) {
					Integer.valueOf(frequencyConceptId);
					frequencyMapping.setConceptId(frequencyConceptId);
				}
				addMapping(mappings, frequencyMapping);
			}
			
			return mm;
		}
		catch (NumberFormatException e) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    "The order entry upgrade settings file contains invalid concept ids");
		}
		catch (Exception e) {
			log.warn("Error:", e);
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Some error occurred, see logs");
		}
		return null;
	}
	
	private void addMapping(List<DoseToConceptMapping> mappings, DoseToConceptMapping mapping) {
		if (!mappings.contains(mapping)) {
			mappings.add(mapping);
		}
	}
	
	/**
	 * Gets all unique values excluding nulls in the specified column and table
	 * 
	 * @param columnName the column
	 * @param tableName the table
	 * @return
	 * @throws Exception
	 */
	private <T> Set<T> getUniqueNonNullColumnValues(String columnName, String tableName) throws Exception {
		Set<T> uniqueValues = new HashSet<T>();
		final String alias = "unique_values";
		String select = "SELECT DISTINCT " + columnName + " AS " + alias + " FROM " + tableName + " WHERE " + columnName
		        + " IS NOT NULL";
		List<List<Object>> rows = Context.getAdministrationService().executeSQL(select, true);
		for (List<Object> row : rows) {
			//There can only be one column since we are selecting one
			uniqueValues.add((T) row.get(0));
		}
		
		return uniqueValues;
	}
}
