package webshop.BAL;

import webshop.DAL.OrderDAO;
import webshop.DAL.Model.Order;
import webshop.DAL.Model.OrderItem;
import webshop.DAL.Model.ShoppingBasket;
import webshop.DAL.Model.BasketItem;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class OrderService {
    
    private OrderDAO orderDAO;
    
    public OrderService() {
        this.orderDAO = new OrderDAO();
    }
    
    // Place an order from a shopping basket
    public Order placeOrder(ShoppingBasket basket) throws SQLException {
        if (basket == null || basket.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot place an order with an empty basket");
        }
        
        // Create a new order
        Order order = new Order();
        order.setUserId(basket.getUserId());
        
        // Convert double to BigDecimal
        BigDecimal totalAmount = new BigDecimal(basket.getTotalPrice());
        order.setTotalAmount(totalAmount);
        
        order.setStatus("Pending");
        
        // Add items to the order
        for (BasketItem basketItem : basket.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(basketItem.getProduct());
            orderItem.setQuantity(basketItem.getQuantity());
            orderItem.setPrice(basketItem.getProduct().getPrice());
            
            order.addItem(orderItem);
        }
        
        // Save the order to the database
        return orderDAO.createOrder(order);
    }
    
    // Get order by ID
    public Order getOrderById(int orderId) throws SQLException {
        return orderDAO.getOrderById(orderId);
    }
    
    // Get all orders for a user
    public List<Order> getOrdersByUserId(int userId) throws SQLException {
        return orderDAO.getOrdersByUserId(userId);
    }
}