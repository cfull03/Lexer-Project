package lexer;

/**
 * Represents a token in a source string.
 *
 * @param type  The type of the token (e.g., "IDENTIFIER", "NUMBER"). Must not be null.
 * @param value The value of the token (e.g., the actual string representation). Must not be null.
 * @throws IllegalArgumentException if type or value is null.
 */
public record Token(String type, String value) {
    /**
     * Constructs a {@code Token} and validates its parameters.
     *
     * @param type  The type of the token.
     * @param value The value of the token.
     */
    public Token {
        if (type == null || value == null) {
            throw new IllegalArgumentException("Type and value must not be null");
        }
    }
}



