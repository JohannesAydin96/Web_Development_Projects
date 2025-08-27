package webshop.DAL;

import webshop.DAL.Model.BasketItem;
import webshop.DAL.Model.Product;
import webshop.DAL.Model.ShoppingBasket;
import webshop.DAL.Util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BasketDAO {
    
    private ProductDAO productDAO = new ProductDAO();
    
    // Create a new shopping basket
    public ShoppingBasket createBasket(int userId) throws SQLException {
        String sql = "INSERT INTO shopping_baskets (user_id) VALUES (?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setInt(1, userId);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating basket failed, no rows affected.");
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                ShoppingBasket basket = new ShoppingBasket();
                basket.setId(rs.getInt(1));
                basket.setUserId(userId);
                basket.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                return basket;
            } else {
                throw new SQLException("Creating basket failed, no ID obtained.");
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) DatabaseUtil.closeConnection(conn);
        }
    }
    
    // Get basket by user ID
    public ShoppingBasket getBasketByUserId(int userId) throws SQLException {
        // Changed from SQL Server's TOP to MySQL's LIMIT
        String sql = "SELECT * FROM shopping_baskets WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                ShoppingBasket basket = new ShoppingBasket();
                basket.setId(rs.getInt("id"));
                basket.setUserId(rs.getInt("user_id"));
                basket.setCreatedAt(rs.getTimestamp("created_at"));
                
                // Load basket items
                basket.setItems(getBasketItems(basket.getId()));
                
                return basket;
            }
            
            // If no basket exists, create a new one
            return createBasket(userId);
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) DatabaseUtil.closeConnection(conn);
        }
    }
    
    // Get basket by ID
    public ShoppingBasket getBasketById(int basketId) throws SQLException {
        String sql = "SELECT * FROM shopping_baskets WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, basketId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                ShoppingBasket basket = new ShoppingBasket();
                basket.setId(rs.getInt("id"));
                basket.setUserId(rs.getInt("user_id"));
                basket.setCreatedAt(rs.getTimestamp("created_at"));
                
                // Load basket items
                basket.setItems(getBasketItems(basketId));
                
                return basket;
            }
            
            return null;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) DatabaseUtil.closeConnection(conn);
        }
    }
    
    // Get all items in a basket
    private List<BasketItem> getBasketItems(int basketId) throws SQLException {
        String sql = "SELECT bi.id, bi.basket_id, bi.product_id, bi.quantity, " +
                     "p.name, p.description, p.price, p.stock, p.image_url " +
                     "FROM basket_items bi " +
                     "JOIN products p ON bi.product_id = p.id " +
                     "WHERE bi.basket_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, basketId);
            
            rs = stmt.executeQuery();
            
            List<BasketItem> items = new ArrayList<>();
            
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("product_id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setStock(rs.getInt("stock"));
                product.setImageUrl(rs.getString("image_url"));
                
                BasketItem item = new BasketItem();
                item.setId(rs.getInt("id"));
                item.setBasketId(rs.getInt("basket_id"));
                item.setProduct(product);
                item.setQuantity(rs.getInt("quantity"));
                
                items.add(item);
            }
            
            return items;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) DatabaseUtil.closeConnection(conn);
        }
    }
    
    // Add item to basket
    public boolean addItemToBasket(int basketId, int productId, int quantity) throws SQLException {
        String sql = "INSERT INTO basket_items (basket_id, product_id, quantity) VALUES (?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            // First check if the item already exists in the basket
            if (itemExistsInBasket(basketId, productId)) {
                return updateItemQuantity(basketId, productId, quantity, true);
            }
            
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, basketId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) DatabaseUtil.closeConnection(conn);
        }
    }
    
    // Check if item exists in basket
    private boolean itemExistsInBasket(int basketId, int productId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM basket_items WHERE basket_id = ? AND product_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, basketId);
            stmt.setInt(2, productId);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) DatabaseUtil.closeConnection(conn);
        }
    }
    
    // Update item quantity
    public boolean updateItemQuantity(int basketId, int productId, int quantity, boolean increment) throws SQLException {
        String sql;
        
        if (increment) {
            // MySQL syntax for incrementing
            sql = "UPDATE basket_items SET quantity = quantity + ? WHERE basket_id = ? AND product_id = ?";
        } else {
            sql = "UPDATE basket_items SET quantity = ? WHERE basket_id = ? AND product_id = ?";
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, quantity);
            stmt.setInt(2, basketId);
            stmt.setInt(3, productId);
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) DatabaseUtil.closeConnection(conn);
        }
    }
    
    // Remove item from basket
    public boolean removeItemFromBasket(int basketId, int productId) throws SQLException {
        String sql = "DELETE FROM basket_items WHERE basket_id = ? AND product_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, basketId);
            stmt.setInt(2, productId);
            
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) DatabaseUtil.closeConnection(conn);
        }
    }
    
    // Clear basket
    public boolean clearBasket(int basketId) throws SQLException {
        String sql = "DELETE FROM basket_items WHERE basket_id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, basketId);
            
            int affectedRows = stmt.executeUpdate();
            
            return true; // Even if no items were in the basket, we consider it cleared
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) DatabaseUtil.closeConnection(conn);
        }
    }
}