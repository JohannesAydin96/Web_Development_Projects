<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>

<style>
/* Additional inline styles to fix specific issues */
.orders-container {
  max-width: 1000px;
  margin: 20px auto;
}

.order-card {
  background-color: white;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 30px;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding-bottom: 15px;
  border-bottom: 1px solid #ddd;
  margin-bottom: 15px;
}

.order-id {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 5px;
}

.order-date, .order-status {
  color: #666;
  margin-bottom: 5px;
}

.order-total {
  font-size: 18px;
  font-weight: bold;
  color: #e44d26;
}

.order-items-table {
  width: 100%;
  border-collapse: collapse;
}

.order-items-table th {
  text-align: left;
  padding: 12px 10px;
  border-bottom: 2px solid #ddd;
  font-weight: bold;
  color: #333;
  background-color: #f9f9f9;
}

.order-items-table td {
  padding: 15px 10px;
  border-bottom: 1px solid #ddd;
  vertical-align: middle;
}

.product-info {
  display: flex;
  align-items: center;
}

.order-item-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 5px;
  margin-right: 15px;
  border: 1px solid #eee;
}
</style>

<h2>My Orders</h2>

<c:if test="${not empty errorMessage}">
    <div class="error-message">${errorMessage}</div>
</c:if>

<c:if test="${not empty sessionScope.successMessage}">
    <div class="success-message">${sessionScope.successMessage}</div>
    <c:remove var="successMessage" scope="session" />
</c:if>

<div class="orders-container">
    <c:choose>
        <c:when test="${empty orders}">
            <p>You have no orders yet.</p>
            <a href="${pageContext.request.contextPath}/products" class="btn">Browse Products</a>
        </c:when>
        <c:otherwise>
            <c:forEach var="order" items="${orders}" varStatus="status">
                <div class="order-card ${status.index == 0 ? 'latest-order' : ''}">
                    <div class="order-header">
                        <div class="order-info">
                           <div class="order-id">Order #${orders.size() - status.index}</div>
                            <div class="order-date">Placed on: ${order.formattedOrderDate}</div>
                            <div class="order-status">Status: ${order.status}</div>
                        </div>
                        <div class="order-total">
                            Total: $${order.totalAmount}
                        </div>
                    </div>
                    
                    <div class="order-items">
                        <h3>Order Items</h3>
                        <table class="order-items-table">
                            <thead>
                                <tr>
                                    <th>Product</th>
                                    <th>Price</th>
                                    <th>Quantity</th>
                                    <th>Subtotal</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${order.items}">
                                    <tr>
                                        <td class="product-info">
                                            <img src="${pageContext.request.contextPath}/images/${item.product.imageUrl}" alt="${item.product.name}" class="order-item-image">
                                            <span>${item.product.name}</span>
                                        </td>
                                        <td>$${item.price}</td>
                                        <td>${item.quantity}</td>
                                        <td>$${item.subtotal}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:forEach>
            
            <div class="order-actions">
                <a href="${pageContext.request.contextPath}/products" class="btn">Continue Shopping</a>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="footer.jsp" %>