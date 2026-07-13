# Exercise 7: Packages

## Scenario 1
**Group all customer-related procedures and functions into a package.**  
**Question:** Create a package `CustomerManagement` with procedures for adding a new customer, updating customer details, and a function to get customer balance.

### Solution
```sql
-- 1. Create the Package Specification
CREATE OR REPLACE PACKAGE CustomerManagement AS
    PROCEDURE AddCustomer(p_id NUMBER, p_name VARCHAR2, p_dob DATE, p_balance NUMBER);
    PROCEDURE UpdateCustomer(p_id NUMBER, p_name VARCHAR2);
    FUNCTION GetBalance(p_id NUMBER) RETURN NUMBER;
END CustomerManagement;
/

-- 2. Create the Package Body
CREATE OR REPLACE PACKAGE BODY CustomerManagement AS
    PROCEDURE AddCustomer(p_id NUMBER, p_name VARCHAR2, p_dob DATE, p_balance NUMBER) IS
    BEGIN
        INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
        VALUES (p_id, p_name, p_dob, p_balance, SYSDATE);
        COMMIT;
    END AddCustomer;
    
    PROCEDURE UpdateCustomer(p_id NUMBER, p_name VARCHAR2) IS
    BEGIN
        UPDATE Customers
        SET Name = p_name, LastModified = SYSDATE
        WHERE CustomerID = p_id;
        COMMIT;
    END UpdateCustomer;
    
    FUNCTION GetBalance(p_id NUMBER) RETURN NUMBER IS
        v_balance NUMBER;
    BEGIN
        SELECT Balance INTO v_balance FROM Customers WHERE CustomerID = p_id;
        RETURN v_balance;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RETURN NULL;
    END GetBalance;
END CustomerManagement;
/
```

---

## Scenario 2
**Create a package to manage employee data.**  
**Question:** Write a package `EmployeeManagement` with procedures to hire new employees, update employee details, and a function to calculate annual salary.

### Solution
```sql
-- 1. Create the Package Specification
CREATE OR REPLACE PACKAGE EmployeeManagement AS
    PROCEDURE HireEmployee(p_id NUMBER, p_name VARCHAR2, p_position VARCHAR2, p_salary NUMBER, p_dept VARCHAR2, p_hire_date DATE);
    PROCEDURE UpdateEmployee(p_id NUMBER, p_position VARCHAR2, p_salary NUMBER);
    FUNCTION CalculateAnnualSalary(p_id NUMBER) RETURN NUMBER;
END EmployeeManagement;
/

-- 2. Create the Package Body
CREATE OR REPLACE PACKAGE BODY EmployeeManagement AS
    PROCEDURE HireEmployee(p_id NUMBER, p_name VARCHAR2, p_position VARCHAR2, p_salary NUMBER, p_dept VARCHAR2, p_hire_date DATE) IS
    BEGIN
        INSERT INTO Employees (EmployeeID, Name, Position, Salary, Department, HireDate)
        VALUES (p_id, p_name, p_position, p_salary, p_dept, p_hire_date);
        COMMIT;
    END HireEmployee;
    
    PROCEDURE UpdateEmployee(p_id NUMBER, p_position VARCHAR2, p_salary NUMBER) IS
    BEGIN
        UPDATE Employees
        SET Position = p_position, Salary = p_salary
        WHERE EmployeeID = p_id;
        COMMIT;
    END UpdateEmployee;
    
    FUNCTION CalculateAnnualSalary(p_id NUMBER) RETURN NUMBER IS
        v_monthly_salary NUMBER;
    BEGIN
        SELECT Salary INTO v_monthly_salary FROM Employees WHERE EmployeeID = p_id;
        RETURN v_monthly_salary * 12;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RETURN NULL;
    END CalculateAnnualSalary;
END EmployeeManagement;
/
```

---

## Scenario 3
**Group all account-related operations into a package.**  
**Question:** Create a package `AccountOperations` with procedures for opening a new account, closing an account, and a function to get the total balance of a customer across all accounts.

### Solution
```sql
-- 1. Create the Package Specification
CREATE OR REPLACE PACKAGE AccountOperations AS
    PROCEDURE OpenAccount(p_acc_id NUMBER, p_cust_id NUMBER, p_type VARCHAR2, p_balance NUMBER);
    PROCEDURE CloseAccount(p_acc_id NUMBER);
    FUNCTION GetTotalCustomerBalance(p_cust_id NUMBER) RETURN NUMBER;
END AccountOperations;
/

-- 2. Create the Package Body
CREATE OR REPLACE PACKAGE BODY AccountOperations AS
    PROCEDURE OpenAccount(p_acc_id NUMBER, p_cust_id NUMBER, p_type VARCHAR2, p_balance NUMBER) IS
    BEGIN
        INSERT INTO Accounts (AccountID, CustomerID, AccountType, Balance, LastModified)
        VALUES (p_acc_id, p_cust_id, p_type, p_balance, SYSDATE);
        COMMIT;
    END OpenAccount;
    
    PROCEDURE CloseAccount(p_acc_id NUMBER) IS
    BEGIN
        DELETE FROM Accounts WHERE AccountID = p_acc_id;
        COMMIT;
    END CloseAccount;
    
    FUNCTION GetTotalCustomerBalance(p_cust_id NUMBER) RETURN NUMBER IS
        v_total NUMBER;
    BEGIN
        SELECT SUM(Balance) INTO v_total 
        FROM Accounts 
        WHERE CustomerID = p_cust_id;
        
        -- Handle NULL in case the customer has no accounts
        RETURN NVL(v_total, 0);
    END GetTotalCustomerBalance;
END AccountOperations;
/
```
