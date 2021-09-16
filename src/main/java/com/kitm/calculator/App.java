package com.kitm.calculator;

public class App {
    static public void main(String[] args) {
        System.out.println("Console calculator v.1.0.0");

        Calculator calculator = Calculator.from(System.in);
        calculator.listOperations();
        calculator.run();
    }
}
