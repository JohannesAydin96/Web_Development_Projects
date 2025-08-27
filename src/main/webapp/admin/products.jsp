<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../header.jsp" %>

<h2>Product Management</h2>

<c:if test="${not empty sessionScope.successMessage}">
    <div class="success-message">${sessionScope.successMessage}</div>
    <c:remove var="successMessage" scope="session" />
</c:if>

<c:if test="${not empty sessionScope.errorMessage}">
    <div class="error-message">${sessionScope.errorMessage}</div>
    <c:remove var="errorMessage" scope="session" />
</c:if>

<div class="admin-actions">
    <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn">Back to Dashboard</a>
    <a href="${pageContext.request.contextPath}/admin/products/new" class="btn btn-primary">Add New Product</a>
</div>

<div class="product-list">
    <table class="admin-table">
        <thead>
            <tr>
                
                <th>Image</th>
                <th>Name</th>
                <th>Price</th>
                <th>Stock</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="product" items="${products}">
                <tr>
                    <td>
                        <img src="${pageContext.request.contextPath}/images/${product.imageUrl}" 
                             alt="${product.name}" class="product-thumbnail"
                             onerror="this.src='${pageContext.request.contextPath}/images/placeholder.jpg'; this.onerror='';">
                    </td>
                    <td>${product.name}</td>
                    <td>$${product.price}</td>
                    <td>${product.stock}</td>
                    <td class="actions">
                        <a href="${pageContext.request.contextPath}/admin/products/edit?id=${product.id}" 
                           class="btn btn-small">Edit</a>
                        <form action="${pageContext.request.contextPath}/admin/products/delete" method="post" 
                              onsubmit="return confirm('Are you sure you want to delete this product?');" style="display: inline;">
                            <input type="hidden" name="productId" value="${product.id}">
                            <button type="submit" class="btn btn-small btn-danger">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="../footer.jsp" %>