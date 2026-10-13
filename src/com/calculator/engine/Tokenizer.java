package com.calculator.engine;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    public static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int length = input.length();
        int i = 0;

        while (i < length) {
            char ch = input.charAt(i);

            // 1. Skip Whitespace
            if (Character.isWhitespace(ch)) {
                i++;
                continue;
            }

            // 2. Parse Numbers (integers and decimals)
            if (Character.isDigit(ch) || ch == '.') {
                StringBuilder numberBuilder = new StringBuilder();
                while (i < length && (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.')) {
                    numberBuilder.append(input.charAt(i));
                    i++;
                }
                tokens.add(new Token(TokenType.NUMBER, numberBuilder.toString()));
                continue;
            }

            // 3. Parse Single-character Operators & Parentheses
            if (isOperator(ch)) {
                tokens.add(new Token(TokenType.OPERATOR, String.valueOf(ch)));
                i++;
                continue;
            }
            if (ch == '(') {
                tokens.add(new Token(TokenType.LPAREN, "("));
                i++;
                continue;
            }
            if (ch == ')') {
                tokens.add(new Token(TokenType.RPAREN, ")"));
                i++;
                continue;
            }

            // 4. Parse Words (Functions like 'sin', 'cos', or Variables like 'ans', 'x')
            if (Character.isLetter(ch)) {
                StringBuilder wordBuilder = new StringBuilder();
                while (i < length && Character.isLetter(input.charAt(i))) {
                    wordBuilder.append(input.charAt(i));
                    i++;
                }
                String word = wordBuilder.toString();
                if (isFunction(word)) {
                    tokens.add(new Token(TokenType.FUNCTION, word));
                } else {
                    tokens.add(new Token(TokenType.VARIABLE, word));
                }
                continue;
            }

            // 5. Unrecognized Character -> Invalid Input!
            throw new IllegalArgumentException("Unexpected character in input: '" + ch + "'");
        }

        return tokens;
    }

    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%' || ch == '^';
    }

    private static boolean isFunction(String word) {
        return word.equalsIgnoreCase("sin") || word.equalsIgnoreCase("cos") ||
               word.equalsIgnoreCase("tan") || word.equalsIgnoreCase("sqrt") ||
               word.equalsIgnoreCase("log") || word.equalsIgnoreCase("ln");
    }
}