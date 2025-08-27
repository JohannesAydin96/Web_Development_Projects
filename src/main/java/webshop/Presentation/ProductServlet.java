package webshop.Presentation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import webshop.BAL.ProductService;
import webshop.BAL.BasketService;
import webshop.DAL.Model.Product;
import webshop.DAL.Model.ShoppingBasket;
import webshop.DAL.Model.BasketItem;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    
    private ProductService productService;
    private BasketService basketService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        productService = new ProductService();
        basketService = new BasketService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Remove the authentication check to allow anyone to view products
        // Instead, just check if user is logged in to customize the UI
        HttpSession session = request.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("userId") != null);
        request.setAttribute("isLoggedIn", isLoggedIn);
        
        // If user is logged in, update basket count
        if (isLoggedIn) {
            try {
                int userId = (int) session.getAttribute("userId");
                ShoppingBasket basket = basketService.getBasketForUser(userId);
                
                // Calculate total items in basket
                int itemCount = 0;
                for (BasketItem item : basket.getItems()) {
                    itemCount += item.getQuantity();
                }
                session.setAttribute("basketItemCount", itemCount);
            } catch (SQLException e) {
                e.printStackTrace();
                // Don't stop page loading if this fails
            }
        }
        
        try {
            // Get all products
            List<Product> products = productService.getAllProducts();
            request.setAttribute("products", products);
            
            // Forward to products page
            request.getRequestDispatcher("/products.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/products.jsp").forward(request, response);
        }
    }
}