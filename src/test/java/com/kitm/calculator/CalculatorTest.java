package com.kitm.calculator;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static com.kitm.calculator.Calculator.Operation;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CalculatorTest {

    private final PrintStream DEFAULT_STDOUT = System.out;

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    @BeforeAll
    private void beforeAll() {
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    private void afterEach() {
        out.reset();
    }

    @AfterAll
    private void afterAll() {
        System.setOut(DEFAULT_STDOUT);
    }

    @Test
    public void listOperations__itShouldListAllAvailableOperations() {
        // given
        Calculator calculator = Calculator.from("");

        // when
        calculator.listOperations();

        // then
        assertEquals("""
                Available operations:
                	addition (+)
                	subtraction (-)
                	multiplication (*)
                	division (/)
                """, out.toString());
    }

    @Test
    public void nextOperand__itShouldReadAnInteger() {
        // given
        Calculator calculator = Calculator.from("123");

        // when
        Double operand = calculator.nextOperand("MESSAGE");

        // then
        assertEquals("MESSAGE: ", out.toString());
        assertEquals(123, operand);
    }

    @Test
    public void nextOperand__itShouldReadADouble() {
        // given
        Calculator calculator = Calculator.from("123.456");

        // when
        Double operand = calculator.nextOperand("MESSAGE");

        // then
        assertEquals("MESSAGE: ", out.toString());
        assertEquals(123.456, operand);
    }

    @Test
    public void nextOperand__itShouldFailWhenInvalidNumber() {
        // given
        Calculator calculator = Calculator.from("INVALID");

        // then
        assertThrows(NoSuchElementException.class, () -> calculator.nextOperand("MESSAGE"));
    }

    @Test
    public void nextOperand__itShouldRecoverWhenAValidNumberIsFollowed() {
        // given
        Calculator calculator = Calculator.from("INVALID\n123.456");

        // when
        Double operand = calculator.nextOperand("MESSAGE");

        // then
        assertEquals("""
                MESSAGE: Please enter numeric value!
                MESSAGE:\s""", out.toString());

        assertEquals(123.456, operand);
    }

    @Test
    public void nextOperation__itShouldReadAValidOperation() {
        // given
        Calculator calculator = Calculator.from("+");

        // when
        Calculator.Operation operation = calculator.nextOperation("MESSAGE");

        // then
        assertEquals("MESSAGE: ", out.toString());
        assertEquals("+", operation.name());
        assertEquals("addition", operation.description());
    }

    @Test()
    public void nextOperation__itShouldFailWhenInvalidOperation() {
        // given
        Calculator calculator = Calculator.from("INVALID");

        // then
        assertThrows(NoSuchElementException.class, () -> calculator.nextOperation("MESSAGE"));
    }

    @Test
    public void nextOperation__itShouldRecoverWhenValidOperationIsFollowed() {
        // given
        Calculator calculator = Calculator.from("INVALID\n+");

        // when
        Operation operation = calculator.nextOperation("MESSAGE");

        // then
        assertEquals("""
                MESSAGE: Available operations:
                	addition (+)
                	subtraction (-)
                	multiplication (*)
                	division (/)
                	""", out.toString());

        assertEquals("+", operation.name());
        assertEquals("addition", operation.description());
    }
}
