package com.calculator.engine;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ExpressionParser {

    /**
     * Converts an infix list of tokens into postfix (Reverse Polish Notation)
     * using the Shunting-Yard algorithm.
     */
    public static List<Token> parseToPostfix(List<Token> tokens) {
        List<Token> outputQueue = new ArrayList<>();
        Deque<Token> operatorStack = new ArrayDeque<>();

        for (Token token : tokens) {
            switch (token.type()) {
                case NUMBER, VARIABLE -> outputQueue.add(token);

                case FUNCTION -> operatorStack.push(token);

                case OPERATOR -> {
                    while (!operatorStack.isEmpty()
                            && operatorStack.peek().type() != TokenType.LPAREN
                            && (operatorStack.peek().type() == TokenType.FUNCTION
                                || getPrecedence(operatorStack.peek().value()) > getPrecedence(token.value())
                                || (getPrecedence(operatorStack.peek().value()) == getPrecedence(token.value()) 
                                    && isLeftAssociative(token.value())))) {
                        outputQueue.add(operatorStack.pop());
                    }
                    operatorStack.push(token);
                }

                case LPAREN -> operatorStack.push(token);

                case RPAREN -> {
                    boolean foundLparen = false;
                    while (!operatorStack.isEmpty()) {
                        if (operatorStack.peek().type() == TokenType.LPAREN) {
                            operatorStack.pop(); // Discard matching '('
                            foundLparen = true;
                            break;
                        }
                        outputQueue.add(operatorStack.pop());
                    }

                    if (!foundLparen) {
                        throw new IllegalArgumentException("Mismatched parentheses: extra closing parenthesis ')'");
                    }

                    // If a function call precedes this parenthesis block, move function to output
                    if (!operatorStack.isEmpty() && operatorStack.peek().type() == TokenType.FUNCTION) {
                        outputQueue.add(operatorStack.pop());
                    }
                }
            }
        }

        // Empty remaining operators from stack
        while (!operatorStack.isEmpty()) {
            Token top = operatorStack.pop();
            if (top.type() == TokenType.LPAREN || top.type() == TokenType.RPAREN) {
                throw new IllegalArgumentException("Mismatched parentheses: unclosed opening parenthesis '('");
            }
            outputQueue.add(top);
        }

        return outputQueue;
    }

    private static int getPrecedence(String op) {
        return switch (op) {
            case "+", "-" -> 1;
            case "*", "/", "%" -> 2;
            case "^" -> 3;
            default -> 0;
        };
    }

    private static boolean isLeftAssociative(String op) {
        // Exponentiation (^) is right-associative (2^3^2 evaluates as 2^(3^2))
        return !op.equals("^");
    }
}