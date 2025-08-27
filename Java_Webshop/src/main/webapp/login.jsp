<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>

<div class="form-container">
    <h2>Login</h2>
    
    <c:if test="${not empty errorMessage}">
        <div class="error-message">${errorMessage}</div>
    </c:if>
    
    <c:if test="${not empty sessionScope.pendingAction && sessionScope.pendingAction eq 'add-to-cart'}">
        <div class="info-message">
            Please log in to add items to your basket.
        </div>
    </c:if>
    
    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
        </div>
        
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
        </div>
        
        <button type="submit" class="btn">Login</button>
    </form>
    
    <p style="margin-top: 15px;">
        Don't have an account? <a href="${pageContext.request.contextPath}/register">Register here</a>
    </p>
</div>

<%@ include file="footer.jsp" %>