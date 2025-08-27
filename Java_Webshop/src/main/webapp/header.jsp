<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Web Shop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <header>
        <div class="container header-content">
            <a href="${pageContext.request.contextPath}/index.jsp" class="logo-link">
                <div class="logo">Web Shop</div>
            </a>
            <div class="nav-links">
                <c:choose>
                    <c:when test="${empty sessionScope.userId}">
                        <a href="${pageContext.request.contextPath}/login">Login</a>
                        <a href="${pageContext.request.contextPath}/register">Register</a>
                    </c:when>
                    <c:when test="${sessionScope.userRole eq 'ADMIN'}">
                        <!-- Admin Navigation Links -->
                        <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
                        <a href="${pageContext.request.contextPath}/admin/products">Manage Products</a>
                        <a href="${pageContext.request.contextPath}/products">View Store</a>
                        <a href="${pageContext.request.contextPath}/logout">Logout</a>
                        <span class="admin-badge">Admin: ${sessionScope.username}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/products">Products</a>
                        <div class="cart-wrapper">
                            <a href="${pageContext.request.contextPath}/basket" class="cart-link">
                                <svg class="cart-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24">
                                    <path fill="none" stroke="currentColor" stroke-width="2" d="M8 22a1 1 0 100-2 1 1 0 000 2zM19 22a1 1 0 100-2 1 1 0 000 2zM2.05 2.05h2l2.66 12.42a2 2 0 002 1.58h9.78a2 2 0 001.95-1.57l1.65-7.43H5.12"/>
                                </svg>
                                <c:if test="${not empty sessionScope.basketItemCount && sessionScope.basketItemCount > 0}">
                                    <span class="cart-count">${sessionScope.basketItemCount}</span>
                                </c:if>
                            </a>
                        </div>
                        <a href="${pageContext.request.contextPath}/orders">My Orders</a>
                        <a href="${pageContext.request.contextPath}/logout">Logout</a>
                        <span>Welcome, ${sessionScope.username}</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </header>
    <div class="container">