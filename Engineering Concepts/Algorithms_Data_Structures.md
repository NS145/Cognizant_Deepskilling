# Algorithms and Data Structures Hands-on Solutions

## Exercise 1: Inventory Management System

**Understanding the Problem:**
Data structures and algorithms are essential for handling large inventories because they directly impact the time and space required to process data. In a warehouse, searching for an item, updating quantities, or adding new items happens frequently. Inefficient algorithms can cause slow operations and poor system performance. 

Appropriate data structures for this problem include:
1. **HashMap**: Best for constant-time complexity O(1) for adding, deleting, and searching items based on an ID.
2. **ArrayList**: Good for traversal but poor for searching O(n) unless sorted (and even then requires O(log n) using binary search).
3. **TreeMap**: Useful if items frequently need to be sorted by ID or name, though operations take O(log n).

**Implementation:**

```java
import java.util.HashMap;
import java.util.Map;

class Product {
    String productId;
    String productName;
    int quantity;
    double price;

    public Product(String productId, String productName, int quantity, double price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }
}

public class InventoryManagement {
    private Map<String, Product> inventory = new HashMap<>();

    public void addProduct(Product product) {
        inventory.put(product.productId, product);
    }

    public void updateProduct(String productId, int newQuantity, double newPrice) {
        if (inventory.containsKey(productId)) {
            Product product = inventory.get(productId);
            product.quantity = newQuantity;
            product.price = newPrice;
        }
    }

    public void deleteProduct(String productId) {
        inventory.remove(productId);
    }
}
```

**Analysis:**
- **Add Operation:** O(1) average time complexity using HashMap.
- **Update Operation:** O(1) average time complexity.
- **Delete Operation:** O(1) average time complexity.
- **Optimization:** HashMaps are generally optimized but we must ensure a good hash function for `productId` to prevent collisions.

---

## Exercise 2: E-commerce Platform Search Function

**Understanding Asymptotic Notation:**
Big O notation is used to classify algorithms according to how their run time or space requirements grow as the input size grows. It provides an upper bound on the growth rate.
- **Best Case:** The element is found at the first position. (O(1))
- **Average Case:** The element is somewhere in the middle. (O(n) for linear, O(log n) for binary)
- **Worst Case:** The element is at the very end or not present at all. (O(n) for linear, O(log n) for binary)

**Implementation:**

```java
import java.util.Arrays;

class Product implements Comparable<Product> {
    String productId;
    String productName;
    String category;

    public Product(String productId, String productName, String category) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
    }

    @Override
    public int compareTo(Product other) {
        return this.productId.compareTo(other.productId);
    }
}

public class SearchAlgorithm {
    // Linear Search
    public static Product linearSearch(Product[] products, String targetId) {
        for (Product p : products) {
            if (p.productId.equals(targetId)) {
                return p;
            }
        }
        return null;
    }

    // Binary Search
    public static Product binarySearch(Product[] sortedProducts, String targetId) {
        int left = 0;
        int right = sortedProducts.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparison = sortedProducts[mid].productId.compareTo(targetId);

            if (comparison == 0) return sortedProducts[mid];
            if (comparison < 0) left = mid + 1;
            else right = mid - 1;
        }
        return null;
    }
}
```

**Analysis:**
- **Linear Search:** O(n) time complexity.
- **Binary Search:** O(log n) time complexity.
- **Conclusion:** Binary search is much more suitable for large datasets as it drastically reduces the number of comparisons. However, it requires the array to be sorted first, which takes O(n log n) time.

---

## Exercise 3: Sorting Customer Orders

**Understanding Sorting Algorithms:**
- **Bubble Sort:** Repeatedly steps through the list, compares adjacent elements and swaps them if they are in the wrong order. Time complexity is O(n^2).
- **Quick Sort:** A divide-and-conquer algorithm that selects a 'pivot' element and partitions the other elements into two sub-arrays according to whether they are less than or greater than the pivot. Time complexity is O(n log n) on average.

**Implementation:**

```java
class Order {
    String orderId;
    String customerName;
    double totalPrice;

    public Order(String orderId, String customerName, double totalPrice) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.totalPrice = totalPrice;
    }
}

public class OrderSorter {
    // Bubble Sort
    public static void bubbleSort(Order[] orders) {
        int n = orders.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (orders[j].totalPrice < orders[j + 1].totalPrice) { // Descending
                    Order temp = orders[j];
                    orders[j] = orders[j + 1];
                    orders[j + 1] = temp;
                }
            }
        }
    }

    // Quick Sort
    public static void quickSort(Order[] orders, int low, int high) {
        if (low < high) {
            int pi = partition(orders, low, high);
            quickSort(orders, low, pi - 1);
            quickSort(orders, pi + 1, high);
        }
    }

    private static int partition(Order[] orders, int low, int high) {
        double pivot = orders[high].totalPrice;
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (orders[j].totalPrice >= pivot) { // Descending
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
}
```

**Analysis:**
- **Bubble Sort:** O(n^2) - Poor performance on large datasets.
- **Quick Sort:** O(n log n) on average - Much faster and scalable.
- **Conclusion:** Quick Sort is preferred because it handles large arrays significantly faster than Bubble Sort due to its divide-and-conquer strategy.

---

## Exercise 4: Employee Management System

**Understanding Array Representation:**
Arrays are represented as contiguous blocks of memory. This allows O(1) access time via an index because the memory address can be calculated directly.

