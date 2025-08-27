package webshop.DAL.Model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ShoppingBasket {
    private int id;
    private int userId;
    private Timestamp createdAt;
    private List<BasketItem> items;

    // Default constructor
    public ShoppingBasket() {
        this.items = new ArrayList<>();
    }

    // Constructor with fields
    public ShoppingBasket(int id, int userId, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.items = new ArrayList<>();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public List<BasketItem> getItems() {
        return items;
    }

    public void setItems(List<BasketItem> items) {
        this.items = items;
    }

    // Add an item to the basket
    public void addItem(BasketItem item) {
        // Check if the product is already in the basket
        for (BasketItem existingItem : items) {
            if (existingItem.getProduct().getId() == item.getProduct().getId()) {
                // Increase quantity instead of adding a new item
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        // If not found, add as a new item
        items.add(item);
    }

    // Remove an item from the basket
    public void removeItem(int productId) {
        items.removeIf(item -> item.getProduct().getId() == productId);
    }

    // Update item quantity
    public void updateItemQuantity(int productId, int quantity) {
        for (BasketItem item : items) {
            if (item.getProduct().getId() == productId) {
                item.setQuantity(quantity);
                return;
            }
        }
    }

    // Calculate total price
    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(item -> item.getProduct().getPrice().doubleValue() * item.getQuantity())
                .sum();
    }

    @Override
    public String toString() {
        return "ShoppingBasket{" +
                "id=" + id +
                ", userId=" + userId +
                ", createdAt=" + createdAt +
                ", items=" + items +
                '}';
    }
}