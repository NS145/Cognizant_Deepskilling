# Exercise 3: Stored Procedures

## Scenario 1
**The bank needs to process monthly interest for all savings accounts.**  
**Question:** Write a stored procedure `ProcessMonthlyInterest` that calculates and updates the balance of all savings accounts by applying an interest rate of 1% to the current balance.

### Solution
```sql
CREATE OR REPLACE PROCEDURE ProcessMonthlyInterest AS
BEGIN
    UPDATE Accounts
    SET Balance = Balance + (Balance * 0.01),
        LastModified = SYSDATE
    WHERE AccountType = 'Savings';
    
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Monthly interest processed successfully for savings accounts.');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error processing monthly interest: ' || SQLERRM);
END;
/
```

---

## Scenario 2
**The bank wants to implement a bonus scheme for employees based on their performance.**  
**Question:** Write a stored procedure `UpdateEmployeeBonus` that updates the salary of employees in a given department by adding a bonus percentage passed as a parameter.

### Solution
```sql
CREATE OR REPLACE PROCEDURE UpdateEmployeeBonus (
    p_department VARCHAR2,
    p_bonus_percentage NUMBER
) AS
BEGIN
    UPDATE Employees
    SET Salary = Salary + (Salary * (p_bonus_percentage / 100))
    WHERE Department = p_department;
    
    IF SQL%ROWCOUNT = 0 THEN
        DBMS_OUTPUT.PUT_LINE('No employees found in department: ' || p_department);
    ELSE
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('Bonus applied successfully to department: ' || p_department);
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error applying employee bonus: ' || SQLERRM);
END;
/
```

---

## Scenario 3
**Customers should be able to transfer funds between their accounts.**  
**Question:** Write a stored procedure `TransferFunds` that transfers a specified amount from one account to another, checking that the source account has sufficient balance before making the transfer.

### Solution
```sql
CREATE OR REPLACE PROCEDURE TransferFunds (
    p_from_account NUMBER,
    p_to_account NUMBER,
    p_amount NUMBER
) AS
    v_balance NUMBER;
    e_insufficient_funds EXCEPTION;
    e_invalid_account EXCEPTION;
BEGIN
    -- Ensure lock is acquired before operating
    SELECT Balance INTO v_balance FROM Accounts WHERE AccountID = p_from_account FOR UPDATE;
    
    IF v_balance >= p_amount THEN
        UPDATE Accounts SET Balance = Balance - p_amount WHERE AccountID = p_from_account;
        
        UPDATE Accounts SET Balance = Balance + p_amount WHERE AccountID = p_to_account;
        IF SQL%ROWCOUNT = 0 THEN
            RAISE e_invalid_account;
        END IF;
        
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('Funds transferred successfully.');
    ELSE
        RAISE e_insufficient_funds;
    END IF;
EXCEPTION
    WHEN e_insufficient_funds THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: Insufficient funds in the source account.');
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
