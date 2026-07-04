# Exercise 6 - Library Management System

## Objective

To implement **Linear Search** and **Binary Search** algorithms to search books by title in a library management system.

---

## Problem Statement

A library stores hundreds or thousands of books. Users should be able to quickly search for books by title. This exercise compares Linear Search and Binary Search for this purpose.

---

## Theory

### Linear Search

Linear Search checks each book one by one until the required title is found.

#### Advantages

- Simple implementation
- Works on unsorted data

#### Disadvantages

- Slow for large datasets

---

### Binary Search

Binary Search repeatedly divides the sorted array into halves until the desired book is found.

#### Advantages

- Much faster than Linear Search
- Suitable for large sorted datasets

#### Disadvantages

- Requires the array to be sorted

---

## Book.java

```java
class Book {

    int bookId;
    String title;
    String author;

    Book(int bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book ID: " + bookId +
                ", Title: " + title +
                ", Author: " + author;
    }
}
```

---

## LibrarySearch.java

```java
import java.util.Arrays;
import java.util.Comparator;

public class LibrarySearch {

    static int linearSearch(Book[] books, String key) {

        for (int i = 0; i < books.length; i++) {

            if (books[i].title.equalsIgnoreCase(key))
                return i;
        }

        return -1;
    }

    static int binarySearch(Book[] books, String key) {

        int low = 0;
        int high = books.length - 1;

        while (low <= high) {

            int mid = (low + high) / 2;

            int compare = books[mid].title.compareToIgnoreCase(key);

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

        Book[] books = {

                new Book(101, "Algorithms", "CLRS"),
                new Book(102, "Clean Code", "Robert Martin"),
                new Book(103, "Java Programming", "James Gosling"),
                new Book(104, "Operating Systems", "Galvin"),
                new Book(105, "Python Basics", "Guido")
        };

        int index = linearSearch(books, "Java Programming");

        if (index != -1)
            System.out.println("Linear Search:");
        else
            System.out.println("Book Not Found.");

        if (index != -1)
            System.out.println(books[index]);

        Arrays.sort(books, Comparator.comparing(b -> b.title));

        index = binarySearch(books, "Java Programming");

        if (index != -1)
            System.out.println("\nBinary Search:");

        if (index != -1)
            System.out.println(books[index]);
    }
}
```

---

## Sample Output

```
Linear Search:

Book ID: 103, Title: Java Programming, Author: James Gosling

Binary Search:

Book ID: 103, Title: Java Programming, Author: James Gosling
```

---

## Time Complexity

| Algorithm | Best | Average | Worst |
|------------|------|----------|--------|
| Linear Search | O(1) | O(n) | O(n) |
| Binary Search | O(1) | O(log n) | O(log n) |

---

## Comparison

| Linear Search | Binary Search |
|---------------|---------------|
| Works on unsorted data | Requires sorted data |
| Slower | Faster |
| O(n) | O(log n) |

---

## Conclusion

Linear Search is suitable for small libraries or unsorted collections, whereas Binary Search is the preferred choice for large libraries with sorted book records because it significantly reduces search time.