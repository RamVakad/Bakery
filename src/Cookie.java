public class Cookie {
    private CookieType type;

    private Integer shape;

    public Cookie(CookieType type, Integer shape) {
        this.type = type;
        this.shape = shape;
    }

    public CookieType getType() {
        return type;
    }

    public void setType(CookieType type) {
        this.type = type;
    }

    public Integer getShape() {
        return shape;
    }

    public void setShape(Integer shape) {
        this.shape = shape;
    }

}