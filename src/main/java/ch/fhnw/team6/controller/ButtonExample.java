package ch.fhnw.team6.controller;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalInputConfig;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.PullResistance;

public class ButtonExample {
    private final DigitalInput button;

    public ButtonExample(Context pi4j, int pinAddress) {
        // Configure the button as a digital input
        DigitalInputConfig config = DigitalInput.newConfigBuilder(pi4j)
            .id("button-" + pinAddress)
            .name("Button on GPIO " + pinAddress)
            .address(pinAddress)
            .pull(PullResistance.PULL_DOWN) // Use PULL_DOWN for a normally open button
            .debounce(3000L) // Debounce time in microseconds
            .build();

        this.button = pi4j.create(config);

        // Add a listener to handle button state changes
        this.button.addListener(event -> {
            if (event.state() == DigitalState.HIGH) {
                System.out.println("Button pressed!");
                onButtonPressed();
            } else if (event.state() == DigitalState.LOW) {
                System.out.println("Button released!");
                onButtonReleased();
            }
        });
    }

    // Define actions for button press
    private void onButtonPressed() {
        // Add your logic here
        System.out.println("Performing action on button press...");
    }

    // Define actions for button release
    private void onButtonReleased() {
        // Add your logic here
        System.out.println("Performing action on button release...");
    }

    public void shutdown() {
        // Clean up resources
        button.shutdown(null);
    }
}