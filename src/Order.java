import java.util.ArrayList;

public class Order {
    private ArrayList<Cookie> cookies;

    public Order() {
        this.cookies = new ArrayList<>();
    }

    public ArrayList<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(ArrayList<Cookie> cookies) {
        this.cookies = cookies;
    }

    //No customer will like more than one shape made with gluten-free flour.
    public boolean isValid() {
        int numGlutenFree = 0;
        for(int i = 0; i < cookies.size(); i++) {
            Cookie c = cookies.get(i);
            if (c.getType() == CookieType.GLUTEN_FREE) numGlutenFree++;
            if (numGlutenFree == 2) return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Cookie c : cookies) {
            sb.append("SHAPE" + c.getShape() + ":" + CookieType.getStringType(c.getType()) + "; \t");
        }
        return sb.toString();
    }
}
