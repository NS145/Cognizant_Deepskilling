# Design Patterns and Principles Hands-on Solutions

## Exercise 1: Implementing the Singleton Pattern

```java
// Logger.java
public class Logger {
    private static Logger instance;

    private Logger() {
        // private constructor
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String message) {
        System.out.println("Log: " + message);
    }
}

// SingletonPatternExample.java
public class SingletonPatternExample {
    public static void main(String[] args) {
        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();

        logger1.log("This is the first log message.");
        System.out.println("Are logger1 and logger2 the same instance? " + (logger1 == logger2));
    }
}
```

## Exercise 2: Implementing the Factory Method Pattern

```java
// Document.java
public interface Document {
    void open();
}

// WordDocument.java
public class WordDocument implements Document {
    public void open() {
        System.out.println("Opening Word Document.");
    }
}

// PdfDocument.java
public class PdfDocument implements Document {
    public void open() {
        System.out.println("Opening PDF Document.");
    }
}

// DocumentFactory.java
public abstract class DocumentFactory {
    public abstract Document createDocument();
}

// WordDocumentFactory.java
public class WordDocumentFactory extends DocumentFactory {
    public Document createDocument() {
        return new WordDocument();
    }
}

// PdfDocumentFactory.java
public class PdfDocumentFactory extends DocumentFactory {
    public Document createDocument() {
        return new PdfDocument();
    }
}

// FactoryMethodPatternExample.java
public class FactoryMethodPatternExample {
    public static void main(String[] args) {
        DocumentFactory wordFactory = new WordDocumentFactory();
        Document wordDoc = wordFactory.createDocument();
        wordDoc.open();

        DocumentFactory pdfFactory = new PdfDocumentFactory();
        Document pdfDoc = pdfFactory.createDocument();
        pdfDoc.open();
    }
}
```

## Exercise 3: Implementing the Builder Pattern

```java
// Computer.java
public class Computer {
    private String CPU;
    private String RAM;
    private String storage;

    private Computer(Builder builder) {
        this.CPU = builder.CPU;
        this.RAM = builder.RAM;
        this.storage = builder.storage;
    }

    public static class Builder {
        private String CPU;
        private String RAM;
        private String storage;

        public Builder setCPU(String CPU) {
            this.CPU = CPU;
            return this;
        }
        public Builder setRAM(String RAM) {
            this.RAM = RAM;
            return this;
        }
        public Builder setStorage(String storage) {
            this.storage = storage;
            return this;
        }

        public Computer build() {
            return new Computer(this);
        }
    }

    @Override
    public String toString() {
        return "Computer [CPU=" + CPU + ", RAM=" + RAM + ", storage=" + storage + "]";
    }
}

// BuilderPatternExample.java
public class BuilderPatternExample {
    public static void main(String[] args) {
        Computer computer = new Computer.Builder()
                .setCPU("Intel i9")
                .setRAM("32GB")
                .setStorage("1TB SSD")
                .build();
        
        System.out.println(computer);
    }
}
```

## Exercise 4: Implementing the Adapter Pattern

```java
// PaymentProcessor.java (Target Interface)
public interface PaymentProcessor {
    void processPayment(double amount);
}

// PayPalGateway.java (Adaptee)
public class PayPalGateway {
    public void makePayment(double amount) {
        System.out.println("Processing $" + amount + " through PayPal.");
    }
}

// PayPalAdapter.java (Adapter)
public class PayPalAdapter implements PaymentProcessor {
    private PayPalGateway payPalGateway;

    public PayPalAdapter(PayPalGateway payPalGateway) {
        this.payPalGateway = payPalGateway;
    }

    @Override
    public void processPayment(double amount) {
        payPalGateway.makePayment(amount);
    }
}

// AdapterPatternExample.java
public class AdapterPatternExample {
    public static void main(String[] args) {
        PayPalGateway payPal = new PayPalGateway();
        PaymentProcessor processor = new PayPalAdapter(payPal);
        processor.processPayment(150.0);
    }
}
```

## Exercise 5: Implementing the Decorator Pattern

```java
// Notifier.java
public interface Notifier {
    void send(String message);
}

// EmailNotifier.java
public class EmailNotifier implements Notifier {
    @Override
    public void send(String message) {
        System.out.println("Sending Email: " + message);
    }
}

// NotifierDecorator.java
public abstract class NotifierDecorator implements Notifier {
    protected Notifier wrapper;

    public NotifierDecorator(Notifier wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public void send(String message) {
        wrapper.send(message);
    }
}

// SMSNotifierDecorator.java
public class SMSNotifierDecorator extends NotifierDecorator {
    public SMSNotifierDecorator(Notifier wrapper) {
        super(wrapper);
    }

    @Override
    public void send(String message) {
        super.send(message);
        System.out.println("Sending SMS: " + message);
    }
}

// DecoratorPatternExample.java
public class DecoratorPatternExample {
    public static void main(String[] args) {
        Notifier notifier = new SMSNotifierDecorator(new EmailNotifier());
        notifier.send("Your OTP is 1234");
    }
}
```

## Exercise 6: Implementing the Proxy Pattern

```java
// Image.java
public interface Image {
    void display();
}

// RealImage.java
public class RealImage implements Image {
    private String filename;

    public RealImage(String filename) {
        this.filename = filename;
        loadFromDisk();
    }

    private void loadFromDisk() {
        System.out.println("Loading " + filename + " from disk.");
    }

    @Override
    public void display() {
        System.out.println("Displaying " + filename);
    }
}

// ProxyImage.java
public class ProxyImage implements Image {
    private RealImage realImage;
    private String filename;

    public ProxyImage(String filename) {
        this.filename = filename;
    }

    @Override
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(filename);
        }
        realImage.display();
    }
}

// ProxyPatternExample.java
public class ProxyPatternExample {
    public static void main(String[] args) {
        Image image = new ProxyImage("test_image.jpg");
        // First time: image will be loaded from disk
        image.display();
        // Second time: image will not be loaded from disk
        image.display();
    }
}
```

