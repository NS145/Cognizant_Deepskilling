# Spring REST Hands-on 3: Spring Web Architecture

## Objectives
- Demonstrate integration of RESTful Web Service of type `GET` and test the service using postman.
- Understand REST Web Service architecture with Controller, Service, and Dao layers.

## Overview: Display and Edit Employee using REST
The hands-on tasks involve creating REST APIs to supply data to an Angular frontend application. The main goals are to retrieve employee data and department data.

### 1. Static Employee List using XML
In `employee.xml`, construct a static list of employees and departments using Spring beans and inject them into `ArrayList`s.

In your DAO layer (`EmployeeDao`):
```java
@Component
public class EmployeeDao {
    private static ArrayList<Employee> EMPLOYEE_LIST;

    public EmployeeDao() {
        ApplicationContext context = new ClassPathXmlApplicationContext("employee.xml");
        EMPLOYEE_LIST = context.getBean("employeeList", ArrayList.class);
    }

    public ArrayList<Employee> getAllEmployees() {
        return EMPLOYEE_LIST;
    }
}
```

### 2. Create REST service to get all employees
In the Service layer (`EmployeeService`), invoke the DAO:
```java
@Service
public class EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    @Transactional
    public ArrayList<Employee> getAllEmployees() {
        return employeeDao.getAllEmployees();
    }
}
```

In the Controller layer (`EmployeeController`), map the endpoint:
```java
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public ArrayList<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }
}
```

### 3. Create REST Service for Department
Similarly, implement the architecture for Departments:
- `DepartmentDao`: Maintains a static list of departments loaded from XML.
- `DepartmentService`: Retrieves departments from DAO.
- `DepartmentController`: Exposes `@GetMapping("/departments")` returning the department array.
