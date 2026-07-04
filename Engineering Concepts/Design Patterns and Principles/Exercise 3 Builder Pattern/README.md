# Exercise 3 - Builder Pattern

## Objective

Implement the Builder Pattern to create Computer objects with optional configurations.

---

## Theory

Builder Pattern separates object construction from object representation.

Useful when objects contain many optional parameters.

---

## Computer.java

```java
class Computer {

    private String cpu;
    private int ram;
    private int storage;

    private Computer(Builder builder){

        cpu = builder.cpu;
        ram = builder.ram;
        storage = builder.storage;

    }

    static class Builder{

        private String cpu;
        private int ram;
        private int storage;

        Builder setCPU(String cpu){

            this.cpu = cpu;
            return this;

        }

        Builder setRAM(int ram){

            this.ram = ram;
            return this;

        }

        Builder setStorage(int storage){

            this.storage = storage;
            return this;

        }

        Computer build(){

            return new Computer(this);

        }

    }

    void display(){

        System.out.println("CPU : "+cpu);
        System.out.println("RAM : "+ram+" GB");
        System.out.println("Storage : "+storage+" GB");

    }

}
```

---

## BuilderTest.java

```java
public class BuilderTest {

    public static void main(String[] args) {

        Computer computer = new Computer.Builder()

                .setCPU("Intel i7")
                .setRAM(16)
                .setStorage(512)
                .build();

        computer.display();

    }

}
```

---

## Output

```
CPU : Intel i7
RAM : 16 GB
Storage : 512 GB
```

---

## Advantages

- Readable code
- Immutable objects
- Optional parameters supported
- Easier maintenance

---

## Conclusion

Builder Pattern simplifies the construction of complex objects with multiple optional fields.