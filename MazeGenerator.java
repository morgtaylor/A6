import java.util.Random;
import java.util.Stack;

public class MazeGenerator {

    // Define directions (east, west, north, south)
    private static final int[] DX = {1, -1, 0, 0}; // East, West, North, South for X-axis
    private static final int[] DY = {0, 0, -1, 1}; // East, West, North, South for Y-axis

    // Maze characters
    private static final char WALL = '#';
    private static final char OPEN = '.';
    private static final char EXIT = 'E';
    // Maze dimensions
    private int rows;
    private int cols;
    private char[][] maze;
    private Random random;

    // Constructor to initialize maze dimensions
    public MazeGenerator(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        maze = new char[rows][cols];
        random = new Random();  // Use a new random instance for each run

        // Initialize the maze with walls
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = WALL;
            }
        }

        // Set start and destination points
        maze[1][0] = OPEN; // Start point at maze[1][0]
        maze[rows - 2][cols - 1] = OPEN; // End point at maze[rows-2][cols-1]

        // Generate the maze using DFS
        generateMaze(1, 1);  // Start DFS from (1,1) inside the maze

        // Ensure a path between start and destination
        maze[rows - 2][cols - 1] = EXIT;
    }

    // DFS-based maze generation with backtracking
    private void generateMaze(int x, int y) {
        // Stack for backtracking
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{x, y});

        // DFS loop
        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int cx = current[0];
            int cy = current[1];

            // Get a list of valid neighbors
            int[][] neighbors = getValidNeighbors(cx, cy);

            if (neighbors.length > 0) {
                // Randomly select a neighbor to move to
                int[] neighbor = neighbors[random.nextInt(neighbors.length)];

                int nx = neighbor[0];
                int ny = neighbor[1];

                // Knock down the wall between the current cell and the neighbor
                maze[(cx + nx) / 2][(cy + ny) / 2] = OPEN;

                // Push the neighbor to the stack
                stack.push(new int[]{nx, ny});

                // Mark the neighbor as an open path
                maze[nx][ny] = OPEN;
            } else {
                // Backtrack if no neighbors are available
                stack.pop();
            }
        }
    }

    // Get valid neighbors for DFS (2 steps away in 4 directions)
    private int[][] getValidNeighbors(int x, int y) {
        Stack<int[]> validNeighbors = new Stack<>();

        for (int i = 0; i < DX.length; i++) {
            int nx = x + DX[i] * 2; // Move 2 steps in the x direction
            int ny = y + DY[i] * 2; // Move 2 steps in the y direction

            // Check if the new position is within bounds and not visited
            if (nx > 0 && nx < rows - 1 && ny > 0 && ny < cols - 1 && maze[nx][ny] == WALL) {
                validNeighbors.push(new int[]{nx, ny});
            }
        }
        // Convert Stack to array
        return validNeighbors.toArray(new int[validNeighbors.size()][]);
    }

    // Method to send maze as string to GUI
    public String printMaze() {
        StringBuilder mazeString = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mazeString.append(maze[i][j]).append(" ");
            }
            mazeString.append("\n"); // New line after each row
        }
        return mazeString.toString();
    }
    public char[][] getMaze() {
        return maze; // Getter method for GUI access
    }
}
