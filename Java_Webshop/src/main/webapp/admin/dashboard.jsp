<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../header.jsp" %>

<h2>Admin Dashboard</h2>

<c:if test="${not empty sessionScope.successMessage}">
    <div class="success-message">${sessionScope.successMessage}</div>
    <c:remove var="successMessage" scope="session" />
</c:if>

<c:if test="${not empty sessionScope.errorMessage}">
    <div class="error-message">${sessionScope.errorMessage}</div>
    <c:remove var="errorMessage" scope="session" />
</c:if>

<div class="admin-dashboard">
    <div class="admin-menu">
        <h3>Navigation</h3>
        <ul class="admin-nav-list">
            <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="admin-nav-link active">Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/products" class="admin-nav-link">Manage Products</a></li>
            <li><a href="${pageContext.request.contextPath}/products" class="admin-nav-link">View Store</a></li>
        </ul>
    </div>
    
    <div class="admin-stats">
        <h3>Statistics</h3>
        <div class="stat-cards">
            <div class="stat-card">
                <div class="stat-title">Total Products</div>
                <div class="stat-value">${productCount}</div>
            </div>
        </div>
    </div>
</div>

<div class="admin-actions" style="margin-top: 20px;">
    <a href="${pageContext.request.contextPath}/admin/products/new" class="btn btn-primary">Add New Product</a>
</div>

<%@ include file="../footer.jsp" %>