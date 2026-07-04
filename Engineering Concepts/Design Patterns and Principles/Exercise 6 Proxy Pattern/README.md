# Exercise 6 - Proxy Pattern

## Objective

Implement the Proxy Pattern for lazy loading of images.

---

## Theory

Proxy Pattern provides a placeholder object that controls access to another object.

---

## Image.java

```java
interface Image {
    void display();
}
```

---

## RealImage.java

```java
class RealImage implements Image {

    private String fileName;

    public RealImage(String fileName) {

        this.fileName = fileName;
        load();

    }

    private void load() {

        System.out.println("Loading image : " + fileName);

    }

    public void display() {

        System.out.println("Displaying : " + fileName);

    }

}
```

---

## ProxyImage.java

```java
class ProxyImage implements Image {

    private RealImage realImage;
    private String fileName;

    public ProxyImage(String fileName) {

        this.fileName = fileName;

    }

    public void display() {

        if(realImage == null)
            realImage = new RealImage(fileName);

        realImage.display();

    }

}
```

---

## ProxyTest.java

```java
public class ProxyTest {

    public static void main(String[] args) {

        Image image = new ProxyImage("nature.jpg");

        image.display();
        image.display();

    }

}
```

---

## Output

```
Loading image : nature.jpg
Displaying : nature.jpg
Displaying : nature.jpg
```

---

## Conclusion

The Proxy Pattern delays object creation until it is actually required, improving performance.