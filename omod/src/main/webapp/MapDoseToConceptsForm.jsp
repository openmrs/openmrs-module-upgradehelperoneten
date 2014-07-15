<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:htmlInclude file="/moduleResources/upgradehelperoneten/MapDoseToConcepts.css"/>

<h2><openmrs:message code="upgradehelperoneten.form.title"/></h2>
<br/>
<div class="info">
    <img src="<openmrs:contextPath />/images/info.gif" border="0" />
    In earlier OpenMRS versions, drug units and frequencies were stored as free-text, but starting with
    OpenMRS 1.10 these are stored using coded concepts. If your database includes drug orders, we need
    you to indicate which concept should represent each unique free-text value shown below.
    <br/>
    OpenMRS recommends that you get the reference lists of unit and frequency concepts from the
    <a href="https://wiki.openmrs.org/x/ww4JAg">CIEL dictionary</a> but in advanced use cases you can create your own concepts for this purpose.
    If you are using CIEL dictionary, make sure that you are running the latest version to have the
    frequency and dosing units concepts. If you are not using CIEL, You can import the concepts by
    downloading the order entry concept metadata package <a href="https://wiki.openmrs.org/x/jALpAw">here</a>
    and importing it using the <a href="https://wiki.openmrs.org/x/Xwo3AQ">metadata sharing module</a>
</div>
<br/>
<form:form modelAttribute="mm" method="post">
    <fieldset>

        <table cellpadding="5" cellspacing="5" border="0">
            <tr>
                <th><spring:message code="upgradehelperoneten.text" /></th>
                <th><spring:message code="upgradehelperoneten.concept" /></th>
                <th><spring:message code="upgradehelperoneten.isFrequency" /></th>
            </tr>
            <c:forEach var="m" items="${mm.mappings}" varStatus="s">
                <tr>
                    <td class="text">${m.text}</td>
                    <td class="concept">
                        <spring:bind path="mappings[${s.index}].conceptId">
                            <openmrs_tag:conceptField formFieldName="${status.expression}"
                                                      formFieldId="unitsConcept${s.index}" initialValue="${status.value}"
                                                      includeClasses="${m.isFrequencyMapping ? 'Frequency' : ''}" />
                            <c:if test="${status.errorMessage != ''}">
                                <span class="error">${status.errorMessage}</span>
                            </c:if>
                        </spring:bind>
                    </td>
                    <td class="isFrequency">
                        <spring:message code="${m.isFrequencyMapping ? 'general.yes' : 'general.no'}" />
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