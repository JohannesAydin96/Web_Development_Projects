<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>

<h2>Shopping Basket</h2>

<c:if test="${not empty errorMessage}">
    <div class="error-message">${errorMessage}</div>
</c:if>

<c:if test="${not empty successMessage}">
    <div class="success-message">${successMessage}</div>
</c:if>

<div class="basket-container">
    <c:choose>
        <c:when test="${empty basket.items}">
            <p>Your basket is empty.</p>
            <a href="${pageContext.request.contextPath}/products" class="btn">Continue Shopping</a>
        </c:when>
        <c:otherwise>
            <c:forEach var="item" items="${basket.items}">
                <div class="basket-item">
                    <div class="basket-item-details">
                        <img src="${pageContext.request.contextPath}/images/${item.product.imageUrl}" alt="${item.product.name}" class="basket-item-image">
                        <div>
                            <div class="basket-item-name">${item.product.name}</div>
                            <div class="basket-item-price">$${item.product.price}</div>
                        </div>
                    </div>
                    
                    <div class="basket-item-quantity">
                        <form action="${pageContext.request.contextPath}/basket" method="post" style="display: flex; align-items: center;">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="productId" value="${item.product.id}">
                            <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.product.stock}">
                            <button type="submit" class="btn" style="margin-left: 10px;">Update</button>
                        </form>
                        
                        <form action="${pageContext.request.contextPath}/basket" method="post" style="margin-left: 10px;">
                            <input type="hidden" name="action" value="remove">
                            <input type="hidden" name="productId" value="${item.product.id}">
                            <button type="submit" class="btn">Remove</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
            
            <div class="basket-total">
                <span>Total:</span>
                <span>$${basket.totalPrice}</span>
            </div>
            
            <div class="basket-actions">
                <a href="${pageContext.request.contextPath}/products" class="btn">Continue Shopping</a>
                
                <form id="placeOrderForm" action="${pageContext.request.contextPath}/orders" method="post">
                    <input type="hidden" name="action" value="place">
                    <button type="button" class="btn btn-primary" onclick="confirmOrder()">Place Order</button>
                </form>
                
                <form action="${pageContext.request.contextPath}/basket" method="post">
                    <input type="hidden" name="action" value="clear">
                    <button type="submit" class="btn">Clear Basket</button>
                </form>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<script>
function confirmOrder() {
    if (confirm("Are you sure you want to place this order?")) {
        document.getElementById("placeOrderForm").submit();
    }
}
</script>

<%@ include file="footer.jsp" %>