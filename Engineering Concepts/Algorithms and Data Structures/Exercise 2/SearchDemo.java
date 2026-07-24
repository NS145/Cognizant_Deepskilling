import java.util.Arrays;
import java.util.Comparator;

public class SearchDemo {

    // Linear Search
    static int linearSearch(Product[] products, String key) {

        for (int i = 0; i < products.length; i++) {

            if (products[i].productName.equalsIgnoreCase(key))
                return i;
        }

        return -1;
    }

    // Binary Search
    static int binarySearch(Product[] products, String key) {

        int low = 0;
        int high = products.length - 1;

        while (low <= high) {

            int mid = (low + high) / 2;

            int compare = products[mid].productName.compareToIgnoreCase(key);

            if (compare == 0)
                return mid;

            if (compare < 0)
                low = mid + 1;
            else
                high = mid - 1;
        }

        return -1;
    }

    public static void main(String[] args) {

        Product[] products = {

                new Product(101, "Laptop", "Electronics"),
                new Product(102, "Keyboard", "Electronics"),
                new Product(103, "Mouse", "Electronics"),
                new Product(104, "Monitor", "Electronics"),
                new Product(105, "Speaker", "Electronics")
        };

        // Linear Search
        int index = linearSearch(products, "Mouse");

        if (index != -1)
            System.out.println("Linear Search Result:");
        else
            System.out.println("Product Not Found");

        if (index != -1)
            System.out.println(products[index]);

        // Sort array before Binary Search
        Arrays.sort(products, Comparator.comparing(p -> p.productName));

        index = binarySearch(products, "Mouse");

        if (index != -1)
            System.out.println("\nBinary Search Result:");
        else
            System.out.println("Product Not Found");

        if (index != -1)
            System.out.println(products[index]);
    }
}