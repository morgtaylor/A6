import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MazeGUI {
   // Main method to test the maze generator with a GUI
    public static void main(String[] args) {
        int rows = 11; // Number of rows in the maze
        int cols = 21; // Number of columns in the maze

        // Create maze generator
        MazeGenerator mazeGenerator = new MazeGenerator(rows, cols);

        // Create a JFrame
        JFrame frame = new JFrame("Maze Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setLocationRelativeTo(null); // Center the window on the screen

        // Create a JTextArea for displaying instructions or the maze
        JTextArea messageArea = new JTextArea("Press any key to display the maze :)");
        messageArea.setEditable(false); // Make the text area read-only
        messageArea.setFont(new Font("Monospaced", Font.PLAIN, 20)); // Use a monospaced font
        messageArea.setBorder(BorderFactory.createEmptyBorder(75, 30, 10, 10)); // Add padding

        // Create Main Panel for Maze
        JPanel mazePanel = new JPanel(new BorderLayout());

        // Add message to main panel
        mazePanel.add(messageArea, BorderLayout.CENTER);

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

        // Add the instruction panel & maze panel to the frame
        frame.add(mazePanel, BorderLayout.CENTER);
        frame.add(instructionPanel, BorderLayout.WEST);

        JTextArea maze = new JTextArea(mazeGenerator.printMaze());
        maze.setFont(new Font("Monospaced", Font.PLAIN, 19));
        maze.setBorder(BorderFactory.createEmptyBorder(75, 15, 10, 1));
    
        // Add key listener to detect any key press
        mazePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Key Pressed!");
                mazePanel.remove(messageArea);
                // Print the maze when any key is pressed
                mazePanel.add(maze, BorderLayout.CENTER);
                mazePanel.revalidate(); // Refresh the panel to show the maze
                mazePanel.repaint(); // Repaint to reflect changes
            }
        });

        mazePanel.setFocusable(true); // Make mazePanel focusable
        mazePanel.requestFocusInWindow(); // Request focus on mazePanel

        // Show the JFrame
        frame.setVisible(true);
    
        // Request focus for mazePanel after the frame is visible
        SwingUtilities.invokeLater(() -> mazePanel.requestFocusInWindow());
    }
}