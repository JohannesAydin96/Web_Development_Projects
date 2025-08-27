package webshop.Presentation.Admin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import webshop.BAL.ProductService;
import webshop.DAL.Model.Product;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

@WebServlet("/admin/products/save")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      // 1 MB
    maxFileSize = 1024 * 1024 * 10,       // 10 MB
    maxRequestSize = 1024 * 1024 * 50     // 50 MB
)
public class AdminProductSaveServlet extends HttpServlet {
    
    private ProductService productService;
    
    // IMPORTANT: Update these constants to match your environment
    private static final String PROJECT_NAME = "WebShop";
    private static final String WORKSPACE_PATH = "E:" + File.separator + "Software house" + File.separator + "java web shop";
    private static final String IMAGES_RELATIVE_PATH = "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "images";
    
    @Override
    public void init() throws ServletException {
        super.init();
        productService = new ProductService();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
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
            // Get product data from form
            String productIdStr = request.getParameter("id");
            int productId = (productIdStr != null && !productIdStr.isEmpty()) 
                ? Integer.parseInt(productIdStr) : 0;
            
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            BigDecimal price = new BigDecimal(request.getParameter("price"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            
            // Handle image upload
            String imageUrl = null;
            Part filePart = request.getPart("imageFile");
            
            if (filePart != null && filePart.getSize() > 0) {
                // Process uploaded image
                imageUrl = saveUploadedFile(filePart, request, name);
                
                // Debug information
                System.out.println("Image saved: " + imageUrl);
            } else if (productId > 0) {
                // If editing and no new image uploaded, keep the current image
                imageUrl = request.getParameter("currentImageUrl");
                System.out.println("Using existing image: " + imageUrl);
            } else {
                // New product with no image
                session.setAttribute("errorMessage", "Please upload a product image");
                response.sendRedirect(request.getContextPath() + "/admin/products/new");
                return;
            }
            
            // Create or update product
            Product product = new Product();
            product.setId(productId);
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setStock(stock);
            product.setImageUrl(imageUrl);
            
            if (productId > 0) {
                // Update existing product
                productService.updateProduct(product);
                session.setAttribute("successMessage", "Product updated successfully");
            } else {
                // Create new product
                productService.createProduct(product);
                session.setAttribute("successMessage", "Product created successfully");
            }
            
            // Redirect to product list
            response.sendRedirect(request.getContextPath() + "/admin/products");
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error saving product: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/products");
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid number format: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/products");
        }
    }
    
    private String saveUploadedFile(Part filePart, HttpServletRequest request, String productName) throws IOException {
        // Get the original file name from the part
        String originalFileName = getSubmittedFileName(filePart);
        
        // Extract file extension
        String fileExtension = "";
        int i = originalFileName.lastIndexOf('.');
        if (i > 0) {
            fileExtension = originalFileName.substring(i);
        }
        
        // Create a sanitized product name (remove special characters)
        String sanitizedProductName = productName.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
        
        // Create a simple, descriptive file name
        String newFileName = sanitizedProductName + fileExtension;
        
        // Check if a file with this name already exists and add a counter if needed
        int counter = 1;
        File tempFile = new File(WORKSPACE_PATH + File.separator + PROJECT_NAME + File.separator + 
                                IMAGES_RELATIVE_PATH + File.separator + newFileName);
        while (tempFile.exists()) {
            newFileName = sanitizedProductName + "_" + counter + fileExtension;
            tempFile = new File(WORKSPACE_PATH + File.separator + PROJECT_NAME + File.separator + 
                               IMAGES_RELATIVE_PATH + File.separator + newFileName);
            counter++;
        }
        
        // Save to runtime directory first (needed for immediate display)
        try {
            String runtimeImagesPath = request.getServletContext().getRealPath("/images");
            System.out.println("Runtime images directory: " + runtimeImagesPath);
            
            // Create the runtime directory if it doesn't exist
            File runtimeImagesDir = new File(runtimeImagesPath);
            if (!runtimeImagesDir.exists()) {
                boolean created = runtimeImagesDir.mkdirs();
                System.out.println("Created runtime images directory: " + created);
            }
            
            // Save to runtime directory
            Path runtimeFilePath = Paths.get(runtimeImagesPath, newFileName);
            try (InputStream fileContent = filePart.getInputStream()) {
                Files.copy(fileContent, runtimeFilePath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File saved to runtime directory: " + runtimeFilePath);
            }
        } catch (Exception e) {
            // Log the error but continue - runtime directory is less important
            System.out.println("Warning: Could not save to runtime directory: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Now save to the project's src/main/webapp/images directory
        boolean projectSaveSuccess = false;
        try {
            // Construct the path to the project's images directory
            String projectPath = WORKSPACE_PATH + File.separator + PROJECT_NAME;
            String projectImagesPath = projectPath + File.separator + IMAGES_RELATIVE_PATH;
            
            System.out.println("Project images path: " + projectImagesPath);
            
            // Create the directory if it doesn't exist
            File projectImagesDir = new File(projectImagesPath);
            if (!projectImagesDir.exists()) {
                boolean created = projectImagesDir.mkdirs();
                System.out.println("Created project images directory: " + created);
            }
            
            // Save the file to the project directory
            Path projectFilePath = Paths.get(projectImagesPath, newFileName);
            try (InputStream fileContent = filePart.getInputStream()) {
                Files.copy(fileContent, projectFilePath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File saved to project directory: " + projectFilePath);
                projectSaveSuccess = true;
            }
            
            // Verify the file was saved
            File savedFile = new File(projectImagesPath, newFileName);
            if (savedFile.exists()) {
                System.out.println("Verified: File exists at " + savedFile.getAbsolutePath());
                System.out.println("File size: " + savedFile.length() + " bytes");
            } else {
                System.out.println("Warning: File does not exist at " + savedFile.getAbsolutePath());
            }
        } catch (Exception e) {
            System.out.println("Error saving to project directory: " + e.getMessage());
            e.printStackTrace();
        }
        
        if (!projectSaveSuccess) {
            System.out.println("WARNING: Failed to save image to project directory. Only runtime copy available.");
        }
        
        return newFileName;
    }
    
    // Helper method to get the file name from a Part
    private String getSubmittedFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        
        for (String item : items) {
            if (item.trim().startsWith("filename")) {
                return item.substring(item.indexOf("=") + 2, item.length() - 1);
            }
        }
        
        return "";
    }
}
