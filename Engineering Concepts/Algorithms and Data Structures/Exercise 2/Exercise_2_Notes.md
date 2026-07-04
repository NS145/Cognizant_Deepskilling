# Exercise 2 - E-commerce Platform Search Function

## Objective

Develop a search functionality for an e-commerce platform using **Linear Search** and **Binary Search** algorithms, and compare their performance.

---

## Problem Statement

An e-commerce platform contains thousands of products. Customers search for products by name, so the search algorithm should return results as quickly as possible.

This exercise demonstrates two searching techniques:

- Linear Search
- Binary Search

---

## Theory

### Big O Notation

Big O notation is used to measure the efficiency of an algorithm based on the input size.

It helps determine how execution time increases as the number of elements grows.

---

### Search Scenarios

| Case | Description |
|------|-------------|
| Best Case | Element found at the first position |
| Average Case | Element found somewhere in the middle |
| Worst Case | Element found at the last position or not found |

---

## Product.java

```java
class Product {

    int productId;
    String productName;
    String category;

    Product(int productId, String productName, String category) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
    }

    @Override
    public String toString() {
        return "ID: " + productId +
                ", Name: " + productName +
                ", Category: " + category;
    }
}
```

---

## SearchDemo.java

```java
import java.util.Arrays;
import java.util.Comparator;

public class SearchDemo {

    // Linear Search
    static int linearSearch(Product[] products, String key) {

        for (int i = 0; i < products.length; i++) {

            if (products[i].productName.equalsIgnoreCase(key))
                return i;
        }

        return -1;
    }

    // Binary Search
    static int binarySearch(Product[] products, String key) {

        int low = 0;
        int high = products.length - 1;

        while (low <= high) {

            int mid = (low + high) / 2;

            int compare = products[mid].productName.compareToIgnoreCase(key);

            if (compare == 0)
                return mid;

            if (compare < 0)
                low = mid + 1;
            else
                high = mid - 1;
        }

        return -1;
    }

    public static void main(String[] args) {

        Product[] products = {

                new Product(101, "Laptop", "Electronics"),
                new Product(102, "Keyboard", "Electronics"),
                new Product(103, "Mouse", "Electronics"),
                new Product(104, "Monitor", "Electronics"),
                new Product(105, "Speaker", "Electronics")
        };

        // Linear Search
        int index = linearSearch(products, "Mouse");

        if (index != -1)
            System.out.println("Linear Search Result:");
        else
            System.out.println("Product Not Found");

        if (index != -1)
            System.out.println(products[index]);

        // Sort array before Binary Search
        Arrays.sort(products, Comparator.comparing(p -> p.productName));

        index = binarySearch(products, "Mouse");

        if (index != -1)
            System.out.println("\nBinary Search Result:");
        else
            System.out.println("Product Not Found");

        if (index != -1)
            System.out.println(products[index]);
    }
}
```

---

## Sample Output

```
Linear Search Result:
ID: 103, Name: Mouse, Category: Electronics

Binary Search Result:
ID: 103, Name: Mouse, Category: Electronics
```

---

## Time Complexity Analysis

| Algorithm | Best Case | Average Case | Worst Case |
|------------|-----------|--------------|-------------|
| Linear Search | O(1) | O(n) | O(n) |
| Binary Search | O(1) | O(log n) | O(log n) |

---

## Comparison

| Feature | Linear Search | Binary Search |
|----------|---------------|---------------|
| Data Requirement | Unsorted | Sorted |
| Best Case | O(1) | O(1) |
| Average Case | O(n) | O(log n) |
| Worst Case | O(n) | O(log n) |
| Suitable For | Small datasets | Large datasets |

---

## Advantages of Linear Search

- Easy to implement
- Works with unsorted data
- Suitable for small datasets

---

## Advantages of Binary Search

- Very fast searching
- Efficient for large datasets
- Reduces search space by half in each iteration

---

## Conclusion

Linear Search is suitable for small or unsorted collections because of its simplicity. However, for large datasets where products are maintained in sorted order, Binary Search is significantly more efficient due to its logarithmic time complexity.

Therefore, **Binary Search is the preferred algorithm for large-scale e-commerce platforms.**