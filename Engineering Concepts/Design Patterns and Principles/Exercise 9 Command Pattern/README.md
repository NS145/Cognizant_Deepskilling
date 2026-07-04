# Exercise 9 - Command Pattern

## Objective

Implement the Command Pattern to control a light using a remote control.

---

## Theory

The Command Pattern encapsulates a request as an object, allowing parameterization and easy execution of commands.

---

## Command.java

```java
interface Command {
    void execute();
}
```

---

## Light.java

```java
class Light {

    public void turnOn() {
        System.out.println("Light ON");
    }

    public void turnOff() {
        System.out.println("Light OFF");
    }

}
```

---

## LightOnCommand.java

```java
class LightOnCommand implements Command {

    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.turnOn();
    }

}
```

---

## LightOffCommand.java

```java
class LightOffCommand implements Command {

    private Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.turnOff();
    }

}
```

---

## RemoteControl.java

```java
class RemoteControl {

    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressButton() {
        command.execute();
    }

}
```

---

## CommandTest.java

```java
public class CommandTest {

    public static void main(String[] args) {

        Light light = new Light();

        RemoteControl remote = new RemoteControl();

        remote.setCommand(new LightOnCommand(light));
        remote.pressButton();

        remote.setCommand(new LightOffCommand(light));
        remote.pressButton();

    }

}
```

---

## Output

```
Light ON
Light OFF
```

---

## Conclusion

The Command Pattern separates the object invoking an operation from the object performing it.