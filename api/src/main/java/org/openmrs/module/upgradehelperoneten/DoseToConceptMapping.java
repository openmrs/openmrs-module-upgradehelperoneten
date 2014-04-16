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
package org.openmrs.module.upgradehelperoneten;

import org.openmrs.DrugOrder;

public class DoseToConceptMapping {

    private String text;
    private Integer conceptId;
    private Boolean isFrequencyMapping;

    public DoseToConceptMapping(DrugOrder d, boolean isFrequencyMapping) {
        if (isFrequencyMapping) {
            text = d.getFrequency();
        } else {
            text = d.getUnits();
        }
        this.isFrequencyMapping = isFrequencyMapping;
    }

    public Integer getConceptId() {
        return conceptId;
    }

    public void setConceptId(Integer conceptId) {
        this.conceptId = conceptId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getIsFrequencyMapping() {
        return isFrequencyMapping;
    }

    public void setIsFrequencyMapping(Boolean isFrequencyMapping) {
        this.isFrequencyMapping = isFrequencyMapping;
    }
}
