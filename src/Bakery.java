import java.util.List;

public class Bakery {

    private Integer numOfShapes;
    private List<Order> orders;
    private CookieType[] bakeConfiguration;
    private Integer numOfGlutenFreeBatches;


    public Bakery(Integer numOfShapes) {
        this.numOfShapes = numOfShapes;
        this.bakeConfiguration = new CookieType[numOfShapes];
        this.numOfGlutenFreeBatches = -1;
        System.out.println("New instance of bakery created with " + this.numOfShapes + " different shapes.");
    }

    //Only 1 batch per shape, either regular or gluten free
    //Each order must get at least one preferred shape + type
    //As less Gluten Free batches as possible.
    public void bakeOrders(List<Order> orders) {
        System.out.println("Attempting to bake orders ... ");
        bruteForce(orders);
    }


    //Brute force every single batch combination and keep track of the best.
    public void bruteForce(List<Order> orders) {
        CookieType[] batches = new CookieType[numOfShapes];
        this.orders = orders;
        this.bruteForce(batches, 0);
        if (this.numOfGlutenFreeBatches == -1) {
            System.out.println("No valid bake configuration found to make all customers happy.");
        } else {
            System.out.println("Cheapest bake configuration that will make all customers happy: ");
            printConfiguration(this.bakeConfiguration);
        }

    }

    //Just like generating all possible binary strings with N bits
    //'0' being equivalent to regular and '1' being equivalent to gluten free.
    public boolean bruteForce(CookieType[] config, int i) {
        if (i == numOfShapes) {
            boolean everyoneHappy = true; //Assume that every costumer is happy with the bake config at first.
            for(Order o : this.orders) { //Now test each order.
                boolean happy = false; //Assume that this customer is unhappy to begin with.
                for(Cookie c : o.getCookies()) {
                    Integer shape = c.getShape();
                    if (config[shape - 1] == c.getType()) { //Costumer is happy if one of his preference matches.
                        happy = true;
                        break;
                    }
                }
                if (!happy) { //If one costumer is not happy => Not everyone is happy => Bad configuration.
                    everyoneHappy = false;
                    break;
                }
            }
            if (everyoneHappy) { //Ok everyone is happy, time to check how many gluten free are being used.
                int newGlutenFree = this.getNumOfGlutenFree(config);
                if (this.numOfGlutenFreeBatches == -1 || newGlutenFree < this.numOfGlutenFreeBatches) {
                    System.arraycopy(config, 0, this.bakeConfiguration, 0, this.bakeConfiguration.length);
                    this.numOfGlutenFreeBatches = newGlutenFree;
                    if (this.numOfGlutenFreeBatches == 0) return true;
                }
            }
            return false;
        }

        config[i] = CookieType.REGULAR;
        boolean bested = bruteForce(config, i + 1);
        if (bested) {
            return true; //Best config with 0 gluten free has been found so brute forcing can be stopped.
        }

        config[i] = CookieType.GLUTEN_FREE;
        bruteForce(config, i + 1);

        return false;
    }

    public int getNumOfGlutenFree(CookieType[] bakeConfig) {
        int glutenFree = 0;
        for(int i = 0; i < bakeConfig.length; i++) {
            if (bakeConfig[i] == CookieType.GLUTEN_FREE) glutenFree++;
        }
        return glutenFree;
    }

    public void printConfiguration(CookieType[] config) {
        for(CookieType type : config) {
            System.out.print(CookieType.getStringType(type) + "\t");
        }
        System.out.println();
    }
}
