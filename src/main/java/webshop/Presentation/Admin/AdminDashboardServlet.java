package webshop.Presentation.Admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import webshop.BAL.ProductService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    
    private ProductService productService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        productService = new ProductService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is logged in and is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            // User is not logged in, redirect to login
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get the user role and check if it's admin (case-insensitive)
        String userRole = (String) session.getAttribute("userRole");
        if (userRole == null || !userRole.equalsIgnoreCase("ADMIN")) {
            // User is not an admin, redirect to products
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }
        
        try {
            // Get product count for dashboard stats
            int productCount = productService.getProductCount();
            request.setAttribute("productCount", productCount);
            
            // Forward to admin dashboard
            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading dashboard: " + e.getMessage());
            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        }
    }
}