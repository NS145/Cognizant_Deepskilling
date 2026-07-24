public class EmployeeManagement {

    static Employee[] employees = new Employee[10];
    static int count = 0;

    static void addEmployee(Employee employee) {
        if (count < employees.length) {
            employees[count++] = employee;
            System.out.println("Employee Added.");
        }
    }

    static void searchEmployee(int id) {

        for (int i = 0; i < count; i++) {

            if (employees[i].employeeId == id) {
                System.out.println(employees[i]);
                return;
            }
        }

        System.out.println("Employee Not Found.");
    }

    static void displayEmployees() {

        for (int i = 0; i < count; i++)
            System.out.println(employees[i]);
    }

    static void deleteEmployee(int id) {

        for (int i = 0; i < count; i++) {

            if (employees[i].employeeId == id) {

                for (int j = i; j < count - 1; j++)
                    employees[j] = employees[j + 1];

                employees[count - 1] = null;
                count--;

                System.out.println("Employee Deleted.");
                return;
            }
        }

        System.out.println("Employee Not Found.");
    }

    public static void main(String[] args) {

        addEmployee(new Employee(101, "Alice", "Manager", 60000));
        addEmployee(new Employee(102, "Bob", "Developer", 45000));
        addEmployee(new Employee(103, "Charlie", "Tester", 40000));

        System.out.println("\nEmployee List");

        displayEmployees();

        System.out.println("\nSearching Employee");

        searchEmployee(102);

        deleteEmployee(101);

        System.out.println("\nAfter Deletion");

        displayEmployees();
    }
}