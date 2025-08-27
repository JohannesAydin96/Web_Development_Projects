package webshop.BAL;

import webshop.DAL.BasketDAO;
import webshop.DAL.ProductDAO;
import webshop.DAL.Model.BasketItem;
import webshop.DAL.Model.Product;
import webshop.DAL.Model.ShoppingBasket;

import java.sql.SQLException;

public class BasketService {
    
    private BasketDAO basketDAO;
    private ProductDAO productDAO;
    
    public BasketService() {
        this.basketDAO = new BasketDAO();
        this.productDAO = new ProductDAO();
    }
    
    // Get or create basket for user
    public ShoppingBasket getBasketForUser(int userId) throws SQLException {
        return basketDAO.getBasketByUserId(userId);
    }
    
    // Add product to basket
    public boolean addProductToBasket(int userId, int productId, int quantity) throws SQLException {
        // Get the user's basket
        ShoppingBasket basket = getBasketForUser(userId);
        
        // Check if product exists and is in stock
        Product product = productDAO.getProductById(productId);
        if (product == null || product.getStock() < quantity) {
            return false;
        }
        
        // Add item to basket
        return basketDAO.addItemToBasket(basket.getId(), productId, quantity);
    }
    
    // Update item quantity in basket
    public boolean updateItemQuantity(int userId, int productId, int quantity) throws SQLException {
        // Get the user's basket
        ShoppingBasket basket = getBasketForUser(userId);
        
        // Check if product exists and is in stock
        Product product = productDAO.getProductById(productId);
        if (product == null || product.getStock() < quantity) {
            return false;
        }
        
        // Update item quantity
        return basketDAO.updateItemQuantity(basket.getId(), productId, quantity, false);
    }
    
    // Remove item from basket
    public boolean removeItemFromBasket(int userId, int productId) throws SQLException {
        // Get the user's basket
        ShoppingBasket basket = getBasketForUser(userId);
        
        // Remove item from basket
        return basketDAO.removeItemFromBasket(basket.getId(), productId);
    }
    
    // Clear basket
    public boolean clearBasket(int userId) throws SQLException {
        // Get the user's basket
        ShoppingBasket basket = getBasketForUser(userId);
        
        // Clear basket
        return basketDAO.clearBasket(basket.getId());
    }
    
    // Get basket by ID
    public ShoppingBasket getBasketById(int basketId) throws SQLException {
        return basketDAO.getBasketById(basketId);
    }
}