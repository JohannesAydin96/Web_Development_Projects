<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../header.jsp" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.List" %>

<h2>${product.id == 0 ? 'Add New Product' : 'Edit Product'}</h2>

<c:if test="${not empty errorMessage}">
    <div class="error-message">${errorMessage}</div>
</c:if>

<div class="form-container" style="max-width: 600px;">
    <form action="${pageContext.request.contextPath}/admin/products/save" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="${product.id}">
        
        <div class="form-group">
            <label for="name">Product Name:</label>
            <input type="text" id="name" name="name" value="${product.name}" required>
        </div>
        
        <div class="form-group">
            <label for="description">Description:</label>
            <textarea id="description" name="description" rows="4" required>${product.description}</textarea>
        </div>
        
        <div class="form-group">
            <label for="price">Price ($):</label>
            <input type="number" id="price" name="price" value="${product.price}" step="0.01" min="0" required>
        </div>
        
        <div class="form-group">
            <label for="stock">Stock Quantity:</label>
            <input type="number" id="stock" name="stock" value="${product.stock}" min="0" required>
        </div>
        
        <div class="form-group">
            <label for="imageFile">Product Image:</label>
            <input type="file" id="imageFile" name="imageFile" accept="image/*" ${product.id == 0 ? 'required' : ''}>
            <p class="help-text">Supported formats: JPG, JPEG, PNG, GIF</p>
            
            <div class="image-preview-container" style="margin-top: 10px;">
                <c:if test="${not empty product.imageUrl}">
                    <p>Current image: ${product.imageUrl}</p>
                    <img id="imagePreview" src="${pageContext.request.contextPath}/images/${product.imageUrl}" 
                         alt="Product Image Preview" style="max-width: 200px; max-height: 200px;">
                    <input type="hidden" name="currentImageUrl" value="${product.imageUrl}">
                </c:if>
                <c:if test="${empty product.imageUrl}">
                    <img id="imagePreview" src="${pageContext.request.contextPath}/images/placeholder.jpg" 
                         alt="Product Image Preview" style="max-width: 200px; max-height: 200px; display: none;">
                </c:if>
            </div>
        </div>
        
        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Save Product</button>
            <a href="${pageContext.request.contextPath}/admin/products" class="btn">Cancel</a>
        </div>
    </form>
</div>

<script>
    // Show image preview when a new image is selected
    document.getElementById('imageFile').addEventListener('change', function() {
        const preview = document.getElementById('imagePreview');
        const file = this.files[0];
        
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                preview.src = e.target.result;
                preview.style.display = 'block';
            }
            reader.readAsDataURL(file);
        } else {
            preview.src = '${pageContext.request.contextPath}/images/placeholder.jpg';
            preview.style.display = 'none';
        }
    });
</script>

<%@ include file="../footer.jsp" %>
