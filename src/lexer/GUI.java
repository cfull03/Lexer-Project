package lexer;

import javax.swing.*;
import java.awt.*;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class GUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextArea input;
    private JTextArea output;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    public GUI() {
        setTitle("Simple Parser GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(600, 400));

        input = new JTextArea(10, 30);
        input.setLineWrap(true);
        input.setWrapStyleWord(true);
        JScrollPane inputScrollPane = new JScrollPane(input, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        output = new JTextArea(10, 30);
        output.setEditable(false);
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        JScrollPane outputScrollPane = new JScrollPane(output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JButton parseButton = new JButton("Parse");
        parseButton.addActionListener(e -> parse());

        JButton clearButton = new JButton("Clear Output");
        clearButton.addActionListener(e -> output.setText(""));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(parseButton);
        buttonPanel.add(clearButton);

        JPanel contentPane = new JPanel(new BorderLayout(5, 5));
        contentPane.add(inputScrollPane, BorderLayout.CENTER);
        contentPane.add(outputScrollPane, BorderLayout.SOUTH);
        contentPane.add(buttonPanel, BorderLayout.NORTH);

        setContentPane(contentPane);
        setLocationRelativeTo(null);

        startWatch();
    }

    private void parse() {
        if (isRunning.compareAndSet(false, true)) {
            new ParseWorker().execute();
        } else {
            showError("Parser is already running.");
        }
    }

    private void showError(String errorMessage) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Error: " + errorMessage, "Parsing Error", JOptionPane.ERROR_MESSAGE));
    }

    private void showSuccess(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, message, "Parsing Success", JOptionPane.INFORMATION_MESSAGE));
    }

    private void startWatch() {
        Thread watch = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    if (isRunning.get()) {
                        showError("The application has become unresponsive.");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        watch.setDaemon(true);
        watch.start();
    }

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
                    output.append("Parsing failed: " + ex.toString() + "\n");
                    showError(ex.toString());
                });
            } finally {
                isRunning.set(false);
            }
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI().setVisible(true));
    }
}
