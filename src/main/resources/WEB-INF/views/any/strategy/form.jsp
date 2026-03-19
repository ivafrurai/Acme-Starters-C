<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="any.strategy.form.ticker" path="ticker"/>
	<acme:form-textbox code="any.strategy.form.name" path="name"/>
	<acme:form-textarea code="any.strategy.form.description" path="description"/>
	<acme:form-moment code="any.strategy.form.startMoment" path="startMoment"/>
	<acme:form-moment code="any.strategy.form.endMoment" path="endMoment"/>
	<acme:form-url code="any.strategy.form.moreInfo" path="moreInfo"/>
	
	<acme:button code="any.strategy.form.button.tactics" action="/any/tactic/list?strategyId=${id}"/>
	
</acme:form>