import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TurtleMazeGame extends JFrame implements KeyListener {
    private int turtleX, turtleY;
    private int currentLevel = 0;
    private final int mazeSize = 10;
    private final int cellSize = 50;
    private char[][][] levels = new char[][][]{
            // Level 1
            {
                {'S', ' ', ' ', '#', '#', ' ', '#', ' ', '#', 'E'},
                {'#', '#', ' ', ' ', '#', ' ', '#', ' ', '#', ' '},
                {'#', ' ', ' ', '#', '#', ' ', ' ', ' ', '#', ' '},
                {'#', '#', ' ', ' ', ' ', ' ', '#', ' ', '#', ' '},
                {'#', ' ', '#', ' ', '#', ' ', '#', ' ', ' ', ' '},
                {'#', ' ', '#', ' ', '#', ' ', '#', '#', '#', '#'},
                {'#', ' ', ' ', ' ', '#', ' ', ' ', ' ', ' ', ' '},
                {'#', ' ', '#', ' ', ' ', ' ', ' ', '#', '#', '#'},
                {'#', ' ', '#', '#', '#', ' ', '#', ' ', ' ', ' '},
                {'#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#', ' '}
            },
            // Level 2
            {
                {'S', ' ', ' ', '#', '#', ' ', '#', ' ', ' ', ' '},
                {'#', ' ', ' ', ' ', '#', ' ', '#', ' ', '#', ' '},
                {'#', ' ', ' ', '#', '#', ' ', '#', ' ', ' ', ' '},
                {'#', '#', ' ', ' ', ' ', ' ', '#', ' ', '#', ' '},
                {'#', ' ', '#', ' ', '#', ' ', ' ', ' ', ' ', ' '},
                {'#', ' ', '#', ' ', ' ', ' ', '#', '#', '#', '#'},
                {'#', ' ', ' ', ' ', '#', ' ', ' ', ' ', ' ', ' '},
                {'#', ' ', '#', ' ', ' ', ' ', '*', '#', '#', '#'},
                {'#', ' ', '#', '#', '#', '#', '#', ' ', ' ', ' '},
                {'#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#', 'E'}
            },
            // Level 3
            {
                {'S', ' ', ' ', '#', '#', ' ', '#', ' ', ' ', 'E'},
                {'#', '#', ' ', ' ', '#', ' ', '#', ' ', '#', '#'},
                {'#', ' ', ' ', '#', '#', ' ', '#', ' ', ' ', ' '},
                {'#', ' ', '#', ' ', ' ', ' ', '#', '#', '#', ' '},
                {'#', ' ', '*', ' ', '#', ' ', '#', ' ', ' ', ' '},
                {'#', ' ', '#', ' ', ' ', ' ', '#', ' ', '#', '#'},
                {'#', ' ', ' ', ' ', '#', ' ', ' ', ' ', ' ', ' '},
                {'#', ' ', '#', ' ', '*', ' ', ' ', '#', '#', '#'},
                {'#', '#', '#', '#', '#', ' ', '#', ' ', ' ', ' '},
                {'#', '#', ' ', ' ', ' ', ' ', ' ', ' ', '#', '#'}
            }
    };

    private boolean[][] enemyPositions = new boolean[mazeSize][mazeSize];

    public TurtleMazeGame() {
        setTitle("Turtle Maze Game - Level " + (currentLevel + 1));
        setSize(mazeSize * cellSize, mazeSize * cellSize);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
        setLocationRelativeTo(null);
        setResizable(false);
        resetTurtlePosition();
        initEnemyPositions();
    }

    private void initEnemyPositions() {
        // Initialize enemy positions for the last level (Level 3)
        char[][] lastLevel = levels[2];
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                if (lastLevel[i][j] == '*') {
                    enemyPositions[i][j] = true;
                } else {
                    enemyPositions[i][j] = false;
                }
            }
        }
    }

    private void resetTurtlePosition() {
        char[][] currentMaze = levels[currentLevel];
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                if (currentMaze[i][j] == 'S') {
                    turtleX = j;
                    turtleY = i;
                    return;
                }
            }
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        char[][] currentMaze = levels[currentLevel];

        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                if (currentMaze[i][j] == '#') {
                    g.setColor(Color.BLACK);
                    g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                } else if (currentMaze[i][j] == 'E') {
                    g.setColor(Color.GREEN);
                    g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                } else if (enemyPositions[i][j]) {
                    g.setColor(Color.BLUE);
                    g.fillOval(j * cellSize, i * cellSize, cellSize, cellSize);
                }
            }
        }

        g.setColor(Color.RED);
        g.fillOval(turtleX * cellSize, turtleY * cellSize, cellSize, cellSize);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        int newX = turtleX;
        int newY = turtleY;

        if (keyCode == KeyEvent.VK_UP) {
            newY--;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            newY++;
        } else if (keyCode == KeyEvent.VK_LEFT) {
            newX--;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            newX++;
        }

        if (isValidMove(newX, newY)) {
            turtleX = newX;
            turtleY = newY;
            repaint();

            if (levels[currentLevel][turtleY][turtleX] == 'E') {
                JOptionPane.showMessageDialog(this, "Congratulations! You've reached the exit of Level " + (currentLevel + 1));
                if (currentLevel < levels.length - 1) {
                    currentLevel++;
                    setTitle("Turtle Maze Game - Level " + (currentLevel + 1));
                    resetTurtlePosition();
                } else {
                    JOptionPane.showMessageDialog(this, "You've completed all levels!");
                    System.exit(0);
                }
            }

            // Check if turtle encounters an enemy
            if (enemyPositions[turtleY][turtleX]) {
                JOptionPane.showMessageDialog(this, "You were caught by an enemy! Try again.");
                resetTurtlePosition();
                repaint();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private boolean isValidMove(int x, int y) {
        char[][] currentMaze = levels[currentLevel];
        return x >= 0 && x < mazeSize && y >= 0 && y < mazeSize && currentMaze[y][x] != '#';
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TurtleMazeGame game = new TurtleMazeGame();
            game.setVisible(true);
        });
    }
}
