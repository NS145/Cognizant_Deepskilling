# Exercise 10 - MVC Pattern

## Objective

Implement the Model-View-Controller (MVC) Pattern for managing student records.

---

## Theory

MVC divides an application into:

- Model
- View
- Controller

This separation improves maintainability and scalability.

---

## Student.java

```java
class Student {

    private String name;
    private int id;
    private String grade;

    public Student(String name, int id, String grade) {
        this.name = name;
        this.id = id;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

}
```

---

## StudentView.java

```java
class StudentView {

    public void displayStudentDetails(Student student) {

        System.out.println("Name : " + student.getName());
        System.out.println("ID : " + student.getId());
        System.out.println("Grade : " + student.getGrade());

    }

}
```

---

## StudentController.java

```java
class StudentController {

    private Student model;
    private StudentView view;

    public StudentController(Student model, StudentView view) {

        this.model = model;
        this.view = view;

    }

    public void updateGrade(String grade) {
        model.setGrade(grade);
    }

    public void updateView() {
        view.displayStudentDetails(model);
    }

}
```

---

## MVCTest.java

```java
public class MVCTest {

    public static void main(String[] args) {

        Student student = new Student("Balaji",101,"A");

        StudentView view = new StudentView();

        StudentController controller =
                new StudentController(student,view);

        controller.updateView();

        controller.updateGrade("A+");

        controller.updateView();

    }

}
```

---

## Output

```
Name : Balaji
ID : 101
Grade : A

Name : Balaji
ID : 101
Grade : A+
```

---

## Conclusion

MVC separates business logic, presentation, and user interaction, improving modularity.