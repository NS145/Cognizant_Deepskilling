# Spring Data JPA with Hibernate Hands-On Solutions

This document consolidates solutions and implementations for the Spring Data JPA and Hibernate hands-on exercises, focusing on the Employee Management System and Payroll schemas.

## 1. Project Configuration & Setup
**`application.properties` (H2 & MySQL variants)**
```properties
# Spring Framework and application log
logging.level.org.springframework=info
logging.level.com.cognizant=debug

# Hibernate logs for displaying executed SQL, input and output
logging.level.org.hibernate.SQL=trace
logging.level.org.hibernate.type.descriptor.sql=trace

# MySQL Database configuration
# spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
# spring.datasource.url=jdbc:mysql://localhost:3306/ormlearn
# spring.datasource.username=root
# spring.datasource.password=root
# spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect

# H2 Database configuration (Employee Management System)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true

# Hibernate configuration
spring.jpa.hibernate.ddl-auto=update
```

## 2. Entities & Relationship Mappings (One-to-Many, Many-to-Many)

**`Department.java`**
```java
package com.cognizant.ormlearn.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="dp_id")
    private int id;

    @Column(name="dp_name")
    private String name;

    // OneToMany mapping. EAGER fetch brings employees when department is fetched.
    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER)
    private Set<Employee> employeeList;

    // Getters, Setters, toString
}
```

**`Skill.java`**
```java
package com.cognizant.ormlearn.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sk_id")
    private int id;

    @Column(name="sk_name")
    private String name;

    @ManyToMany(mappedBy = "skillList")
    private Set<Employee> employeeList;
    
    // Getters, Setters, toString
}
```

**`Employee.java`**
```java
package com.cognizant.ormlearn.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="em_id")
    private int id;

    @Column(name="em_name")
    private String name;

    @Column(name="em_salary")
    private double salary;

    @Column(name="em_permanent")
    private boolean permanent;

    @Column(name="em_date_of_birth")
    private Date dateOfBirth;

    // ManyToOne Mapping with Department
    @ManyToOne
    @JoinColumn(name = "em_dp_id")
    private Department department;

    // ManyToMany Mapping with Skill
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "employee_skill",
        joinColumns = @JoinColumn(name = "es_em_id"), 
        inverseJoinColumns = @JoinColumn(name = "es_sk_id"))
    private Set<Skill> skillList;

    // Getters, Setters, toString
}
```

## 3. Repositories, HQL & Native Queries

**`EmployeeRepository.java`**
```java
package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // 1. HQL Query with Joins and Fetch (Optimized)
    @Query(value="SELECT e FROM Employee e left join fetch e.department d left join fetch e.skillList WHERE e.permanent = true")
    List<Employee> getAllPermanentEmployees();

    // 2. Aggregate Function in HQL
    @Query(value="SELECT AVG(e.salary) FROM Employee e WHERE e.department.id = :id")
    double getAverageSalary(@Param("id") int id);

    // 3. Native Query
    @Query(value="SELECT * FROM employee", nativeQuery = true)
    List<Employee> getAllEmployeesNative();
}
```

## 4. Service Layer Implementations

**`EmployeeService.java`**
```java
package com.cognizant.ormlearn.service;

import com.cognizant.ormlearn.model.Employee;
import com.cognizant.ormlearn.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public Employee get(int id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Employee employee) {
        employeeRepository.save(employee);
    }
    
    @Transactional(readOnly = true)
    public List<Employee> getAllPermanentEmployees() {
        return employeeRepository.getAllPermanentEmployees();
    }
}
```

## 5. Main Application Testing

**`OrmLearnApplication.java`**
```java
package com.cognizant.ormlearn;

import com.cognizant.ormlearn.model.Employee;
import com.cognizant.ormlearn.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class OrmLearnApplication {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OrmLearnApplication.class);
    private static EmployeeService employeeService;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(OrmLearnApplication.class, args);
        employeeService = context.getBean(EmployeeService.class);
        
        testGetEmployee();
        testGetAllPermanentEmployees();
    }

    private static void testGetEmployee() {
        LOGGER.info("Start testGetEmployee");
        Employee employee = employeeService.get(1);
        if(employee != null) {
            LOGGER.debug("Employee: {}", employee);
            LOGGER.debug("Department: {}", employee.getDepartment());
            LOGGER.debug("Skills: {}", employee.getSkillList());
        }
        LOGGER.info("End testGetEmployee");
    }

    private static void testGetAllPermanentEmployees() {
        LOGGER.info("Start testGetAllPermanentEmployees");
        List<Employee> employees = employeeService.getAllPermanentEmployees();
        LOGGER.debug("Permanent Employees: {}", employees);
        employees.forEach(e -> LOGGER.debug("Skills: {}", e.getSkillList()));
        LOGGER.info("End testGetAllPermanentEmployees");
    }
}
```

## 6. Advanced Exercises (Pagination & Auditing)

**Pagination & Sorting in Repository:**
```java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// Inside EmployeeRepository
Page<Employee> findAll(Pageable pageable);
```
**Usage in Controller/Service:**
```java
// Fetching page 0, size 10, sorted by salary descending
Pageable pageable = PageRequest.of(0, 10, Sort.by("salary").descending());
Page<Employee> page = employeeRepository.findAll(pageable);
```

**Entity Auditing Setup:**
1. Add `@EnableJpaAuditing` to `OrmLearnApplication`.
2. Add `@EntityListeners(AuditingEntityListener.class)` to `Employee`.
3. Add Audit fields:
```java
@CreatedDate
@Column(updatable = false)
private Date createdAt;

@LastModifiedDate
private Date updatedAt;

@CreatedBy
@Column(updatable = false)
private String createdBy;

@LastModifiedBy
private String modifiedBy;
```
