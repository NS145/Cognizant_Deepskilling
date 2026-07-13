# Exercise 2: Error Handling

## Scenario 1
**Handle exceptions during fund transfers between accounts.**  
**Question:** Write a stored procedure `SafeTransferFunds` that transfers funds between two accounts. Ensure that if any error occurs (e.g., insufficient funds), an appropriate error message is logged and the transaction is rolled back.

### Solution
```sql
CREATE OR REPLACE PROCEDURE SafeTransferFunds (
    p_from_account NUMBER,
    p_to_account NUMBER,
    p_amount NUMBER
) AS
    v_balance NUMBER;
    e_insufficient_funds EXCEPTION;
    e_invalid_account EXCEPTION;
BEGIN
    -- Check balance with a row lock to prevent race conditions
    SELECT Balance INTO v_balance FROM Accounts WHERE AccountID = p_from_account FOR UPDATE;
    
    IF v_balance < p_amount THEN
        RAISE e_insufficient_funds;
    END IF;
    
    -- Perform transfer
    UPDATE Accounts SET Balance = Balance - p_amount WHERE AccountID = p_from_account;
    
    UPDATE Accounts SET Balance = Balance + p_amount WHERE AccountID = p_to_account;
    IF SQL%ROWCOUNT = 0 THEN
        RAISE e_invalid_account;
    END IF;
    
    -- We can imagine a TRANSACTION_SEQ exists for TransactionID
    -- INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
    -- VALUES (TRANSACTION_SEQ.NEXTVAL, p_from_account, SYSDATE, p_amount, 'Withdrawal');
    -- INSERT INTO Transactions (TransactionID, AccountID, TransactionDate, Amount, TransactionType)
    -- VALUES (TRANSACTION_SEQ.NEXTVAL, p_to_account, SYSDATE, p_amount, 'Deposit');
    
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Transfer successful.');
EXCEPTION
    WHEN e_insufficient_funds THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: Insufficient funds in account ' || p_from_account);
    WHEN e_invalid_account THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: Destination account ' || p_to_account || ' does not exist.');
    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: Source account ' || p_from_account || ' does not exist.');
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('An unexpected error occurred: ' || SQLERRM);
END;
/
```

---

## Scenario 2
**Manage errors when updating employee salaries.**  
**Question:** Write a stored procedure `UpdateSalary` that increases the salary of an employee by a given percentage. If the employee ID does not exist, handle the exception and log an error message.

### Solution
```sql
CREATE OR REPLACE PROCEDURE UpdateSalary (
    p_employee_id NUMBER,
    p_percentage NUMBER
) AS
BEGIN
    UPDATE Employees
    SET Salary = Salary + (Salary * (p_percentage / 100))
    WHERE EmployeeID = p_employee_id;
    
    IF SQL%ROWCOUNT = 0 THEN
        -- Raising a custom exception or standard one when row count is 0
        RAISE NO_DATA_FOUND;
    END IF;
    
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Salary updated successfully.');
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('Error: Employee ID ' || p_employee_id || ' does not exist.');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('An unexpected error occurred: ' || SQLERRM);
END;
/
```

---

## Scenario 3
**Ensure data integrity when adding a new customer.**  
**Question:** Write a stored procedure `AddNewCustomer` that inserts a new customer into the Customers table. If a customer with the same ID already exists, handle the exception by logging an error and preventing the insertion.

### Solution
```sql
CREATE OR REPLACE PROCEDURE AddNewCustomer (
    p_customer_id NUMBER,
    p_name VARCHAR2,
    p_dob DATE,
    p_balance NUMBER
) AS
BEGIN
    INSERT INTO Customers (CustomerID, Name, DOB, Balance, LastModified)
    VALUES (p_customer_id, p_name, p_dob, p_balance, SYSDATE);
    
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('New customer added successfully.');
EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
        DBMS_OUTPUT.PUT_LINE('Error: Customer with ID ' || p_customer_id || ' already exists.');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('An unexpected error occurred: ' || SQLERRM);
END;
/
```
