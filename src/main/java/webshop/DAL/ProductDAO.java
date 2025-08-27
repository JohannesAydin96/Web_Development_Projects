package webshop.DAL;

import webshop.DAL.Model.Product;
import webshop.DAL.Util.DatabaseUtil;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    
    // Get all products
    public List<Product> getAllProducts() throws SQLException {
        String sql = "SELECT * FROM products";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            List<Product> products = new ArrayList<>();
            
            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
            
            return products;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) DatabaseUtil.closeConnection(conn);
        }
    }
    
    // Get product by ID
    public Product getProductById(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractProductFromResultSet(rs);
            }
            
            return null;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) DatabaseUtil.closeConnection(conn);
        }
    }
    
    // Create a new product
    public void createProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, description, price, stock, image_url) VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            stmt.setString(5, product.getImageUrl());
            
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) DatabaseUtil.closeConnection(conn);
        }
    }
    
    // Update an existing product
    public void updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock = ?, image_url = ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            stmt.setString(5, product.getImageUrl());
            stmt.setInt(6, product.getId());
            
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) DatabaseUtil.closeConnection(conn);
        }
    }
    
    // Delete a product
    public void deleteProduct(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) DatabaseUtil.closeConnection(conn);
        }
    }
    
    // Get product count for dashboard
    public int getProductCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM products";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
            return 0;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) DatabaseUtil.closeConnection(conn);
        }
    }
    
    // Helper method to extract product from ResultSet
    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setStock(rs.getInt("stock"));
        product.setImageUrl(rs.getString("image_url"));
        return product;
    }
}