package webshop.Presentation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import webshop.BAL.BasketService;
import webshop.DAL.Model.ShoppingBasket;
import webshop.DAL.Model.BasketItem;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/basket")
public class BasketServlet extends HttpServlet {
    
    private BasketService basketService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        basketService = new BasketService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            // Get user's basket
            int userId = (int) session.getAttribute("userId");
            ShoppingBasket basket = basketService.getBasketForUser(userId);
            
            // Update basket count in session
            updateBasketItemCount(session, basket);
            
            request.setAttribute("basket", basket);
            
            // Forward to basket page
            request.getRequestDispatcher("/basket.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/basket.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            // User is not logged in, redirect to login page
           response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        String action = request.getParameter("action");
        
        try {
            if ("add".equals(action)) {
                // Add product to basket
                int productId = Integer.parseInt(request.getParameter("productId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                
                basketService.addProductToBasket(userId, productId, quantity);
                
                // Get updated basket and update count in session
                ShoppingBasket basket = basketService.getBasketForUser(userId);
                updateBasketItemCount(session, basket);
                
                // Set success message
                session.setAttribute("successMessage", "Product added to your basket successfully!");
                
                // Redirect back to products page
                response.sendRedirect(request.getContextPath() + "/products");
                
            } else if ("update".equals(action)) {
                // Update item quantity
                int productId = Integer.parseInt(request.getParameter("productId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                
                basketService.updateItemQuantity(userId, productId, quantity);
                
                // Get updated basket and update count in session
                ShoppingBasket basket = basketService.getBasketForUser(userId);
                updateBasketItemCount(session, basket);
                
                // Set success message
                session.setAttribute("successMessage", "Basket updated successfully!");
                
                // Redirect back to basket page
                response.sendRedirect(request.getContextPath() + "/basket");
                
            } else if ("remove".equals(action)) {
                // Remove item from basket
                int productId = Integer.parseInt(request.getParameter("productId"));
                
                basketService.removeItemFromBasket(userId, productId);
                
                // Get updated basket and update count in session
                ShoppingBasket basket = basketService.getBasketForUser(userId);
                updateBasketItemCount(session, basket);
                
                // Set success message
                session.setAttribute("successMessage", "Item removed from basket!");
                
                // Redirect back to basket page
                response.sendRedirect(request.getContextPath() + "/basket");
                
            } else if ("clear".equals(action)) {
                // Clear basket
                basketService.clearBasket(userId);
                
                // Update basket count in session
                session.setAttribute("basketItemCount", 0);
                
                // Set success message
                session.setAttribute("successMessage", "Basket cleared successfully!");
                
                // Redirect back to basket page
                response.sendRedirect(request.getContextPath() + "/basket");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/basket.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Invalid input: " + e.getMessage());
            request.getRequestDispatcher("/basket.jsp").forward(request, response);
        }
    }
    
    // Helper method to update basket item count in session
    private void updateBasketItemCount(HttpSession session, ShoppingBasket basket) {
        int itemCount = 0;
        for (BasketItem item : basket.getItems()) {
            itemCount += item.getQuantity();
        }
        session.setAttribute("basketItemCount", itemCount);
    }
}