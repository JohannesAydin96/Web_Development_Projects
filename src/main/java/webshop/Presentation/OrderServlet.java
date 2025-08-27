package webshop.Presentation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import webshop.BAL.BasketService;
import webshop.BAL.OrderService;
import webshop.DAL.Model.Order;
import webshop.DAL.Model.ShoppingBasket;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    private OrderService orderService;
    private BasketService basketService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        orderService = new OrderService();
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
        
        int userId = (int) session.getAttribute("userId");
        
        try {
            // Get all orders for the user
            List<Order> orders = orderService.getOrdersByUserId(userId);
            
            // Format dates for each order
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
            for (Order order : orders) {
                if (order.getOrderDate() != null) {
                    order.setFormattedOrderDate(dateFormat.format(order.getOrderDate()));
                } else {
                    order.setFormattedOrderDate("N/A");
                }
            }
            
            request.setAttribute("orders", orders);
            
            // Forward to orders page
            request.getRequestDispatcher("/my-orders.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/my-orders.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        int userId = (int) session.getAttribute("userId");
        String action = request.getParameter("action");
        
        try {
            if ("place".equals(action)) {
                // Get user's basket
                ShoppingBasket basket = basketService.getBasketForUser(userId);
                
                if (basket.getItems().isEmpty()) {
                    request.setAttribute("errorMessage", "Your basket is empty. Cannot place an order.");
                    request.getRequestDispatcher("/basket.jsp").forward(request, response);
                    return;
                }
                
                // Place the order
                Order order = orderService.placeOrder(basket);
                
                // Clear the basket
                basketService.clearBasket(userId);
                
                // Reset the basket count in session to 0
                session.setAttribute("basketItemCount", 0);
                
                // Set success message
                session.setAttribute("successMessage", "Your order has been placed successfully!");
                
                // Redirect to orders page
                response.sendRedirect(request.getContextPath() + "/orders");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/basket.jsp").forward(request, response);
        }
    }
}