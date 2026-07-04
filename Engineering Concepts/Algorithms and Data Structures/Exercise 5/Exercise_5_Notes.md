# Exercise 5 - Task Management System

## Objective

To implement a Task Management System using a Singly Linked List in Java.

---

## Problem Statement

A task management system should support dynamic insertion and deletion of tasks without wasting memory. A Singly Linked List provides an efficient way to handle such operations.

---

## Theory

### Singly Linked List

A Singly Linked List consists of nodes where each node contains:

- Data
- Reference to the next node

Unlike arrays, linked lists do not require contiguous memory.

### Advantages

- Dynamic memory allocation
- Efficient insertion and deletion
- No shifting of elements

### Disadvantages

- Sequential access only
- Extra memory for pointers

---

## Task.java

```java
class Task {

    int taskId;
    String taskName;
    String status;

    Task next;

    Task(int taskId, String taskName, String status) {

        this.taskId = taskId;
        this.taskName = taskName;
        this.status = status;
        next = null;
    }
}
```

---

## SinglyLinkedList.java

```java
public class SinglyLinkedList {

    Task head = null;

    void addTask(int id, String name, String status) {

        Task newTask = new Task(id, name, status);

        if (head == null) {
            head = newTask;
            return;
        }

        Task temp = head;

        while (temp.next != null)
            temp = temp.next;

        temp.next = newTask;
    }

    void displayTasks() {

        Task temp = head;

        while (temp != null) {

            System.out.println(
                    "ID: " + temp.taskId +
                    ", Task: " + temp.taskName +
                    ", Status: " + temp.status);

            temp = temp.next;
        }
    }

    void searchTask(int id) {

        Task temp = head;

        while (temp != null) {

            if (temp.taskId == id) {

                System.out.println("Task Found");
                System.out.println(
                        "ID: " + temp.taskId +
                        ", Task: " + temp.taskName +
                        ", Status: " + temp.status);

                return;
            }

            temp = temp.next;
        }

        System.out.println("Task Not Found.");
    }

    void deleteTask(int id) {

        if (head == null)
            return;

        if (head.taskId == id) {

            head = head.next;
            return;
        }

        Task temp = head;

        while (temp.next != null && temp.next.taskId != id)
            temp = temp.next;

        if (temp.next != null)
            temp.next = temp.next.next;
    }

    public static void main(String[] args) {

        SinglyLinkedList list = new SinglyLinkedList();

        list.addTask(101, "Design Database", "Completed");
        list.addTask(102, "Develop Backend", "In Progress");
        list.addTask(103, "Testing", "Pending");

        System.out.println("Task List");

        list.displayTasks();

        System.out.println("\nSearching Task");

        list.searchTask(102);

        list.deleteTask(101);

        System.out.println("\nAfter Deletion");

        list.displayTasks();
    }
}
```

---

## Sample Output

```
Task List

ID: 101, Task: Design Database, Status: Completed
ID: 102, Task: Develop Backend, Status: In Progress
ID: 103, Task: Testing, Status: Pending

Searching Task

Task Found
ID: 102, Task: Develop Backend, Status: In Progress

After Deletion

ID: 102, Task: Develop Backend, Status: In Progress
ID: 103, Task: Testing, Status: Pending
```

---

## Time Complexity

| Operation | Complexity |
|-----------|------------|
| Add | O(n) |
| Search | O(n) |
| Traverse | O(n) |
| Delete | O(n) |

---

## Linked List vs Array

| Array | Linked List |
|--------|-------------|
| Fixed size | Dynamic size |
| Fast random access | Sequential access |
| Expensive insertion/deletion | Efficient insertion/deletion |
| Contiguous memory | Non-contiguous memory |

---

## Advantages of Linked Lists

- Dynamic memory allocation
- Efficient insertion and deletion
- No shifting of elements
- Better utilization of memory

---

## Conclusion

A Singly Linked List is suitable for applications where the number of elements changes frequently. It provides flexibility and efficient insertion/deletion operations compared to arrays.