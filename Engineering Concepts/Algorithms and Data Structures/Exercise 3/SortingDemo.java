public class SortingDemo {

    // Bubble Sort
    static void bubbleSort(Order[] orders) {

        int n = orders.length;

        for (int i = 0; i < n - 1; i++) {

            for (int j = 0; j < n - i - 1; j++) {

                if (orders[j].totalPrice > orders[j + 1].totalPrice) {

                    Order temp = orders[j];
                    orders[j] = orders[j + 1];
                    orders[j + 1] = temp;
                }
            }
        }
    }

    // Quick Sort
    static void quickSort(Order[] orders, int low, int high) {

        if (low < high) {

            int pivotIndex = partition(orders, low, high);

            quickSort(orders, low, pivotIndex - 1);
            quickSort(orders, pivotIndex + 1, high);
        }
    }

    static int partition(Order[] orders, int low, int high) {

        double pivot = orders[high].totalPrice;

        int i = low - 1;

        for (int j = low; j < high; j++) {

            if (orders[j].totalPrice <= pivot) {

                i++;

                Order temp = orders[i];
                orders[i] = orders[j];
                orders[j] = temp;
            }
        }

        Order temp = orders[i + 1];
        orders[i + 1] = orders[high];
        orders[high] = temp;

        return i + 1;
    }

    static void display(Order[] orders) {

        for (Order order : orders)
            System.out.println(order);
    }

    public static void main(String[] args) {

        Order[] orders = {

                new Order(101, "Alice", 5600),
                new Order(102, "Bob", 2300),
                new Order(103, "Charlie", 8900),
                new Order(104, "David", 4200),
                new Order(105, "Emma", 6700)
        };

        System.out.println("Original Orders:");

        display(orders);

        bubbleSort(orders);

        System.out.println("\nOrders After Bubble Sort:");

        display(orders);

        Order[] orders2 = {

                new Order(101, "Alice", 5600),
                new Order(102, "Bob", 2300),
                new Order(103, "Charlie", 8900),
                new Order(104, "David", 4200),
                new Order(105, "Emma", 6700)
        };

        quickSort(orders2, 0, orders2.length - 1);

        System.out.println("\nOrders After Quick Sort:");

        display(orders2);
    }
}