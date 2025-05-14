package ch.fhnw.team6.controller;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInput;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ButtonExampleTest {

    private Context pi4j;
    private ButtonExample buttonExample;

    @BeforeEach
    void setUp() {
        // Create a mock Pi4J context
        pi4j = Pi4J.newAutoContext();

        // Create the ButtonExample instance for GPIO 4
        buttonExample = new ButtonExample(pi4j, 4);
    }

    @AfterEach
    void tearDown() {
        buttonExample.shutdown();
        pi4j.shutdown();
    }

    @Test
    void testButtonCreation() {
        // Check that the button instance exists in the Pi4J context
        DigitalInput input = pi4j.registry().get("button-4", DigitalInput.class);
        assertNotNull(input, "Button should be registered in the context");
        assertEquals("button-4", input.id());
        assertEquals("Button on GPIO 4", input.name());
    }

    @Test
    void testButtonListenerTrigger() {
        // Simulate button press and release using the mock provider
        DigitalInput input = pi4j.registry().get("button-4", DigitalInput.class);

        // Cast to MockDigitalInput to access mockState method
        assertTrue(input instanceof MockDigitalInput);
        MockDigitalInput mockInput = (MockDigitalInput) input;

        // Simulate state changes
        mockInput.mockState(DigitalState.HIGH);
        mockInput.mockState(DigitalState.LOW);

        // No assertions here since output is printed, but this ensures no exceptions
    }
}
