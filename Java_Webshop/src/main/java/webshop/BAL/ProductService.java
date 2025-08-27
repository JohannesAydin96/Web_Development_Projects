package webshop.BAL;

import webshop.DAL.ProductDAO;
import webshop.DAL.Model.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
    
    private ProductDAO productDAO;
    
    public ProductService() {
        productDAO = new ProductDAO();
    }
    
    public List<Product> getAllProducts() throws SQLException {
        return productDAO.getAllProducts();
    }
    
    public Product getProductById(int id) throws SQLException {
        return productDAO.getProductById(id);
    }
    
    public void createProduct(Product product) throws SQLException {
        productDAO.createProduct(product);
    }
    
    public void updateProduct(Product product) throws SQLException {
        productDAO.updateProduct(product);
    }
    
    public void deleteProduct(int id) throws SQLException {
        productDAO.deleteProduct(id);
    }
    
    public int getProductCount() throws SQLException {
        return productDAO.getProductCount();
    }
}