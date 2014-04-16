<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:htmlInclude file="/moduleResources/upgradehelperoneten/MapDoseToConcepts.css"/>

<h2><openmrs:message code="upgradehelperoneten.form.title"/></h2>
<br/>
<form:form modelAttribute="mm" method="post">
    <fieldset>

        <table cellpadding="5" cellspacing="5" border="0">
            <tr>
                <th><spring:message code="upgradehelperoneten.text" /></th>
                <th><spring:message code="upgradehelperoneten.concept" /></th>
            </tr>
            <c:forEach var="m" items="${mm.mappings}" varStatus="s">
                <tr>
                    <td class="text">${m.text}</td>
                    <td class="concept">
                        <spring:bind path="mappings[${s.index}].conceptId">
                            <openmrs_tag:conceptField formFieldName="${status.expression}"
                                                      formFieldId="unitsConcept${s.index}" initialValue="${status.value}"
                                                      includeClasses="${m.isFrequencyMapping ? 'Frequency' : 'Units Of Measure'}" />
                            <c:if test="${status.errorMessage != ''}">
                                <span class="error">${status.errorMessage}</span>
                            </c:if>
                        </spring:bind>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <span class="buttons">
            <input type="submit" id="saveMappings" value='<openmrs:message code="upgradehelperoneten.mappings.save"/>' />
            <input type="button" id="cancel" value='<openmrs:message code="general.cancel"/>' onclick="javascript:window.location='<openmrs:contextPath />/admin'" />
        </span>
    </fieldset>
</form:form>
<%@ include file="/WEB-INF/template/footer.jsp"%>