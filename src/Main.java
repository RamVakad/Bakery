import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static Bakery BAKERY;

    //Parses input, creates the order and sends to bakery.
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Filename for input unspecified. Exiting.");
            return;
        }

        FileReader file = null;
        try {
            file = new FileReader(args[0]);
        } catch (FileNotFoundException fnfe) {
            System.out.println(args[0] + " not found. Is it in the same directory? " + fnfe.getMessage());
            return;
        }

        System.out.println("Reading input file ... ");
        BufferedReader br = new BufferedReader(file);
        List<String> lines = br.lines().collect(Collectors.toList());

        if (lines.size() == 0) {
            System.out.println("Input file is empty. Exiting.");
            return;
        } else if (lines.size() == 1) {
            System.out.println("Don't bake anything because there are no orders. Exiting.");
            return;
        }

        //Init bakery with the number of shapes
        Integer numShapes = -1;
        try {
            numShapes = Integer.parseInt(lines.get(0));
        } catch (NumberFormatException nfe) {
            System.out.println("Input file is corrupt: " + nfe.getMessage());
        }
        BAKERY = new Bakery(numShapes);


        //Start creating the list of orders.
        List<Order> orders = new ArrayList<>();

        for(int i = 1; i < lines.size(); i++) { //Orders start at line 2
            String line = lines.get(i).trim().replaceAll(" +", " "); //Cleanup
            if (line.length() == 0) {
                continue; //Empty line.
            }
            String[] lineSplit = line.split(" ");
            Order order = new Order();
            int k = 0;
            while(k < lineSplit.length) {
                Integer shape = Integer.parseInt(lineSplit[k]);
                if (shape < 1 || shape > numShapes) {
                    System.out.println("Invalid Shape: " + shape);
                    return;
                }
                CookieType type = null;
                try {
                    type = CookieType.getCookieType(lineSplit[k+1]);
                } catch (UnsupportedCookieTypeException ucte) {
                    System.out.println("Corrupt input file: " + ucte.getMessage());
                }
                order.getCookies().add(new Cookie(type, shape));
                k += 2;
            }
            if (order.isValid()) {
                System.out.println("Valid Order: " + lines.get(i));
                orders.add(order);
            } else {
                System.out.println("Invalid order: " + order.toString());
                System.out.println("Exiting.");
                return;
            }
        }
        if (orders.size() != 0) {
            BAKERY.bakeOrders(orders);
        } else {
            System.out.println("Don't bake anything because there are no orders. Exiting.");
        }
    }
}
