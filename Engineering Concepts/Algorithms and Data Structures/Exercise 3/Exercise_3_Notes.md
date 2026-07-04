# Exercise 3 - Sorting Customer Orders

## Objective

To implement **Bubble Sort** and **Quick Sort** algorithms in Java to sort customer orders based on their total price and compare their performance.

---

## Problem Statement

An e-commerce platform receives multiple customer orders every day. Sorting these orders by their total price helps prioritize high-value orders for processing and analysis.

This exercise demonstrates the implementation and comparison of two sorting algorithms:

- Bubble Sort
- Quick Sort

---

## Theory

### Bubble Sort

Bubble Sort repeatedly compares adjacent elements and swaps them if they are in the wrong order. After each pass, the largest element moves to its correct position.

#### Advantages

- Easy to understand
- Simple to implement

#### Disadvantages

- Slow for large datasets
- Performs many unnecessary comparisons

---

### Quick Sort

Quick Sort is a divide-and-conquer algorithm. It selects a pivot element, partitions the array around the pivot, and recursively sorts the left and right subarrays.

#### Advantages

- Fast and efficient
- Suitable for large datasets
- Widely used in real-world applications

#### Disadvantages

- Worst-case performance occurs when the pivot selection is poor
- Recursive implementation requires additional stack space

---

## Order.java

```java
class Order {

    int orderId;
    String customerName;
    double totalPrice;

    Order(int orderId, String customerName, double totalPrice) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId +
                ", Customer: " + customerName +
                ", Total Price: ₹" + totalPrice;
    }
}
```

---

## SortingDemo.java

```java
public class SortingDemo {

    // Bubble Sort
    static void bubbleSort(Order[] orders) {

        int n = orders.length;

        for (int i = 0; i < n - 1; i++) {

            for (int j = 0; j < n - i - 1; j++) {

                if (orders[j].totalPrice > orders[j + 1].totalPrice) {

                    Order temp = orders[j];
                    orders[j] = orders[j + 1];
                    orders[j + 1] = temp;
                }
            }
        }
    }

    // Quick Sort
    static void quickSort(Order[] orders, int low, int high) {

        if (low < high) {

            int pivotIndex = partition(orders, low, high);

            quickSort(orders, low, pivotIndex - 1);
            quickSort(orders, pivotIndex + 1, high);
        }
    }

    static int partition(Order[] orders, int low, int high) {

        double pivot = orders[high].totalPrice;

        int i = low - 1;

        for (int j = low; j < high; j++) {

            if (orders[j].totalPrice <= pivot) {

                i++;

                Order temp = orders[i];
                orders[i] = orders[j];
                orders[j] = temp;
            }
        }

        Order temp = orders[i + 1];
        orders[i + 1] = orders[high];
        orders[high] = temp;

        return i + 1;
    }

    static void display(Order[] orders) {

        for (Order order : orders)
            System.out.println(order);
    }

    public static void main(String[] args) {

        Order[] orders = {

                new Order(101, "Alice", 5600),
                new Order(102, "Bob", 2300),
                new Order(103, "Charlie", 8900),
                new Order(104, "David", 4200),
                new Order(105, "Emma", 6700)
        };

        System.out.println("Original Orders:");

        display(orders);

        bubbleSort(orders);

        System.out.println("\nOrders After Bubble Sort:");

        display(orders);

        Order[] orders2 = {

                new Order(101, "Alice", 5600),
                new Order(102, "Bob", 2300),
                new Order(103, "Charlie", 8900),
                new Order(104, "David", 4200),
                new Order(105, "Emma", 6700)
        };

        quickSort(orders2, 0, orders2.length - 1);

        System.out.println("\nOrders After Quick Sort:");

        display(orders2);
    }
}
```

---

## Sample Output

```
Original Orders:

Order ID: 101, Customer: Alice, Total Price: ₹5600.0
Order ID: 102, Customer: Bob, Total Price: ₹2300.0
Order ID: 103, Customer: Charlie, Total Price: ₹8900.0
Order ID: 104, Customer: David, Total Price: ₹4200.0
Order ID: 105, Customer: Emma, Total Price: ₹6700.0

Orders After Bubble Sort:

Order ID: 102, Customer: Bob, Total Price: ₹2300.0
Order ID: 104, Customer: David, Total Price: ₹4200.0
Order ID: 101, Customer: Alice, Total Price: ₹5600.0
Order ID: 105, Customer: Emma, Total Price: ₹6700.0
Order ID: 103, Customer: Charlie, Total Price: ₹8900.0

Orders After Quick Sort:

Order ID: 102, Customer: Bob, Total Price: ₹2300.0
Order ID: 104, Customer: David, Total Price: ₹4200.0
Order ID: 101, Customer: Alice, Total Price: ₹5600.0
Order ID: 105, Customer: Emma, Total Price: ₹6700.0
Order ID: 103, Customer: Charlie, Total Price: ₹8900.0
```

---

## Time Complexity Analysis

| Algorithm | Best Case | Average Case | Worst Case |
|------------|-----------|--------------|-------------|
| Bubble Sort | O(n) | O(n²) | O(n²) |
| Quick Sort | O(n log n) | O(n log n) | O(n²) |

---

## Comparison

| Feature | Bubble Sort | Quick Sort |
|----------|-------------|------------|
| Approach | Repeated swapping | Divide and Conquer |
| Average Complexity | O(n²) | O(n log n) |
| Worst Case | O(n²) | O(n²) |
| Space Complexity | O(1) | O(log n) (recursive stack) |
| Suitable For | Small datasets | Large datasets |

---

## Why is Quick Sort Preferred?

- Much faster for large datasets.
- Uses divide-and-conquer to reduce the number of comparisons.
- Average-case complexity is **O(n log n)**.
- Widely used in standard libraries and real-world applications.

---

## Conclusion

Bubble Sort is simple and useful for learning sorting concepts or handling very small datasets. However, Quick Sort is significantly more efficient for practical applications due to its average-case time complexity of **O(n log n)**. Therefore, Quick Sort is the preferred choice for sorting customer orders in large-scale e-commerce platforms.