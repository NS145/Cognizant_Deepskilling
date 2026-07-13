# Exercise 6: Cursors

## Scenario 1
**Generate monthly statements for all customers.**  
**Question:** Write a PL/SQL block using an explicit cursor `GenerateMonthlyStatements` that retrieves all transactions for the current month and prints a statement for each customer.

### Solution
```sql
DECLARE
    CURSOR GenerateMonthlyStatements IS
        SELECT c.Name, a.AccountID, t.TransactionDate, t.Amount, t.TransactionType
        FROM Customers c
        JOIN Accounts a ON c.CustomerID = a.CustomerID
        JOIN Transactions t ON a.AccountID = t.AccountID
        WHERE EXTRACT(MONTH FROM t.TransactionDate) = EXTRACT(MONTH FROM SYSDATE)
          AND EXTRACT(YEAR FROM t.TransactionDate) = EXTRACT(YEAR FROM SYSDATE)
        ORDER BY c.Name, a.AccountID, t.TransactionDate;
        
    v_record GenerateMonthlyStatements%ROWTYPE;
BEGIN
    OPEN GenerateMonthlyStatements;
    LOOP
        FETCH GenerateMonthlyStatements INTO v_record;
        EXIT WHEN GenerateMonthlyStatements%NOTFOUND;
        
        DBMS_OUTPUT.PUT_LINE(
            'Customer: ' || v_record.Name || 
            ' | Account: ' || v_record.AccountID ||
            ' | Date: ' || TO_CHAR(v_record.TransactionDate, 'YYYY-MM-DD') ||
            ' | Type: ' || v_record.TransactionType ||
            ' | Amount: $' || v_record.Amount
        );
    END LOOP;
    CLOSE GenerateMonthlyStatements;
END;
/
```

---

## Scenario 2
**Apply annual fee to all accounts.**  
**Question:** Write a PL/SQL block using an explicit cursor `ApplyAnnualFee` that deducts an annual maintenance fee from the balance of all accounts.

### Solution
```sql
DECLARE
    v_annual_fee CONSTANT NUMBER := 50; -- Define the annual fee amount
    
    -- Cursor for updating accounts
    CURSOR ApplyAnnualFee IS
        SELECT AccountID, Balance FROM Accounts FOR UPDATE OF Balance, LastModified;
        
    v_account_id Accounts.AccountID%TYPE;
    v_balance Accounts.Balance%TYPE;
BEGIN
    OPEN ApplyAnnualFee;
    LOOP
        FETCH ApplyAnnualFee INTO v_account_id, v_balance;
        EXIT WHEN ApplyAnnualFee%NOTFOUND;
        
        -- Update the specific row retrieved by the cursor
        UPDATE Accounts
        SET Balance = Balance - v_annual_fee,
            LastModified = SYSDATE
        WHERE CURRENT OF ApplyAnnualFee;
    END LOOP;
    CLOSE ApplyAnnualFee;
    
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Annual fee applied successfully to all accounts.');
END;
/
```

---

## Scenario 3
**Update the interest rate for all loans based on a new policy.**  
**Question:** Write a PL/SQL block using an explicit cursor `UpdateLoanInterestRates` that fetches all loans and updates their interest rates based on the new policy.

### Solution
```sql
DECLARE
    -- Cursor to lock the loan rows for update
    CURSOR UpdateLoanInterestRates IS
        SELECT LoanID, LoanAmount FROM Loans FOR UPDATE OF InterestRate;
        
    v_loan_id Loans.LoanID%TYPE;
    v_loan_amount Loans.LoanAmount%TYPE;
    v_new_rate NUMBER;
BEGIN
    OPEN UpdateLoanInterestRates;
    LOOP
        FETCH UpdateLoanInterestRates INTO v_loan_id, v_loan_amount;
        EXIT WHEN UpdateLoanInterestRates%NOTFOUND;
        
        -- Apply the new policy rules to determine the interest rate
        -- Example logic: 4.5% if the loan is > $10,000, else 5.5%
        IF v_loan_amount > 10000 THEN
            v_new_rate := 4.5;
        ELSE
            v_new_rate := 5.5;
        END IF;
        
        -- Perform the update on the current cursor row
        UPDATE Loans
        SET InterestRate = v_new_rate
        WHERE CURRENT OF UpdateLoanInterestRates;
    END LOOP;
    CLOSE UpdateLoanInterestRates;
    
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Loan interest rates updated based on the new policy.');
END;
/
```
