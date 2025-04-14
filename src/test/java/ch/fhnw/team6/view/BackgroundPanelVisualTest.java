package ch.fhnw.team6.view;

import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

public class BackgroundPanelVisualTest {

    public static void main(String[] args) {
        // Prepare animation paths
        Map<String, String[]> animationPaths = new HashMap<>();
        animationPaths.put("1", new String[] {
            "src/test/resources/images/background/animation1/1.png",
            "src/test/resources/images/background/animation1/2.png",
            "src/test/resources/images/background/animation1/3.png",
            "src/test/resources/images/background/animation1/4.png"
        });

        ch.fhnw.team6.view.BackgroundPanel backgroundPanel = new ch.fhnw.team6.view.BackgroundPanel(animationPaths, 300, "1");

        // Create JFrame
        JFrame frame = new JFrame("Visual Test for BackgroundPanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Use JLayeredPane to layer buttons over the background
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setSize(800, 600);

        // Set panel size
        backgroundPanel.setSize(800, 600);
        backgroundPanel.setLocation(0, 0);

        // Create Start button
        JButton startButton = new JButton("Start Animation");
        startButton.setSize(150, 40);
        startButton.setLocation((800 - 150) / 2, (600 - 40) / 2 - 30); // Centered

        // Create Stop button
        JButton stopButton = new JButton("Stop Animation");
        stopButton.setSize(150, 40);
        stopButton.setLocation((800 - 150) / 2, (600 - 40) / 2 + 30); // Below start button

        // Button actions
        startButton.addActionListener( _-> backgroundPanel.startAnimation());
        stopButton.addActionListener( _ -> backgroundPanel.stopAnimation());

        // Add components to layered pane
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(startButton, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(stopButton, JLayeredPane.PALETTE_LAYER);

        frame.add(layeredPane);
        frame.setVisible(true);

        backgroundPanel.startAnimation();
    }
}
