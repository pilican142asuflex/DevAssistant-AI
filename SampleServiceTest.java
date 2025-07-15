package devassistant.generated;

import devassistant.SampleService;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SampleServiceTest {

    private final SampleService service = new SampleService();

    @Test
    public void testPositiveNumbers() {
        assertEquals(12, service.multiply(3, 4));
    }
    
    @Test
    public void testNegativeNumbers() {
        assertEquals(-10, service.multiply(-2, 5));
    }
    
    @Test
    public void testZero() {
        assertEquals(0, service.multiply(5, 0));
    }

    @Test
    public void testNullName() {
        assertEquals("Hello, Guest", service.greet(null));
    }
    
    @Test
    public void testValidName() {
        assertEquals("Hello, John", service.greet("John"));
    }
    
    @Test
    public void testEmptyName() {
        assertEquals("Hello, Guest", service.greet(""));
    }

}