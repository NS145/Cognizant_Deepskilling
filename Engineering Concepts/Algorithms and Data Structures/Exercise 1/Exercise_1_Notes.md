# Exercise 1 - Inventory Management System

## Objective

Develop an Inventory Management System that efficiently stores and manages products using Java Collections.

---

## Problem Statement

Warehouses store thousands of products. Efficient insertion, deletion, and searching are necessary for smooth inventory management.

---

## Data Structure Used

**HashMap<Integer, Product>**

Why HashMap?

- Constant time insertion
- Constant time deletion
- Constant time searching
- Product ID acts as a unique key

---

## Product Class

```java
class Product {

    int productId;
    String productName;
    int quantity;
    double price;

    Product(int productId, String productName, int quantity, double price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public String toString() {
        return "ID: " + productId +
                ", Name: " + productName +
                ", Quantity: " + quantity +
                ", Price: ₹" + price;
    }
}
```

---

## InventoryManagement.java

```java
import java.util.HashMap;

public class InventoryManagement {

    static HashMap<Integer, Product> inventory = new HashMap<>();

    static void addProduct(Product product) {
        inventory.put(product.productId, product);
        System.out.println("Product Added Successfully.");
    }

    static void updateProduct(int id, int quantity, double price) {

        Product p = inventory.get(id);

        if (p != null) {
            p.quantity = quantity;
            p.price = price;
            System.out.println("Product Updated.");
        } else {
            System.out.println("Product Not Found.");
        }
    }

    static void deleteProduct(int id) {

        if (inventory.remove(id) != null)
            System.out.println("Product Deleted.");
        else
            System.out.println("Product Not Found.");
    }

    static void displayProducts() {

        System.out.println("\nInventory");

        for (Product p : inventory.values())
            System.out.println(p);
    }

    public static void main(String[] args) {

        addProduct(new Product(101, "Laptop", 10, 65000));
        addProduct(new Product(102, "Keyboard", 20, 1500));
        addProduct(new Product(103, "Mouse", 30, 700));

        displayProducts();

        updateProduct(102, 25, 1700);

        deleteProduct(103);

        displayProducts();
    }
}
```

---

## Sample Output

```
Product Added Successfully.
Product Added Successfully.
Product Added Successfully.

Inventory

ID:101 Name:Laptop Quantity:10 Price:65000.0
ID:102 Name:Keyboard Quantity:20 Price:1500.0
ID:103 Name:Mouse Quantity:30 Price:700.0

Product Updated.
Product Deleted.

Inventory

ID:101 Name:Laptop Quantity:10 Price:65000.0
ID:102 Name:Keyboard Quantity:25 Price:1700.0
```

---

## Time Complexity

| Operation | Complexity |
|------------|------------|
| Add | O(1) |
| Search | O(1) |
| Update | O(1) |
| Delete | O(1) |

---

## Advantages

- Fast lookups
- Scalable for large inventories
- Easy implementation
- Efficient memory usage

---

## Conclusion

Using a HashMap significantly improves inventory operations by providing constant-time insertion, deletion, and retrieval, making it suitable for large warehouse management systems.