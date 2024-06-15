package lexer;

import java.util.*;

public class Lexer {
	private final String source;
    private final Queue<Token> tokens = new LinkedList<Token>();
    private int start = 0;
    private int current = 0;

    public Lexer(String source) {
        this.source = source;
    }

    public Queue<Token> tokenize() {
        while(!end()) {
            whitespace();
            start = current;
            next();
        }
        return tokens;
    }

    private void next() {
        if(end()) return;
        char c = forward();
        switch(c) {
            case '(': tokens.add(new Token("LEFT_PAREN", "(")); break;
            case ')': tokens.add(new Token("RIGHT_PAREN", ")")); break;
            case ';': tokens.add(new Token("SEMICOLON", ";")); break;
            case '+': tokens.add(new Token("PLUS", "+")); break;
            case '-': tokens.add(new Token("MINUS", "-")); break;
            case '*': tokens.add(new Token("MULTIPLY", "*")); break;
            case '/': tokens.add(new Token("DIVIDE", "/")); break;
            case '%': tokens.add(new Token("MODULUS", "%")); break;
            case ':': tokens.add(new Token("COLON", ":")); break;
            case '=':
                if(match('=')) {
                    tokens.add(new Token("EQUALS", "=="));
                    current++;
                }else {
                    tokens.add(new Token("ASSIGN", "="));
                }
                break;
            case '>':
                if(match('=')) {
                    tokens.add(new Token("GREATER_THAN_OR_EQUAL_TO", ">="));
                    current++;
                } else {
                    tokens.add(new Token("GREATER_THAN", ">"));
                }
                break;
            case '<':
                if(match('=')) {
                    tokens.add(new Token("LESS_THAN_OR_EQUAL_TO", "<="));
                    current++;
                } else {
                    tokens.add(new Token("LESS_THAN", "<"));
                }
                break;
            case '!':
            	if(match('=')) {
            		tokens.add(new Token("NOT_EQUAL", "!="));
            		current++;
            	}else {
            		tokens.add(new Token("NOT", "!"));
            	}
            	break;
            default:
                if(Character.isDigit(c)) {
                    number();
                } else if(Character.isLetter(c) || c == '_') {
                    identifier();
                } else {
                    throw new IllegalArgumentException("Unexpected character: " + c);
                }
                break;
        }
    }

    private void number() {
        while(Character.isDigit(peek())) {
            forward();
        }
        tokens.add(new Token("VAR", source.substring(start, current)));
    }

    private void identifier() {
        while(Character.isLetterOrDigit(peek()) || peek() == '_') {
            forward();
        }
        String id = source.substring(start, current);
        String type = switch (id) {
            case "program" -> "PROGRAM";
            case "end_program" -> "END_PROGRAM";
            case "if" -> "IF";
            case "end_if" -> "END_IF";
            case "loop" -> "LOOP";
            case "end_loop" -> "END_LOOP";
            default -> "VAR";
        };
        tokens.add(new Token(type, id));
    }

    private boolean end() {
        return current >= source.length();
    }

    private char forward() {
        if (!end()) {
            return source.charAt(current++);
        }
        return '\0'; 
    }

    private boolean match(char expected) {
        if (end() || source.charAt(current) != expected) {
            return false;
        }
        current++;
        return true;
    }

    private char peek() {
        if (!end()) {
            return source.charAt(current);
        }
        return '\0';
    }
    
    private void whitespace() {
        while (!end() && Character.isWhitespace(peek())) {
            forward();
        }
    }
}






