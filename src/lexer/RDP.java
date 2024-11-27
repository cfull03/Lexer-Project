package lexer;

import java.util.Queue;

/**
 * Recursive Descent Parser (RDP) for token streams.
 * This class parses a queue of tokens based on predefined grammar rules.
 */
public class RDP {

    private final Queue<Token> tokens;
    private Token currentToken;

    /**
     * Constructs the RDP with the provided queue of tokens.
     *
     * @param tokens The {@link Queue} of {@link Token} objects to parse.
     */
    public RDP(Queue<Token> tokens) {
        this.tokens = tokens;
        lex();
    }

    /**
     * Advances to the next token in the token stream.
     */
    private void lex() {
        currentToken = tokens.poll();
        if (currentToken != null) {
            System.out.println("Current Token: " + currentToken.type() + ", Value: " + currentToken.value());
        } else {
            System.out.println("End of Token Stream");
        }
    }

    /**
     * Parses a program based on the grammar rules.
     */
    public void program() {
        if (!checkToken("PROGRAM", "Expected 'PROGRAM' at the beginning")) return;
        while (!checkToken("END_PROGRAM", "Expected 'END_PROGRAM' at the end")) {
            statement();
        }
    }

    private void statement() {
        while (currentToken != null && !isEndToken()) {
            switch (currentToken.type()) {
                case "LOOP" -> loop();
                case "IF" -> condition();
                case "VAR" -> assignment();
                default -> error("Invalid statement. Unexpected token: " + currentToken.type());
            }
        }
    }

    private void loop() {
        lex();
        if (!checkToken("LEFT_PAREN", "Expected '(' after 'LOOP'")) return;
        if (!checkToken("VAR", "Expected variable in loop initialization")) return;
        if (!checkToken("ASSIGN", "Expected '=' in loop initialization")) return;
        expression();
        if (!checkToken("COLON", "Expected ':' after loop initialization")) return;
        logic();
        if (!checkToken("RIGHT_PAREN", "Expected ')' after loop condition")) return;

        while (currentToken != null && !"END_LOOP".equals(currentToken.type()) && !"END_PROGRAM".equals(currentToken.type())) {
            statement();
        }

        checkToken("END_LOOP", "Missing 'END_LOOP'");
    }

    private void condition() {
        lex();
        if (!checkToken("LEFT_PAREN", "Expected '(' after 'IF'")) return;
        logic();
        if (!checkToken("RIGHT_PAREN", "Expected ')' after condition")) return;

        while (currentToken != null && !"END_IF".equals(currentToken.type()) && !"ELSE".equals(currentToken.type())) {
            statement();
        }

        if ("ELSE".equals(currentToken.type())) {
            lex();
            while (currentToken != null && !"END_IF".equals(currentToken.type())) {
                statement();
            }
        }

        checkToken("END_IF", "Missing 'END_IF'");
    }

    private void assignment() {
        lex();
        if (!checkToken("ASSIGN", "Expected '=' after variable")) return;
        expression();
        checkToken("SEMICOLON", "Expected ';' after expression in assignment");
    }

    private void logic() {
        expression();
        if (!isLogicalOperator(currentToken.type())) {
            error("Expected a logical operator (==, !=, >, <, >=, <=)");
        }
        lex();
        expression();
    }

    private void expression() {
        term();
        while (currentToken != null && ("PLUS".equals(currentToken.type()) || "MINUS".equals(currentToken.type()))) {
            lex();
            term();
        }
    }

    private void term() {
        factor();
        while (currentToken != null && ("MULTIPLY".equals(currentToken.type()) || "DIVIDE".equals(currentToken.type()) || "MODULUS".equals(currentToken.type()))) {
            lex();
            factor();
        }
    }

    private void factor() {
        if ("VAR".equals(currentToken.type()) || "NUMBER".equals(currentToken.type())) {
            lex();
        } else if ("LEFT_PAREN".equals(currentToken.type())) {
            lex();
            expression();
            checkToken("RIGHT_PAREN", "Expected ')'");
        } else {
            error("Unexpected token in factor: " + (currentToken != null ? currentToken.type() : "null"));
        }
    }

    private boolean isLogicalOperator(String tokenType) {
        return switch (tokenType) {
            case "EQUALS", "NOT_EQUAL", "GREATER_THAN", "LESS_THAN", "GREATER_THAN_OR_EQUAL_TO", "LESS_THAN_OR_EQUAL_TO" -> true;
            default -> false;
        };
    }

    private void error(String message) {
        throw new IllegalArgumentException("Parse error: " + message +
                (currentToken != null ? ". Unexpected token: " + currentToken.type() + " with value " + currentToken.value() : ". Unexpected end of token stream"));
    }

    private boolean checkToken(String expectedType, String errorMessage) {
        if (currentToken == null || !expectedType.equals(currentToken.type())) {
            error(errorMessage);
            return false;
        }
        lex();
        return true;
    }

    private boolean isEndToken() {
        return "END_PROGRAM".equals(currentToken.type()) || "END_IF".equals(currentToken.type()) || "END_LOOP".equals(currentToken.type());
    }
}
