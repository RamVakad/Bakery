public enum CookieType {
    REGULAR, GLUTEN_FREE;


    public static CookieType getCookieType(String type) throws UnsupportedCookieTypeException {
        switch(type) {
            case "R": return REGULAR;
            case "G": return GLUTEN_FREE;
            default: throw new UnsupportedCookieTypeException("CookieType: " + type + " not supported.");
        }
    }

    public static String getStringType(CookieType type) {
        switch(type) {
            case GLUTEN_FREE: return "G";
            case REGULAR: return "R";
            default: return "NEVER_HERE";
        }
    }
}