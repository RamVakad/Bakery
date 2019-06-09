import java.util.List;


//Only 1 batch per shape, either regular or gluten free
//Each order must get at least one preferred shape + type
//As less Gluten Free batches as possible.
public class Bakery {

    private Integer numOfShapes;
    private List<Order> orders;
    private CookieType[] bakeConfiguration;
    private boolean[] configLocks;
    private Integer numOfGlutenFreeBatches;
    private Integer bestedComp = 0;


    public Bakery(Integer numOfShapes) {
        this.numOfShapes = numOfShapes;
        this.bakeConfiguration = new CookieType[numOfShapes];
        this.configLocks = new boolean[numOfShapes];
        this.numOfGlutenFreeBatches = -1;
        System.out.println("New instance of bakery created with " + this.numOfShapes + " different shapes.");
    }


    public void bakeOrders(List<Order> orders) {
        System.out.println("Attempting to bake orders ... ");

        //Check for costumers that are ordering only 1 cookie, and lock that preference.
        for(int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            if (o.getCookies().size() == 1) {
                Cookie c = o.getCookies().get(0);

                if (this.configLocks[c.getShape() - 1]) {
                    System.out.println("Config lock for Shape " + c.getShape() + " exists: " + CookieType.getStringType(this.bakeConfiguration[c.getShape() - 1]));
                    if(this.bakeConfiguration[c.getShape() - 1] != c.getType()) {
                        System.out.println("Config lock conflict with: " + CookieType.getStringType(c.getType()) + ". No solution exists.");
                        System.out.println("Exiting.");
                        return;
                    }
                }

                this.bakeConfiguration[c.getShape() - 1] = c.getType();
                this.configLocks[c.getShape() - 1] = true;
                orders.remove(i);
                i--;
                this.updateBestedComp();
            }
        }

        bruteForce(orders);
    }

    public void updateBestedComp() {
        int bestComp = 0;
        for(int i = 0; i < this.configLocks.length; i++) {
            if (this.configLocks[i] == true) {
                if (this.bakeConfiguration[i] == CookieType.GLUTEN_FREE) {
                    bestComp++;
                }
            }
        }
        this.bestedComp = bestComp;
    }


    //Brute force every single batch combination and keep track of the best.
    public void bruteForce(List<Order> orders) {
        CookieType[] batches = new CookieType[this.numOfShapes];
        System.arraycopy(this.bakeConfiguration, 0, batches, 0, this.numOfShapes);
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
            printConfiguration(config);
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
                    if (this.numOfGlutenFreeBatches.equals(bestedComp)) return true;
                }
            }
            return false;
        }

        if (!configLocks[i]) {
            config[i] = CookieType.REGULAR;
            if (bruteForce(config, i + 1)) return true; //Best config with 0 gluten free has been found so brute forcing can be stopped.
            config[i] = CookieType.GLUTEN_FREE;
            if (bruteForce(config, i + 1)) return true; //Best config with 0 gluten free has been found so brute forcing can be stopped.
        } else {
            if (bruteForce(config, i + 1)) return true;
        }

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
