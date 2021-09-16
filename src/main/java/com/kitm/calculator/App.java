package com.kitm.calculator;

public class App {
    public static void main(String[] args) {
        System.out.println("Console calculator v.1.0.0");

        Calculator calculator = Calculator.from(System.in);
        calculator.listOperations();
        calculator.run();
    }
}
