<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<h2><spring:message code="label.extend"/></h2>
<form:form method="post" modelAttribute="extensionForm" cssClass="form-horizontal" enctype="multipart/form-data">

    <div class="form-group">
        <form:label path="signedFile" cssClass="col-sm-2 control-label">
            <spring:message code="label.signed.file" />
        </form:label>
        <div class="col-sm-4">
            <form:input path="signedFile" type="file" /> 
        </div>
        <div class="col-xs-4 col-md-offset-2">
            <form:errors path="signedFile" cssClass="text-danger" />
        </div>
    </div>
    
    <div class="form-group">
        <form:label path="originalFile" cssClass="col-sm-2 control-label">
            <spring:message code="label.original.file" />
        </form:label>
        <div class="col-sm-4">
            <form:input path="originalFile" type="file" />
        </div>
    </div>

    <div class="form-group">
        <form:label path="signatureForm" cssClass="col-sm-2 control-label">
            <spring:message code="label.signature.form" />
        </form:label>
        <div class="col-sm-6">
            <c:forEach var="sigForm" items="${signatureForms}">
                <label class="radio-inline">
                    <form:radiobutton path="signatureForm" value="${sigForm}" /> <spring:message code="label.${sigForm}" />
                </label>
            </c:forEach>
        </div>
        <div class="col-xs-4">
            <form:errors path="signatureForm" cssClass="text-danger" />
        </div>
    </div>
    
    <div class="form-group">
        <form:label path="signaturePackaging" cssClass="col-sm-2 control-label">
            <spring:message code="label.signature.packaging" />
        </form:label>
        <div class="col-sm-6">
            <c:forEach var="sigPack" items="${signaturePackagings}">
                <label class="radio-inline">
                    <form:radiobutton path="signaturePackaging" value="${sigPack}" /> <spring:message code="label.${sigPack}" />
                </label>
            </c:forEach>
        </div>
        <div class="col-xs-3">
            <form:errors path="signaturePackaging" cssClass="text-danger" />
        </div>
    </div>
    
    <div class="form-group">
        <form:label path="signatureLevel" cssClass="col-sm-2 control-label">
            <spring:message code="label.signature.level" />
        </form:label>
        <div class="col-xs-4">
            <form:select path="signatureLevel" cssClass="form-control input-sm">
                <form:option value="" />
                <form:options items="${signatureLevels}" />
            </form:select>
        </div>
        <div class="col-xs-4 col-md-offset-2">
            <form:errors path="signatureLevel" cssClass="text-danger" />
        </div>
    </div>
    
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-primary"><spring:message code="label.submit"/></button>
            <button type="reset" class="btn"><spring:message code="label.clear"/></button>
        </div>
    </div>   
</form:form>

