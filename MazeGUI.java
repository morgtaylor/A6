import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MazeGUI {
    private static int playerRow = 1, playerCol = 0; // Starting position of the player
    private static final int rows = 17, cols = 21; // Maze dimensions
    private static char[][] mazeData;
   // Main method to test the maze generator with a GUI
    public static void main(String[] args) {
        // Create maze generator
        MazeGenerator mazeGenerator = new MazeGenerator(rows, cols);
        mazeData = mazeGenerator.getMaze();

        // Create a JFrame
        JFrame frame = new JFrame("Maze Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setLocationRelativeTo(null); // Center the window on the screen

        // Create a JTextArea for displaying instructions or the maze
        JTextArea messageArea = new JTextArea("Press ENTER to start the maze :)");
        messageArea.setEditable(false); // Make the text area read-only
        messageArea.setFont(new Font("Monospaced", Font.PLAIN, 20)); // Use a monospaced font
        messageArea.setBorder(BorderFactory.createEmptyBorder(75, 30, 10, 10)); // Add padding
        

        // Create Main Panel for Maze
        JPanel mazePanel = new JPanel(new BorderLayout());
        mazePanel.add(messageArea, BorderLayout.CENTER); // Add message to main panel
        frame.add(mazePanel, BorderLayout.CENTER);

        // Create a JPanel for instructions
        JPanel instructionPanel = new JPanel();
        instructionPanel.setLayout(new BorderLayout());

        // Create a JTextArea for the instructions
        JTextArea instructions = new JTextArea("Directions: \n a = West \n w = North \n s = South \n d = East \n h = Hint");
        instructions.setEditable(false); // Make the text area read-only
        instructions.setLineWrap(true); // Wrap lines
        instructions.setWrapStyleWord(true);
        instructions.setBackground(frame.getBackground()); // Match the frame's background
        instructions.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0)); // Add padding
        
        // Add the instructions to the panel
        instructionPanel.add(instructions, BorderLayout.CENTER);
        frame.add(instructionPanel, BorderLayout.WEST); // Add panel to frame

        // Add 3x3 maze view to frame
        JTextArea mazeView = new JTextArea();
        mazeView.setFont(new Font("Monospaced", Font.PLAIN, 20));
        mazeView.setEditable(false);
        mazeView.setBorder(BorderFactory.createEmptyBorder(10, 17, 10, 10));
        
        // Set initial view text
        mazeView.setText(get3x3View());
        // Show frame
        frame.setVisible(true);

    
        // Add key listener to detect any key press
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    mazePanel.remove(messageArea); // Remove the initial message
                    mazePanel.add(mazeView, BorderLayout.CENTER); // Add the maze view
                    mazePanel.revalidate();
                    mazePanel.repaint();
                } else {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W -> movePlayer(-1, 0); // Move up
                        case KeyEvent.VK_A -> movePlayer(0, -1); // Move left
                        case KeyEvent.VK_S -> movePlayer(1, 0);  // Move down
                        case KeyEvent.VK_D -> movePlayer(0, 1);  // Move right
                    }
                    mazeView.setText(get3x3View()); // Update 3x3 view after movement
                    mazePanel.revalidate();
                    mazePanel.repaint();
                }
            }
        });
        // Ensure the frame is focusable for key events
        frame.setFocusable(true);
        frame.requestFocusInWindow();
    }
    private static void movePlayer(int rowOffset, int colOffset) {
        int newRow = playerRow + rowOffset;
        int newCol = playerCol + colOffset;

        // Check for valid movement (within bounds and not a wall)
        if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && mazeData[newRow][newCol] != '#') {
            playerRow = newRow;
            playerCol = newCol;
        }
    }
    private static String get3x3View() {
        StringBuilder view = new StringBuilder();
        for (int i = playerRow - 1; i <= playerRow + 1; i++) {
            for (int j = playerCol - 1; j <= playerCol + 1; j++) {
                if (i == playerRow && j == playerCol) {
                    view.append('@'); // Player position
                } else if (i < 0 || j < 0 || i >= rows || j >= cols) {
                    view.append(' '); // Out of bounds
                } else {
                    view.append(mazeData[i][j] == '+' ? ' ' : mazeData[i][j]); // Hide '+' unless hint is active
                }
            }
            view.append("\n");
        }
        return view.toString();
    }
}