package com.calculator.engine;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public class Evaluator {

    /**
     * Evaluates a postfix token list given a context map of variables.
     */
    public static double evaluate(List<Token> postfixTokens, Map<String, Double> variables) {
        Deque<Double> stack = new ArrayDeque<>();

        for (Token token : postfixTokens) {
            switch (token.type()) {
                case NUMBER -> stack.push(Double.parseDouble(token.value()));

                case VARIABLE -> {
                    String varName = token.value().toLowerCase();
                    if (!variables.containsKey(varName)) {
                        throw new IllegalArgumentException("Undefined variable: '" + token.value() + "'");
                    }
                    stack.push(variables.get(varName));
                }

                case OPERATOR -> {
                    if (stack.size() < 2) {
                        throw new IllegalArgumentException("Invalid expression: insufficient operands for operator '" + token.value() + "'");
                    }
                    double b = stack.pop(); // Right operand
                    double a = stack.pop(); // Left operand
                    stack.push(applyOperator(token.value(), a, b));
                }

                case FUNCTION -> {
                    if (stack.isEmpty()) {
                        throw new IllegalArgumentException("Invalid expression: missing argument for function '" + token.value() + "'");
                    }
                    double arg = stack.pop();
                    stack.push(applyFunction(token.value(), arg));
                }

                default -> throw new IllegalArgumentException("Unexpected token in evaluator: " + token.type());
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid mathematical expression.");
        }

        return stack.pop();
    }

    private static double applyOperator(String op, double a, double b) {
        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> {
                if (b == 0) throw new ArithmeticException("Division by zero");
                yield a / b;
            }
            case "%" -> {
                if (b == 0) throw new ArithmeticException("Modulo by zero");
                yield a % b;
            }
            case "^" -> Math.pow(a, b);
            default -> throw new IllegalArgumentException("Unknown operator: " + op);
        };
    }

    private static double applyFunction(String func, double arg) {
        return switch (func.toLowerCase()) {
            case "sin" -> Math.sin(Math.toRadians(arg)); // Evaluates in degrees
            case "cos" -> Math.cos(Math.toRadians(arg));
            case "tan" -> Math.tan(Math.toRadians(arg));
            case "sqrt" -> {
                if (arg < 0) throw new ArithmeticException("Cannot take square root of negative number");
                yield Math.sqrt(arg);
            }
            case "log" -> Math.log10(arg);
            case "ln" -> Math.log(arg);
            default -> throw new IllegalArgumentException("Unknown function: " + func);
        };
    }
}