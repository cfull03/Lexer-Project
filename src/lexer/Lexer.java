package lexer;

import java.util.*;

/**
 * A simple lexical analyzer (lexer) that tokenizes a given source string
 * into a sequence of tokens based on predefined rules.
 */
public class Lexer {

    private final String source;
    private final Queue<Token> tokens = new LinkedList<>();
    private int start = 0;
    private int current = 0;

    /**
     * Constructs a Lexer with the given source string.
     *
     * @param source The input source string to be tokenized.
     */
    public Lexer(String source) {
        this.source = Objects.requireNonNull(source, "Source cannot be null");
    }

    /**
     * Tokenizes the source string and returns a queue of tokens.
     *
     * @return A {@link Queue} of {@link Token} objects representing the tokenized input.
     */
    public Queue<Token> tokenize() {
        while (!isAtEnd()) {
            skipWhitespace();
            start = current;
            scanNext();
        }
        return tokens;
    }

    private void scanNext() {
        if (isAtEnd()) return;
        char c = advance();
        switch (c) {
            case '(' -> tokens.add(new Token("LEFT_PAREN", "("));
            case ')' -> tokens.add(new Token("RIGHT_PAREN", ")"));
            case ';' -> tokens.add(new Token("SEMICOLON", ";"));
            case '+' -> tokens.add(new Token("PLUS", "+"));
            case '-' -> tokens.add(new Token("MINUS", "-"));
            case '*' -> tokens.add(new Token("MULTIPLY", "*"));
            case '/' -> tokens.add(new Token("DIVIDE", "/"));
            case '%' -> tokens.add(new Token("MODULUS", "%"));
            case ':' -> tokens.add(new Token("COLON", ":"));
            case '=' -> tokens.add(match('=') ? new Token("EQUALS", "==") : new Token("ASSIGN", "="));
            case '>' -> tokens.add(match('=') ? new Token("GREATER_THAN_OR_EQUAL_TO", ">=") : new Token("GREATER_THAN", ">"));
            case '<' -> tokens.add(match('=') ? new Token("LESS_THAN_OR_EQUAL_TO", "<=") : new Token("LESS_THAN", "<"));
            case '!' -> tokens.add(match('=') ? new Token("NOT_EQUAL", "!=") : new Token("NOT", "!"));
            default -> {
                if (Character.isDigit(c)) {
                    scanNumber();
                } else if (Character.isLetter(c) || c == '_') {
                    scanIdentifier();
                } else {
                    throw new IllegalArgumentException("Unexpected character: " + c);
                }
            }
        }
    }

    private void scanNumber() {
        while (Character.isDigit(peek())) {
            advance();
        }
        tokens.add(new Token("NUMBER", source.substring(start, current)));
    }

    private void scanIdentifier() {
        while (Character.isLetterOrDigit(peek()) || peek() == '_') {
            advance();
        }
        String id = source.substring(start, current);
        String type = switch (id) {
            case "program" -> "PROGRAM";
            case "end_program" -> "END_PROGRAM";
            case "if" -> "IF";
            case "end_if" -> "END_IF";
            case "loop" -> "LOOP";
            case "end_loop" -> "END_LOOP";
            default -> "IDENTIFIER";
        };
        tokens.add(new Token(type, id));
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char advance() {
        return isAtEnd() ? '\0' : source.charAt(current++);
    }

    private boolean match(char expected) {
        if (isAtEnd() || source.charAt(current) != expected) {
            return false;
        }
        current++;
        return true;
    }

    private char peek() {
        return isAtEnd() ? '\0' : source.charAt(current);
    }

    private void skipWhitespace() {
        while (!isAtEnd() && Character.isWhitespace(peek())) {
            advance();
        }
    }
}
