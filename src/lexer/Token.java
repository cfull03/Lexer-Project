package lexer;

public class Token {
    private final String type;
    private final String value;

    public Token(String type, String value) {
        if (type == null || value == null) {
            throw new IllegalArgumentException("Type and value must not be null");
        }
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}