**Implementation:**

```java
class Employee {
    String employeeId;
    String name;
    String position;
    double salary;

    public Employee(String employeeId, String name, String position, double salary) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
        this.salary = salary;
    }
}

public class EmployeeManager {
    private Employee[] employees;
    private int size;

    public EmployeeManager(int capacity) {
        employees = new Employee[capacity];
        size = 0;
    }

    public void addEmployee(Employee emp) {
        if (size < employees.length) {
            employees[size++] = emp;
        }
    }

    public Employee searchEmployee(String employeeId) {
        for (int i = 0; i < size; i++) {
            if (employees[i].employeeId.equals(employeeId)) {
                return employees[i];
            }
        }
        return null;
    }

    public void traverseEmployees() {
        for (int i = 0; i < size; i++) {
            System.out.println(employees[i].name);
        }
    }

    public void deleteEmployee(String employeeId) {
        for (int i = 0; i < size; i++) {
            if (employees[i].employeeId.equals(employeeId)) {
                // Shift elements to the left
                for (int j = i; j < size - 1; j++) {
                    employees[j] = employees[j + 1];
                }
                employees[--size] = null;
                return;
            }
        }
    }
}
```

**Analysis:**
- **Add:** O(1) at the end, assuming array is not full.
- **Search:** O(n).
- **Traverse:** O(n).
- **Delete:** O(n) because subsequent elements must be shifted.
- **Limitations:** Arrays have a fixed size. Adding elements beyond capacity requires creating a new array. Deleting elements is expensive due to shifting.

---

## Exercise 5: Task Management System

**Understanding Linked Lists:**
- **Singly Linked List:** Each node contains data and a reference to the next node.
- **Doubly Linked List:** Each node contains data, a reference to the next node, and a reference to the previous node.

**Implementation:**

```java
class Task {
    String taskId;
    String taskName;
    String status;

    public Task(String taskId, String taskName, String status) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.status = status;
    }
}

class Node {
    Task task;
    Node next;

    public Node(Task task) {
        this.task = task;
        this.next = null;
    }
}

public class TaskLinkedList {
    private Node head;

    public void addTask(Task task) {
        Node newNode = new Node(task);
        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
    }

    public Task searchTask(String taskId) {
        Node temp = head;
        while (temp != null) {
            if (temp.task.taskId.equals(taskId)) return temp.task;
            temp = temp.next;
        }
        return null;
    }

    public void traverseTasks() {
        Node temp = head;
        while (temp != null) {
            System.out.println(temp.task.taskName);
            temp = temp.next;
        }
    }

    public void deleteTask(String taskId) {
        if (head == null) return;
        if (head.task.taskId.equals(taskId)) {
            head = head.next;
            return;
        }

        Node temp = head;
        while (temp.next != null && !temp.next.task.taskId.equals(taskId)) {
            temp = temp.next;
        }

        if (temp.next != null) {
            temp.next = temp.next.next;
        }
    }
}
```

**Analysis:**
- **Add:** O(n) to append at the end, O(1) if adding at the head.
- **Search:** O(n).
- **Traverse:** O(n).
- **Delete:** O(n) to find the node, but O(1) to actually unlink it.
- **Advantage:** Linked lists grow dynamically, meaning you don't need to resize them like arrays. 

---

## Exercise 6: Library Management System

**Understanding Search Algorithms:**
- **Linear Search:** Checks each element one by one.
- **Binary Search:** Divides the sorted collection in half until the target is found.

**Implementation:**

```java
class Book {
    String bookId;
    String title;
    String author;

    public Book(String bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
    }
}

public class LibrarySearch {
    // Linear Search
    public static Book searchByTitleLinear(Book[] books, String title) {
        for (Book book : books) {
            if (book.title.equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    // Binary Search
    public static Book searchByTitleBinary(Book[] books, String title) {
        int left = 0, right = books.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comparison = books[mid].title.compareToIgnoreCase(title);

            if (comparison == 0) return books[mid];
            if (comparison < 0) left = mid + 1;
            else right = mid - 1;
        }
        return null;
    }
}
```

**Analysis:**
- **Linear:** O(n) time. Useful for small unsorted arrays.
- **Binary:** O(log n) time. Mandatory for large sorted arrays.

---

## Exercise 7: Financial Forecasting

**Understanding Recursive Algorithms:**
Recursion simplifies certain problems by breaking them down into smaller sub-problems. It requires a base case to stop execution.

**Implementation:**

```java
public class FinancialForecast {
    
    // Recursive method to predict future value
    public static double predictFutureValue(double currentValue, double growthRate, int years) {
        // Base Case
        if (years <= 0) {
            return currentValue;
        }
        // Recursive Call
        return predictFutureValue(currentValue * (1 + growthRate), growthRate, years - 1);
    }

    public static void main(String[] args) {
        double futureValue = predictFutureValue(1000.0, 0.05, 10);
        System.out.println("Predicted Future Value: " + futureValue);
    }
}
```

**Analysis:**
- **Time Complexity:** O(n) where n is the number of years.
- **Optimization:** To avoid excessive computation in complex scenarios (like Fibonacci), recursion can be optimized with Memoization (caching past results) to bring complexity down to O(n). In this specific linear recursion, iterative approaches (a simple loop or mathematical formula `value * (1 + rate)^n`) are usually better to avoid stack overflow errors.
