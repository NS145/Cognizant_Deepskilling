# Exercise 11 - Dependency Injection

## Objective

Implement Constructor Dependency Injection between CustomerService and CustomerRepository.

---

## Theory

Dependency Injection is a design principle where objects receive their dependencies instead of creating them.

### Advantages

- Loose coupling
- Easy testing
- Better maintainability

---

## CustomerRepository.java

```java
interface CustomerRepository {

    String findCustomerById(int id);

}
```

---

## CustomerRepositoryImpl.java

```java
class CustomerRepositoryImpl implements CustomerRepository {

    public String findCustomerById(int id) {

        return "Customer ID : " + id + " Name : Balaji";

    }

}
```

---

## CustomerService.java

```java
class CustomerService {

    private CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {

        this.repository = repository;

    }

    public void displayCustomer(int id) {

        System.out.println(repository.findCustomerById(id));

    }

}
```

---

## DependencyInjectionTest.java

```java
public class DependencyInjectionTest {

    public static void main(String[] args) {

        CustomerRepository repository =
                new CustomerRepositoryImpl();

        CustomerService service =
                new CustomerService(repository);

        service.displayCustomer(101);

    }

}
```

---

## Output

```
Customer ID : 101 Name : Balaji
```

---

## Advantages

- Promotes loose coupling
- Easier unit testing
- Better scalability
- Flexible implementation

---

## Conclusion

Dependency Injection improves software design by reducing dependencies between classes and making the application more modular.