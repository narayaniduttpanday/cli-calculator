package com.calculator.cli;

import com.calculator.engine.Evaluator;
import com.calculator.engine.ExpressionParser;
import com.calculator.engine.Token;
import com.calculator.engine.Tokenizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Map<String, Double> variables = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Default constants
        variables.put("pi", Math.PI);
        variables.put("e", Math.E);
        variables.put("ans", 0.0);

        System.out.println("========================================");
        System.out.println("       CLI Calculator (Java 25)         ");
        System.out.println(" Type 'help' for commands or 'exit' to quit");
        System.out.println("========================================\n");

        while (running) {
            System.out.print("calc> ");
            String input = scanner.nextLine().trim();

            if (input.isBlank()) {
                continue;
            }

            switch (input.toLowerCase()) {
                case "exit", "quit" -> {
                    System.out.println("Exiting calculator. Goodbye!");
                    running = false;
                }
                case "help" -> printHelp();
                case "vars" -> System.out.println("Variables: " + variables);
                default -> evaluateExpression(input);
            }
        }

        scanner.close();
    }

    private static void evaluateExpression(String input) {
        try {
            List<Token> tokens = Tokenizer.tokenize(input);
            List<Token> postfix = ExpressionParser.parseToPostfix(tokens);
            double result = Evaluator.evaluate(postfix, variables);

            // Store result in 'ans' for subsequent calculations
            variables.put("ans", result);

            // Format clean integers vs decimals
            if (result == (long) result) {
                System.out.println("= " + (long) result);
            } else {
                System.out.println("= " + result);
            }
        } catch (ArithmeticException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void printHelp() {
        System.out.println("\n--- CLI Calculator Commands ---");
        System.out.println("  exit, quit  : Exit application");
        System.out.println("  vars        : View stored variables and constants");
        System.out.println("  help        : Display help");
        System.out.println("  <expr>      : Supports +, -, *, /, %, ^, sin, cos, tan, sqrt, log, ln");
        System.out.println("  Constants   : pi, e, ans\n");
    }
}