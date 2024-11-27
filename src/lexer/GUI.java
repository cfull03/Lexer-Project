package lexer;

import javax.swing.*;
import java.awt.*;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A graphical user interface for a simple parser application.
 * Users can input code, parse it, and view the output or errors.
 */
public class GUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JTextArea input;
    private final JTextArea output;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    /**
     * Constructs the GUI and sets up its components.
     */
    public GUI() {
        setTitle("Simple Parser GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        // Input Text Area
        input = new JTextArea(10, 30);
        input.setLineWrap(true);
        input.setWrapStyleWord(true);
        var inputScrollPane = new JScrollPane(input, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Output Text Area
        output = new JTextArea(10, 30);
        output.setEditable(false);
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        var outputScrollPane = new JScrollPane(output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Buttons
        var parseButton = new JButton("Parse");
        parseButton.addActionListener(e -> parse());

        var clearButton = new JButton("Clear Output");
        clearButton.addActionListener(e -> output.setText(""));

        // Button Panel
        var buttonPanel = new JPanel();
        buttonPanel.add(parseButton);
        buttonPanel.add(clearButton);

        // Main Content Pane
        var contentPane = new JPanel(new BorderLayout(5, 5));
        contentPane.add(inputScrollPane, BorderLayout.CENTER);
        contentPane.add(outputScrollPane, BorderLayout.SOUTH);
        contentPane.add(buttonPanel, BorderLayout.NORTH);

        setContentPane(contentPane);
        setLocationRelativeTo(null);
    }

    /**
     * Starts the parsing process.
     */
    private void parse() {
        if (isRunning.compareAndSet(false, true)) {
            new ParseWorker().execute();
        } else {
            showError("Parser is already running.");
        }
    }

    /**
     * Displays an error message in a dialog box.
     *
     * @param errorMessage The error message to display.
     */
    private void showError(String errorMessage) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Error: " + errorMessage, "Parsing Error", JOptionPane.ERROR_MESSAGE));
    }

    /**
     * Displays a success message in a dialog box.
     *
     * @param message The success message to display.
     */
    private void showSuccess(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, message, "Parsing Success", JOptionPane.INFORMATION_MESSAGE));
    }

    /**
     * Worker thread for parsing the input asynchronously.
     */
    private class ParseWorker extends SwingWorker<Void, Void> {
        @Override
        protected Void doInBackground() {
            String code = input.getText();
            try {
                Lexer lexer = new Lexer(code);
                Queue<Token> tokens = lexer.tokenize();

                RDP parser = new RDP(tokens);
                parser.program();

                SwingUtilities.invokeLater(() -> {
                    output.append("Parsing successful!\n");
                    showSuccess("Parsing successful!");
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    output.append("Parsing failed: " + ex.getMessage() + "\n");
                    showError(ex.getMessage());
                });
            } finally {
                isRunning.set(false);
            }
            return null;
        }
    }
}
