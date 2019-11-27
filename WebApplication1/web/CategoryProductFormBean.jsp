<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%--
    This file is an entry point for JavaServer Faces application.
--%>
<f:view>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
            <title>JSP Page</title>
        </head>
        <body>
            <center>
                <h:form>
                    <h:selectOneMenu value="#{categoryProductFormBean.selectedCategory}" valueChangeListener="#{categoryProductFormBean.loadProduct}" onchange="submit();">
                        <f:selectItems value="#{categoryProductFormBean.categoryList}"/>
                    </h:selectOneMenu>
                    <br/>
                    <h:dataTable value="#{categoryProductFormBean.productList}" var="pro">
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Name"/>
                            </f:facet>
                            <h:outputText value="#{pro.proName}"/>
                        </h:column>
                        <h:column>
                            <f:facet name="header">
                                <h:outputText value="Price"/>
                            </f:facet>
                            <h:outputText value="#{pro.price}"/>
                        </h:column>
                    </h:dataTable>
                </h:form>
            </center>
        </body>
    </html>
</f:view>
