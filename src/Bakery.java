import java.util.List;

public class Bakery {

    private Integer numOfShapes; //Number of different shapes
    private List<Order> orders; //The list of orders from customers, each order has an array of cookies they like.
    private CookieType[] bakeConfiguration; //The best possible baking configuration.
    private Integer numOfGlutenFreeBatches; //The number of gluten free batches in the bake configuration.

    private boolean[] configLocks;
    //Used to lock baking configurations for specific shapes
    //Based on customers with only one preference.
    //configLocks[4] = true for the initial example because of the third customer
    //It will prevent iterations on that configuration, therefore reducing the runtime
    //of the brute forcer substantially.

    private Integer bestedComp = 0;
    //The minimum number of gluten free batches that the bake configuration will need.
    //bestedComp = 1 for the initial example because of the fifth costumer.
    //If the brute forcer encounters a baking configuration that matches bestedComp, it will
    //immediately stop because that is the cheapest configuration.


    public Bakery(Integer numOfShapes) {
        this.numOfShapes = numOfShapes;
        this.bakeConfiguration = new CookieType[numOfShapes];
        this.configLocks = new boolean[numOfShapes];
        this.numOfGlutenFreeBatches = -1;
        System.out.println("New instance of bakery created with " + this.numOfShapes + " different shapes.");
    }


    //Only 1 batch per shape, either regular or gluten free
    //Each order must get at least one preferred shape + type
    //As less Gluten Free batches as possible.
    public void bakeOrders(List<Order> orders) {
        System.out.println("Finding best baking configuration ... ");

        //Check for costumers that are ordering only 1 cookie, and lock that preference.
        for(int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            if (o.getCookies().size() == 1) {
                Cookie c = o.getCookies().get(0);

                //Before locking preference, check if a lock already exists on that shape and check if it's the same
                //If it's a conflict, no valid baking configuration exists.
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
                orders.remove(i); //Remove this order from the list because the brute forcer doesn't need to iterate it
                i--;//Decrement because of removal.
                this.updateBestedComp(); //Update the minimum number of gluten free batches.
            }
        }

        this.orders = orders;
        CookieType[] batches = new CookieType[this.numOfShapes];
        System.arraycopy(this.bakeConfiguration, 0, batches, 0, this.numOfShapes);
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
    //Accounts for the baking configuration locks set out in configLocks[] to significantly
    //reduce runtime.
    public boolean bruteForce(CookieType[] config, int i) {
        if (i == numOfShapes) {
            System.out.print("Iterating Configuration: "); printConfiguration(config);
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
            if (bruteForce(config, i + 1)) return true; //Best config with glutenFree = bestedComp has been found so brute forcing can be stopped.
            config[i] = CookieType.GLUTEN_FREE;
            if (bruteForce(config, i + 1)) return true;
        } else {
            if (bruteForce(config, i + 1)) return true;
        }

        return false;
    }

    //Calculates and updates the minimum number of gluten free batches required for the best bake configuration.
    //Bruteforcer stops the moment it finds a baking configuration that matches bestedComp.
    public void updateBestedComp() {
        int bestComp = 0;
        for(int i = 0; i < this.configLocks.length; i++) {
            if (this.configLocks[i]) {
                if (this.bakeConfiguration[i] == CookieType.GLUTEN_FREE) {
                    bestComp++;
                }
            }
        }
        this.bestedComp = bestComp;
    }

    //Calculates the number of gluten free batches in the given bake configuration.
    public int getNumOfGlutenFree(CookieType[] bakeConfig) {
        int glutenFree = 0;
        for(int i = 0; i < bakeConfig.length; i++) {
            if (bakeConfig[i] == CookieType.GLUTEN_FREE) glutenFree++;
        }
        return glutenFree;
    }

    //Print the baking configuration.
    public void printConfiguration(CookieType[] config) {
        for(CookieType type : config) {
            System.out.print(CookieType.getStringType(type) + "\t");
        }
        System.out.println();
    }
}
