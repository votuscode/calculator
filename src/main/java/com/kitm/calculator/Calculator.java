package com.kitm.calculator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.BiFunction;

public class Calculator {
    public static record Operation(String name, String description, BiFunction<Double, Double, Double> op) {
        public boolean match(String value) {
            return name.equals(value);
        }

        public double execute(Double left, Double right) {
            return op.apply(left, right);
        }
    }

    private static Operation[] operations = {
            new Operation("+", "addition", Double::sum),
            new Operation("-", "subtraction", (a, b) -> a - b),
            new Operation("*", "multiplication", (a, b) -> a * b),
            new Operation("/", "division", (a, b) -> a / b),
    };

    private Scanner scanner;

    void listOperations() {
        System.out.println("Available operations:");

        for (Operation operation : operations) {
            System.out.printf("\t%s (%s)\n", operation.description, operation.name);
        }
    }

    double nextOperand(String message) {
        while (true) {
            System.out.printf("%s: ", message);

            String value = scanner.next();

            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                System.out.println("Please enter numeric value!");
            }
        }
    }

    Operation nextOperation(String message) {
        System.out.printf("%s: ", message);

        while (true) {
            String value = scanner.next();

            Optional<Operation> operation = Arrays.stream(operations).filter(s -> s.match(value)).findFirst();

            if (operation.isPresent()) {
                return operation.get();
            } else {
                listOperations();
            }
        }
    }

    void mainLoop() {
        while (true) {
            Double left = nextOperand("Please enter the first operand");
            Operation operation = nextOperation("Please enter the operation");
            Double right = nextOperand("Please enter the second operand");

            System.out.printf("The result is: %.2f\n\n", operation.execute(left, right));

            System.out.print("Wanna make it again? (no): ");
            if (scanner.next().equalsIgnoreCase("no")) {
                return;
            }
        }
    }

    public void run() {
        mainLoop();
        scanner.close();
    }

    private Calculator(InputStream inputStream) {
        scanner = new Scanner(inputStream);
    }

    public static Calculator from(String input) {
        return new Calculator(new ByteArrayInputStream(input.getBytes()));
    }

    public static Calculator from(InputStream inputStream) {
        return new Calculator(inputStream);
    }
}
