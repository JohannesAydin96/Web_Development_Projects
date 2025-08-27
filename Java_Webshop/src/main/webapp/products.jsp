<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>

<h2>Products</h2>

<c:if test="${not empty errorMessage}">
    <div class="error-message">${errorMessage}</div>
</c:if>

<c:if test="${not empty sessionScope.successMessage}">
    <div class="success-message">${sessionScope.successMessage}</div>
    <c:remove var="successMessage" scope="session" />
</c:if>

<style>
    /* Override product card styles with inline styles */
    .products-container {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
        gap: 20px;
        margin-top: 20px;
    }
    
    .product-card {
        background-color: white;
        border-radius: 5px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        padding: 15px;
        display: flex;
        flex-direction: column;
        height: 450px; /* Fixed height */
    }
    
    .product-image {
        width: 100%;
        height: 200px;
        object-fit: cover;
        border-radius: 5px;
        margin-bottom: 10px;
    }
    
    .product-name {
        font-size: 18px;
        font-weight: bold;
        margin-bottom: 5px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
    
    .product-price {
        font-size: 16px;
        color: #e44d26;
        margin-bottom: 10px;
    }
    
    .product-description {
        margin-bottom: 15px;
        color: #666;
        display: -webkit-box;
        -webkit-line-clamp: 3; /* Limit to 3 lines */
        -webkit-box-orient: vertical;
        overflow: hidden;
        text-overflow: ellipsis;
        max-height: 60px; /* Approximately 3 lines of text */
    }
    
    .product-card form {
        margin-top: auto; /* Push form to bottom of card */
    }
    
    /* Style for admin tooltip */
    .admin-tooltip {
        position: absolute;
        background-color: rgba(0, 0, 0, 0.8);
        color: white;
        padding: 5px 10px;
        border-radius: 4px;
        font-size: 12px;
        bottom: 50px;
        left: 50%;
        transform: translateX(-50%);
        white-space: nowrap;
        display: none;
        z-index: 100;
    }
    
    .admin-button-wrapper {
        position: relative;
        width: 100%;
    }
    
    .admin-button-wrapper:hover .admin-tooltip {
        display: block;
    }
</style>

<div class="products-container">
    <c:forEach var="product" items="${products}">
        <div class="product-card">
            <img src="${pageContext.request.contextPath}/images/${product.imageUrl}" alt="${product.name}" class="product-image">
            <div class="product-name">${product.name}</div>
            <div class="product-price">$${product.price}</div>
            <div class="product-description">${product.description}</div>
            
            <c:choose>
                <c:when test="${sessionScope.userRole != null && sessionScope.userRole.equalsIgnoreCase('ADMIN')}">
                    <%-- Admin user - show the form but make it non-functional --%>
                    <form onsubmit="return false;" style="margin-top: auto;">
                        <div class="form-group" style="display: flex; align-items: center;">
                            <label for="quantity-${product.id}" style="margin-right: 10px;">Quantity:</label>
                            <input type="number" id="quantity-${product.id}" name="quantity" value="1" min="1" max="${product.stock}" style="width: 60px;">
                        </div>
                        
                        <div class="admin-button-wrapper">
                            <button type="button" class="btn" style="width: 100%; margin-top: 10px;" 
                                    onclick="showAdminMessage()">Add to Basket</button>
                            
                        </div>
                    </form>
                </c:when>
                <c:otherwise>
                    <%-- Regular user - functional form --%>
                    <form action="${pageContext.request.contextPath}/basket" method="post">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="${product.id}">
                        
                        <div class="form-group" style="display: flex; align-items: center;">
                            <label for="quantity-${product.id}" style="margin-right: 10px;">Quantity:</label>
                            <input type="number" id="quantity-${product.id}" name="quantity" value="1" min="1" max="${product.stock}" style="width: 60px;">
                        </div>
                        
                        <button type="submit" class="btn" style="width: 100%; margin-top: 10px;">Add to Basket</button>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
    </c:forEach>
</div>

<script>
    function showAdminMessage() {
        alert("Admin users cannot add items to the basket.");
    }
</script>

<%@ include file="footer.jsp" %>