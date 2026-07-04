# Exercise 2 - Factory Method Pattern

## Objective

Implement the Factory Method Pattern to create different types of documents.

---

## Theory

Factory Method delegates object creation to subclasses.

Instead of using

```java
new PdfDocument();
```

we use

```java
factory.createDocument();
```

---

## Document.java

```java
interface Document{

    void open();

}
```

---

## Concrete Documents

```java
class WordDocument implements Document{

    public void open(){
        System.out.println("Opening Word Document");
    }

}

class PdfDocument implements Document{

    public void open(){
        System.out.println("Opening PDF Document");
    }

}

class ExcelDocument implements Document{

    public void open(){
        System.out.println("Opening Excel Document");
    }

}
```

---

## Factory Classes

```java
abstract class DocumentFactory{

    abstract Document createDocument();

}

class WordFactory extends DocumentFactory{

    Document createDocument(){
        return new WordDocument();
    }

}

class PdfFactory extends DocumentFactory{

    Document createDocument(){
        return new PdfDocument();
    }

}

class ExcelFactory extends DocumentFactory{

    Document createDocument(){
        return new ExcelDocument();
    }

}
```

---

## FactoryTest.java

```java
public class FactoryTest {

    public static void main(String[] args) {

        DocumentFactory factory = new PdfFactory();

        Document doc = factory.createDocument();

        doc.open();

    }

}
```

---

## Output

```
Opening PDF Document
```

---

## Conclusion

Factory Method hides object creation and improves scalability.