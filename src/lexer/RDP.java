package lexer;

import java.util.Queue;

public class RDP {
    private Queue<Token> tokens;
    private Token currentToken;

    public RDP(Queue<Token> tokens) {
        this.tokens = tokens;
        lex();
    }

    private void lex() {
        currentToken = tokens.poll();
        if (currentToken != null) {
            System.out.println("Current Token: " + currentToken.getType() + ", Value: " + currentToken.getValue());
        } else {
            System.out.println("End of Token Stream");
        }
    }

    public void program() {
        if (!checkToken("PROGRAM", "Expected 'PROGRAM' at the beginning")) return;
        while (!checkToken("END_PROGRAM", "Expected 'END_PROGRAM' at the end")) {
            statement();
        }
    }

    private void statement() {
        while (currentToken != null && !isEndToken()) {
            switch (currentToken.getType()) {
                case "LOOP":
                    loop();
                    break;
                case "IF":
                    condition();
                    break;
                case "VAR":
                    assignment();
                    break;
                default:
                    error("Invalid statement. Unexpected token: " + currentToken.getType());
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

        while (currentToken != null && !"END_LOOP".equals(currentToken.getType()) && !"END_PROGRAM".equals(currentToken.getType())) {
            statement();
        }

        if (!checkToken("END_LOOP", "Missing 'END_LOOP'")) return;
    }

    private void condition() {
        lex();
        if (!checkToken("LEFT_PAREN", "Expected '(' after 'IF'")) return;
        logic();
        if (!checkToken("RIGHT_PAREN", "Expected ')' after condition")) return;

        while (currentToken != null && !"END_IF".equals(currentToken.getType()) && !"ELSE".equals(currentToken.getType())) {
            statement();
        }

        if ("ELSE".equals(currentToken.getType())) {
            lex();
            while (currentToken != null && !"END_IF".equals(currentToken.getType())) {
                statement();
            }
        }

        if (!checkToken("END_IF", "Missing 'END_IF'")) return;
    }

    private void assignment() {
        lex();
        if (!checkToken("ASSIGN", "Expected '=' after variable")) return;
        expression();
        if (!checkToken("SEMICOLON", "Expected ';' after expression in assignment")) return;
    }

    private void logic() {
        expression();
        if (!isLogicalOperator(currentToken.getType())) {
            error("Expected a logical operator (==, !=, >, <, >=, <=)");
        }
        lex();
        expression();
    }

    private void expression() {
        term();
        while (currentToken != null && ("PLUS".equals(currentToken.getType()) || "MINUS".equals(currentToken.getType()))) {
            lex();
            term();
        }
    }

    private void term() {
        factor();
        while (currentToken != null && ("MULTIPLY".equals(currentToken.getType()) || "DIVIDE".equals(currentToken.getType()) || "MODULUS".equals(currentToken.getType()))) {
            lex();
            factor();
        }
    }

    private void factor() {
        if ("VAR".equals(currentToken.getType()) || "INTEGER".equals(currentToken.getType())) {
            lex();
        } else if ("LEFT_PAREN".equals(currentToken.getType())) {
            lex();
            expression();
            if (!checkToken("RIGHT_PAREN", "Expected ')'")) return;
        } else {
            error("Unexpected token in factor: " + currentToken.getType());
        }
    }

    private boolean isLogicalOperator(String tokenType) {
        return "EQUALS".equals(tokenType) || "NOT_EQUALS".equals(tokenType) ||
               "GREATER_THAN".equals(tokenType) || "LESS_THAN".equals(tokenType) ||
               "GREATER_THAN_OR_EQUAL_TO".equals(tokenType) || "LESS_THAN_OR_EQUAL_TO".equals(tokenType);
    }

    private void error(String message) {
        throw new IllegalArgumentException("Parse error: " + message + 
                (currentToken != null ? ". Unexpected token: " + currentToken.getType() + " with value " + currentToken.getValue() : ". Unexpected end of token stream"));
    }

    private boolean checkToken(String expectedType, String errorMessage) {
        if (currentToken == null || !expectedType.equals(currentToken.getType())) {
            error(errorMessage);
            return false;
        }
        lex();
        return true;
    }

    private boolean isEndToken() {
        return "END_PROGRAM".equals(currentToken.getType()) || "END_IF".equals(currentToken.getType()) || "END_LOOP".equals(currentToken.getType());
    }
}
