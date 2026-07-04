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