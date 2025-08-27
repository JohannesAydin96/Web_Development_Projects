package webshop.Presentation.Admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import webshop.BAL.ProductService;
import webshop.DAL.Model.Product;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet({"/admin/products/new", "/admin/products/edit"})
public class AdminProductFormServlet extends HttpServlet {
    
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
            // Check if we're editing an existing product or creating a new one
            String productId = request.getParameter("id");
            
            if (productId != null && !productId.isEmpty()) {
                // Editing existing product
                Product product = productService.getProductById(Integer.parseInt(productId));
                if (product != null) {
                    request.setAttribute("product", product);
                } else {
                    session.setAttribute("errorMessage", "Product not found");
                    response.sendRedirect(request.getContextPath() + "/admin/products");
                    return;
                }
            } else {
                // Creating new product
                Product newProduct = new Product();
                newProduct.setId(0); // Set ID to 0 for new products
                newProduct.setPrice(new BigDecimal("0.00")); // Default price
                newProduct.setStock(0); // Default stock
                request.setAttribute("product", newProduct);
            }
            
            // Forward to product form
            request.getRequestDispatcher("/admin/product-form.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error loading product: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/products");
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid product ID");
            response.sendRedirect(request.getContextPath() + "/admin/products");
        }
    }
}