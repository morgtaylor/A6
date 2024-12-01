import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class MazeGUI {
    private static int playerRow = 1, playerCol = 0; // Starting position of the player
    private static final int rows = 11, cols = 11; // Maze dimensions
    private static char[][] mazeData; // Stores the maze structure
    private static JTextArea mazeView; // Displays the 3x3 view of the maze
    private static List<int[]> optimalPath; // Stores the optimal path for hints
    private static JPanel mainPanel = new JPanel(new BorderLayout());
    private static JTextArea messageArea = new JTextArea("Press ENTER to start the maze :)");
    private static JFrame frame = null; // Set up main frame

    // Main method to launch the Maze GUI
    public static void main(String[] args) {
        // Generate the maze layout and store it in mazeData
        MazeGenerator mazeGenerator = new MazeGenerator(rows, cols);
        mazeData = mazeGenerator.getMaze();

        // Initialize optimal path array
        optimalPath = new ArrayList<>();

        findPath(playerRow, playerCol, new boolean[rows][cols]); // Find the optimal path and mark it in optimalPath
        frame = initializeFrame(); // Set up main frame
        addInstructionPanel(frame); // Add instructions on the left
        addMazePanel(frame); // Add main maze view panel

        frame.setVisible(true); // Display frame
    }

    // Initialize the main frame settings
    private static JFrame initializeFrame() {
        JFrame frame = new JFrame("Maze Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        return frame;
    }

    // Set up instructions on the left side of the frame
    private static void addInstructionPanel(JFrame frame) {
        JPanel instructionPanel = new JPanel(new BorderLayout());
        JTextArea instructions = new JTextArea("Directions:\n a = West\n w = North\n s = South\n d = East\n h = Hint");
        instructions.setEditable(false);
        instructions.setLineWrap(true);
        instructions.setWrapStyleWord(true);
        instructions.setBackground(frame.getBackground());
        instructions.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0));

        instructionPanel.add(instructions, BorderLayout.CENTER);
        frame.add(instructionPanel, BorderLayout.WEST);
    }

    // Set up and add the main maze panel, initially showing a start message
    private static void addMazePanel(JFrame frame) {
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
        messageArea.setBorder(BorderFactory.createEmptyBorder(75, 30, 10, 10));
        mainPanel.add(messageArea, BorderLayout.CENTER);

        mazeView = new JTextArea();
        mazeView.setFont(new Font("Monospaced", Font.PLAIN, 40));
        mazeView.setEditable(false);
        mazeView.setBorder(BorderFactory.createEmptyBorder(125, 175, 10, 10));

        frame.add(mainPanel, BorderLayout.CENTER);
        addKeyListeners(frame, mainPanel, messageArea);
    }

    // Add key listeners for player movement and hint functionality
    private static void addKeyListeners(JFrame frame, JPanel mazePanel, JTextArea messageArea) {
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Remove message and add maze view
                    mainPanel.remove(messageArea);
                    mainPanel.add(mazeView, BorderLayout.CENTER);
                    // Set initial maze view
                    mazeView.setText(get3x3View());
                    // Refresh the panel to display changes
                    mainPanel.revalidate();
                    mainPanel.repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_H) {
                    mazeView.setText(get3x3ViewWithHints()); // Show hint
                } else {
                    // Handle movement keys
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W -> movePlayer(-1, 0); // Move up
                        case KeyEvent.VK_A -> movePlayer(0, -1); // Move left
                        case KeyEvent.VK_S -> movePlayer(1, 0); // Move down
                        case KeyEvent.VK_D -> movePlayer(0, 1); // Move right
                    }
                    mazeView.setText(get3x3View()); // Update the 3x3 view after movement
                }
            }
        });
    }

    // Move the player if within bounds and not blocked by a wall
    private static void movePlayer(int rowOffset, int colOffset) {
        int newRow = playerRow + rowOffset;
        int newCol = playerCol + colOffset;

        if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && mazeData[newRow][newCol] != '#') {
            playerRow = newRow;
            playerCol = newCol;
            // Check if player has reached the exit
            if (playerRow == rows - 2 && playerCol == cols - 1) {
                displayWinMessage(); // Display win message and handle replay
            }
        }
    }

    // Generate a 3x3 view of the maze around the player without hints
    private static String get3x3View() {
        StringBuilder view = new StringBuilder();
        for (int i = playerRow - 1; i <= playerRow + 1; i++) {
            for (int j = playerCol - 1; j <= playerCol + 1; j++) {
                if (i == playerRow && j == playerCol) {
                    view.append('@'); // Player's current position
                } else if (i < 0 || j < 0 || i >= rows || j >= cols) {
                    view.append(' '); // Out of bounds
                } else {
                    view.append(mazeData[i][j]); // Display maze cell
                }
            }
            view.append("\n");
        }
        return view.toString();
    }

    private static boolean findPath(int row, int col, boolean[][] visited) {
        // Check if the current position is out of bounds or already visited
        if (row < 0 || row >= rows || col < 0 || col >= cols || mazeData[row][col] == '#' || visited[row][col]) {
            return false; // No valid path
        }
        visited[row][col] = true; // Mark current cell as visited
        // Check if we have reached the destination (exit)
        if (row == rows - 2 && col == cols - 1) { // Exit is at (rows-2, cols-1)
            optimalPath.add(new int[] { row, col }); // Mark destination as part of the path
            return true;
        }
        // Explore neighbors in all four directions (up, down, left, right)
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            // Continue exploring if the new position is valid and hasn't been visited
            if (findPath(newRow, newCol, visited)) {
                optimalPath.add(new int[] { row, col }); // Mark this cell as part of the optimal path
                return true; // Stop further recursion once the path is found
            }
        }
        return false; // No path found from this cell
    }

    // Function to check if a cell is part of the optimal path from the player's
    // position onward
    private static boolean isPartOfOptimalPath(int row, int col, int playerIndex) {
        for (int i = 0; i < playerIndex; i++) { // Iterate only up to the current playerIndex
            int[] position = optimalPath.get(i);
            if (position[0] == row && position[1] == col) {
                //System.out.println("Found cell at index " + i + ": " + Arrays.toString(position));
                return true;
            }
        }
        return false;
    }

    // Function to find the player's index in the optimal path
    private static int findPlayerIndexInOptimalPath() {
        for (int i = 0; i < optimalPath.size(); i++) {
            int[] position = optimalPath.get(i);
            if (position[0] == playerRow && position[1] == playerCol) {
                //System.out.println("Player found at: " + Arrays.toString(position));
                return i;
            }
        }
        return -1;
    }

    // Generate a 3x3 view of the maze around the player, displaying hints
    private static String get3x3ViewWithHints() {
        // Check if recalculating optimal path is necessary
        int playerIndex = findPlayerIndexInOptimalPath();
        //System.out.println("Player Index: " + playerIndex);

        if (playerIndex == -1) {
            //System.out.println("Recalculating optimal path...");
            optimalPath = new ArrayList<>();
            findPath(playerRow, playerCol, new boolean[rows][cols]); // Recalculate optimal path from player's position
            playerIndex = findPlayerIndexInOptimalPath(); // Retry finding player index
            if (playerIndex == -1) {
                //System.out.println("Failed to recalculate optimal path.");
                return "Error: Unable to calculate optimal path.";
            }
        }

        StringBuilder view = new StringBuilder();
        for (int i = playerRow - 1; i <= playerRow + 1; i++) {
            for (int j = playerCol - 1; j <= playerCol + 1; j++) {
                if (i == playerRow && j == playerCol) {
                    view.append('@'); // Player's current position
                } else if (i < 0 || j < 0 || i >= rows || j >= cols) {
                    view.append(' '); // Out of bounds
                } else if (isPartOfOptimalPath(i, j, playerIndex)) {
                    view.append('+'); // Show part of the optimal path
                } else {
                    view.append(mazeData[i][j]); // Display maze cell
                }
            }
            view.append("\n");
        }
        return view.toString();
    }

    private static void displayWinMessage() {
        mainPanel.remove(mazeView); // Remove the maze view
        messageArea.setText("You Won!\nDo you want to play again?\nPress 'y' to restart or 'n' to exit.");
        mainPanel.add(messageArea, BorderLayout.CENTER); // Add the message area
        mainPanel.revalidate();
        mainPanel.repaint();
    
        // Add a key listener for replay options to the frame
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Y) {
                    frame.removeKeyListener(this); // Remove the replay listener
                    messageArea.setText("Press ENTER to start the maze :)");
                    restartGame(); // Restart the game
                } else if (e.getKeyCode() == KeyEvent.VK_N) {
                    System.exit(0); // Exit the program
                }
            }
        });
        frame.requestFocusInWindow(); // Ensure the focus is on the frame for input
       }

    private static void restartGame() {
    // Dispose of the current frame to avoid multiple windows
        if (frame != null) {
            frame.dispose();
        }
        // Reset global variables (like player position)
        playerRow = 1;
        playerCol = 0;
        // Call the main method to restart the game
        String[] args = {}; // Pass an empty argument array if necessary
        main(args);
    }
}