package webshop.DAL.Model;

public class BasketItem {
    private int id;
    private int basketId;
    private Product product;
    private int quantity;

    // Default constructor
    public BasketItem() {
    }

    // Constructor with fields
    public BasketItem(int id, int basketId, Product product, int quantity) {
        this.id = id;
        this.basketId = basketId;
        this.product = product;
        this.quantity = quantity;
    }

    // Constructor for adding a product to basket
    public BasketItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBasketId() {
        return basketId;
    }

    public void setBasketId(int basketId) {
        this.basketId = basketId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Calculate subtotal for this item
    public double getSubtotal() {
        return product.getPrice().doubleValue() * quantity;
    }

    @Override
    public String toString() {
        return "BasketItem{" +
                "id=" + id +
                ", basketId=" + basketId +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}