## Exercise 7: Implementing the Observer Pattern

```java
import java.util.ArrayList;
import java.util.List;

// Observer.java
public interface Observer {
    void update(double price);
}

// Stock.java
public interface Stock {
    void register(Observer observer);
    void deregister(Observer observer);
    void notifyObservers();
}

// StockMarket.java
public class StockMarket implements Stock {
    private List<Observer> observers = new ArrayList<>();
    private double price;

    public void setPrice(double price) {
        this.price = price;
        notifyObservers();
    }

    @Override
    public void register(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void deregister(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(price);
        }
    }
}

// MobileApp.java
public class MobileApp implements Observer {
    @Override
    public void update(double price) {
        System.out.println("MobileApp notified. New Stock Price: " + price);
    }
}

// WebApp.java
public class WebApp implements Observer {
    @Override
    public void update(double price) {
        System.out.println("WebApp notified. New Stock Price: " + price);
    }
}

// ObserverPatternExample.java
public class ObserverPatternExample {
    public static void main(String[] args) {
        StockMarket stockMarket = new StockMarket();
        Observer mobileApp = new MobileApp();
        Observer webApp = new WebApp();

        stockMarket.register(mobileApp);
        stockMarket.register(webApp);

        stockMarket.setPrice(100.50);
        stockMarket.setPrice(105.75);
    }
}
```

## Exercise 8: Implementing the Strategy Pattern

```java
// PaymentStrategy.java
public interface PaymentStrategy {
    void pay(double amount);
}

// CreditCardPayment.java
public class CreditCardPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using Credit Card.");
    }
}

// PayPalPayment.java
public class PayPalPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using PayPal.");
    }
}

// PaymentContext.java
public class PaymentContext {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void executePayment(double amount) {
        paymentStrategy.pay(amount);
    }
}

// StrategyPatternExample.java
public class StrategyPatternExample {
    public static void main(String[] args) {
        PaymentContext context = new PaymentContext();

        context.setPaymentStrategy(new CreditCardPayment());
        context.executePayment(250.0);

        context.setPaymentStrategy(new PayPalPayment());
        context.executePayment(100.0);
    }
}
```

## Exercise 9: Implementing the Command Pattern

```java
// Command.java
public interface Command {
    void execute();
}

// Light.java (Receiver)
public class Light {
    public void turnOn() {
        System.out.println("Light is ON");
    }
    public void turnOff() {
        System.out.println("Light is OFF");
    }
}

// LightOnCommand.java
public class LightOnCommand implements Command {
    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOn();
    }
}

// LightOffCommand.java
public class LightOffCommand implements Command {
    private Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOff();
    }
}

// RemoteControl.java (Invoker)
public class RemoteControl {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressButton() {
        command.execute();
    }
}

// CommandPatternExample.java
public class CommandPatternExample {
    public static void main(String[] args) {
        Light livingRoomLight = new Light();
        Command lightOn = new LightOnCommand(livingRoomLight);
        Command lightOff = new LightOffCommand(livingRoomLight);

        RemoteControl remote = new RemoteControl();
        remote.setCommand(lightOn);
        remote.pressButton();

        remote.setCommand(lightOff);
        remote.pressButton();
    }
}
```

## Exercise 10: Implementing the MVC Pattern

```java
// Student.java (Model)
public class Student {
    private String id;
    private String name;
    private String grade;

    public Student(String id, String name, String grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}

// StudentView.java (View)
public class StudentView {
    public void displayStudentDetails(String studentName, String studentId, String studentGrade) {
        System.out.println("Student Details:");
        System.out.println("Name: " + studentName);
        System.out.println("ID: " + studentId);
        System.out.println("Grade: " + studentGrade);
    }
}

// StudentController.java (Controller)
public class StudentController {
    private Student model;
    private StudentView view;

    public StudentController(Student model, StudentView view) {
        this.model = model;
        this.view = view;
    }

    public void setStudentName(String name) {
        model.setName(name);
    }

    public void setStudentGrade(String grade) {
        model.setGrade(grade);
    }

    public void updateView() {
        view.displayStudentDetails(model.getName(), model.getId(), model.getGrade());
    }
}

// MVCPatternExample.java
public class MVCPatternExample {
    public static void main(String[] args) {
        Student model = new Student("1", "John Doe", "A");
        StudentView view = new StudentView();
        StudentController controller = new StudentController(model, view);

        controller.updateView();

        controller.setStudentName("Jane Doe");
        controller.setStudentGrade("A+");
        controller.updateView();
    }
}
```

## Exercise 11: Implementing Dependency Injection

```java
// CustomerRepository.java
public interface CustomerRepository {
    String findCustomerById(String id);
}

// CustomerRepositoryImpl.java
public class CustomerRepositoryImpl implements CustomerRepository {
    @Override
    public String findCustomerById(String id) {
        return "Customer Details for ID " + id;
    }
}

// CustomerService.java
public class CustomerService {
    private CustomerRepository customerRepository;

    // Constructor Injection
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void fetchCustomer(String id) {
        String details = customerRepository.findCustomerById(id);
        System.out.println(details);
    }
}

// DependencyInjectionExample.java
public class DependencyInjectionExample {
    public static void main(String[] args) {
        CustomerRepository repository = new CustomerRepositoryImpl();
        CustomerService service = new CustomerService(repository);
        service.fetchCustomer("C100");
    }
}
```
