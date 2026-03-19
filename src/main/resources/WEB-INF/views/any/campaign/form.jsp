<%--
- form.jsp
-
- Copyright (C) 2012-2026 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="any.campaign.form.label.ticker" path="ticker"/>
	<acme:form-textbox code="any.campaign.form.label.name" path="name"/>
	<acme:form-textarea code="any.campaign.form.label.description" path="description"/>
	<acme:form-moment code="any.campaign.form.label.start-moment" path="startMoment"/>
	<acme:form-moment code="any.campaign.form.label.end-moment" path="endMoment"/>
	<acme:form-url code="any.campaign.form.label.more-info" path="moreInfo"/>
	<acme:form-double code="any.campaign.form.label.months-active" path="monthsActive" readonly="true"/>
	<acme:form-double code="any.campaign.form.label.effort" path="effort" readonly="true"/>
	<acme:form-textbox code="any.campaign.form.label.spokesperson" path="spokesperson.identity.fullName"/>

	<acme:button code="any.campaign.form.button.milestones" action="/any/milestone/list?campaignId=${id}"/>
	<acme:button code="any.campaign.form.button.spokesperson" action="/any/spokesperson/show?id=${spokespersonId}"/>
</acme:form>
