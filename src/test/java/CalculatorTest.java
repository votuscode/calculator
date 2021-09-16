import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CalculatorTest {

    final PrintStream DEFAULT_STDOUT = System.out;

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    @BeforeAll
    void beforeAll() {
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    public void afterEach() {
        out.reset();
    }

    @AfterAll
    void afterAll() {
        System.setOut(DEFAULT_STDOUT);
    }

    @Test
    void listOperations__itShouldListAllAvailableOperations() {
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
    void nextOperand__itShouldReadAnInteger() {
        // given
        Calculator calculator = Calculator.from("123");

        // when
        Double operand = calculator.nextOperand("MESSAGE");

        // then
        assertEquals("MESSAGE: ", out.toString());
        assertEquals(123, operand);
    }

    @Test
    void nextOperand__itShouldReadADouble() {
        // given
        Calculator calculator = Calculator.from("123.456");

        // when
        Double operand = calculator.nextOperand("MESSAGE");

        // then
        assertEquals("MESSAGE: ", out.toString());
        assertEquals(123.456, operand);
    }

    @Test
    void nextOperand__itShouldFailWhenInvalidNumber() {
        // given
        Calculator calculator = Calculator.from("INVALID");

        // then
        assertThrows(NoSuchElementException.class, () -> calculator.nextOperand("MESSAGE"));
    }

    @Test
    void nextOperand__itShouldRecoverWhenAValidNumberIsFollowed() {
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
    void nextOperation__itShouldReadAValidOperation() {
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
    void nextOperation__itShouldFailWhenInvalidOperation() {
        // given
        Calculator calculator = Calculator.from("INVALID");

        // then
        assertThrows(NoSuchElementException.class, () -> calculator.nextOperation("MESSAGE"));
    }

    @Test
    void nextOperation__itShouldRecoverWhenValidOperationIsFollowed() {
        // given
        Calculator calculator = Calculator.from("INVALID\n+");

        // when
        Calculator.Operation operation = calculator.nextOperation("MESSAGE");

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
