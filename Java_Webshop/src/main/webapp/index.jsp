<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>

<div class="hero-section">
    <div class="hero-content">
        <h1>Welcome to Web Shop</h1>
        <p class="hero-subtitle">Your one-stop destination for quality products at amazing prices!</p>
        
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="success-message">${sessionScope.successMessage}</div>
            <% session.removeAttribute("successMessage"); %>
        </c:if>
        
        <div class="hero-buttons">
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Browse Products</a>
           
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>