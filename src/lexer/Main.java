package lexer;

import javax.swing.SwingUtilities;

/**
 * The entry point for the lexer application.
 * Launches the graphical user interface (GUI) on the Event Dispatch Thread (EDT).
 */
public class Main {
    /**
     * Main method to start the lexer application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var gui = new GUI();
            gui.setVisible(true);
        });
    }
}


