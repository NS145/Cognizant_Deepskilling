# Exercise 7 - Observer Pattern

## Objective

Implement the Observer Pattern to notify multiple clients when stock prices change.

---

## Theory

Observer Pattern establishes a one-to-many dependency between objects.

When the subject changes, all observers are notified automatically.

---

## Observer.java

```java
interface Observer {

    void update(double price);

}
```

---

## Stock.java

```java
interface Stock {

    void register(Observer observer);

    void remove(Observer observer);

    void notifyObservers();

}
```

---

## StockMarket.java

```java
import java.util.*;

class StockMarket implements Stock {

    private List<Observer> observers = new ArrayList<>();

    private double price;

    public void setPrice(double price){

        this.price = price;
        notifyObservers();

    }

    public void register(Observer observer){

        observers.add(observer);

    }

    public void remove(Observer observer){

        observers.remove(observer);

    }

    public void notifyObservers(){

        for(Observer observer : observers)
            observer.update(price);

    }

}
```

---

## MobileApp.java

```java
class MobileApp implements Observer {

    public void update(double price){

        System.out.println("Mobile App Updated : ₹"+price);

    }

}
```

---

## WebApp.java

```java
class WebApp implements Observer {

    public void update(double price){

        System.out.println("Web App Updated : ₹"+price);

    }

}
```

---

## ObserverTest.java

```java
public class ObserverTest {

    public static void main(String[] args) {

        StockMarket market = new StockMarket();

        market.register(new MobileApp());
        market.register(new WebApp());

        market.setPrice(2500);

    }

}
```

---

## Output

```
Mobile App Updated : ₹2500.0
Web App Updated : ₹2500.0
```

---

## Conclusion

Observer Pattern allows automatic notification of multiple dependent objects whenever the subject changes.