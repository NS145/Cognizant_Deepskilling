# Exercise 4 - Employee Management System

## Objective

To implement an Employee Management System using arrays in Java and perform operations such as adding, searching, traversing, and deleting employee records.

---

## Problem Statement

An organization needs a simple employee management system to maintain employee records. The system should allow users to add, search, display, and delete employee information efficiently.

---

## Theory

### Array Representation

An array is a collection of elements of the same data type stored in **contiguous memory locations**.

Each element can be accessed directly using its index.

### Advantages

- Fast random access (O(1))
- Easy to implement
- Efficient memory usage

### Limitations

- Fixed size
- Insertion and deletion require shifting elements
- Cannot grow dynamically

---

## Employee.java

```java
class Employee {

    int employeeId;
    String name;
    String position;
    double salary;

    Employee(int employeeId, String name, String position, double salary) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "ID: " + employeeId +
                ", Name: " + name +
                ", Position: " + position +
                ", Salary: ₹" + salary;
    }
}
```

---

## EmployeeManagement.java

```java
public class EmployeeManagement {

    static Employee[] employees = new Employee[10];
    static int count = 0;

    static void addEmployee(Employee employee) {
        if (count < employees.length) {
            employees[count++] = employee;
            System.out.println("Employee Added.");
        }
    }

    static void searchEmployee(int id) {

        for (int i = 0; i < count; i++) {

            if (employees[i].employeeId == id) {
                System.out.println(employees[i]);
                return;
            }
        }

        System.out.println("Employee Not Found.");
    }

    static void displayEmployees() {

        for (int i = 0; i < count; i++)
            System.out.println(employees[i]);
    }

    static void deleteEmployee(int id) {

        for (int i = 0; i < count; i++) {

            if (employees[i].employeeId == id) {

                for (int j = i; j < count - 1; j++)
                    employees[j] = employees[j + 1];

                employees[count - 1] = null;
                count--;

                System.out.println("Employee Deleted.");
                return;
            }
        }

        System.out.println("Employee Not Found.");
    }

    public static void main(String[] args) {

        addEmployee(new Employee(101, "Alice", "Manager", 60000));
        addEmployee(new Employee(102, "Bob", "Developer", 45000));
        addEmployee(new Employee(103, "Charlie", "Tester", 40000));

        System.out.println("\nEmployee List");

        displayEmployees();

        System.out.println("\nSearching Employee");

        searchEmployee(102);

        deleteEmployee(101);

        System.out.println("\nAfter Deletion");

        displayEmployees();
    }
}
```

---

## Sample Output

```
Employee Added.
Employee Added.
Employee Added.

Employee List

ID: 101, Name: Alice, Position: Manager, Salary: ₹60000.0
ID: 102, Name: Bob, Position: Developer, Salary: ₹45000.0
ID: 103, Name: Charlie, Position: Tester, Salary: ₹40000.0

Searching Employee

ID: 102, Name: Bob, Position: Developer, Salary: ₹45000.0

Employee Deleted.

After Deletion

ID: 102, Name: Bob, Position: Developer, Salary: ₹45000.0
ID: 103, Name: Charlie, Position: Tester, Salary: ₹40000.0
```

---

## Time Complexity

| Operation | Complexity |
|-----------|------------|
| Add | O(1) |
| Search | O(n) |
| Traverse | O(n) |
| Delete | O(n) |

---

## When to Use Arrays

- Fixed-size collections
- Fast random access
- Small datasets

---

## Conclusion

Arrays provide efficient random access but have limitations such as fixed size and expensive insertion/deletion operations. They are suitable for applications where the number of records is known in advance.