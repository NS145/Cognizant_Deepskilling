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