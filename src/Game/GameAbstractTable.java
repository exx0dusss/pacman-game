package Game;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.Random;

public final class GameAbstractTable extends AbstractTableModel {
    private final static Color pathColor = Color.black;
    private final static Color wallColor = Color.blue;
    private final GameCell[][] maze;

    GameAbstractTable(int rows, int columns) {
        this.maze = Maze.generate(rows, columns);

    }

    public GameCell[][] getMaze() {
        return maze;
    }

    @Override
    public int getRowCount() {
        return maze.length;
    }

    @Override
    public int getColumnCount() {
        return maze[0].length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return maze[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Color.class;
    }

    public static Color getWallColor() {
        return wallColor;
    }

    public static Color getPathColor() {
        return pathColor;
    }

    static final class Maze {

        public static GameCell[][] generate(int rows, int columns) {

            // Algorithm for creating maze
            GameCell[][] maze = new GameCell[rows][columns];
            int times = Math.min(rows, columns) / 2;

            // row borders
            for (int i = 0; i < rows; i++) {
                GameCell wall = new GameCell();
                wall.setColor(wallColor);
                wall.setWall(true);
                maze[i][0] = wall;
                maze[i][columns - 1] = wall;
            }

            // column borders
            for (int j = 0; j < columns; j++) {
                GameCell wall = new GameCell();
                wall.setColor(wallColor);
                wall.setWall(true);
                maze[0][j] = wall;
                maze[rows - 1][j] = wall;
            }


            // inner borders
            Random random = new Random();
            for (int i = 1; i < rows - 1; i++) {
                for (int j = 1; j < columns - 1; j++) {
                    GameCell path = new GameCell();
                    path.setColor(pathColor);
                    path.hasFood(true);
                    boolean draw = false;
                    for (int l = 0; l <= times; l += 2) {
                        boolean square = (i == l && j >= l && j <= columns - l - 1)
                                || (j == l && j <= columns - l - 1 && i >= l && i <= rows - l - 1)
                                || (i == rows - l - 1 && j >= l && j <= columns - l - 1)
                                || (j == columns - l - 1 && j <= columns - l - 1 && i >= l && i <= rows - l - 1);
                        if (square) {
                            draw = true;
                            break;
                        }
                    }

                    int randomNumber = random.nextInt(5) + 1;
                    if (randomNumber == 2)
                        draw = false;
                    if (draw) {
                        GameCell wall = new GameCell();
                        wall.setColor(wallColor);
                        wall.setWall(true);
                        maze[i][j] = wall;
                    } else {
                        maze[i][j] = path;
                    }
                }
            }

            int centerRow = rows / 2;
            GameCell path = new GameCell();
            path.setColor(pathColor);
            path.hasFood(true);
            maze[centerRow][0] = path;
            GameCell path1 = new GameCell();
            path1.setColor(pathColor);
            path1.hasFood(true);
            maze[centerRow][columns - 1] = path1;


            // Setting solid borders so the blocks look like a one figure
            for (int row = 1; row < maze.length; row++) {
                for (int column = 1; column < maze[row].length; column++) {
                    if (maze[row][column].isWall()) {
                        Border newBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);

                        if (column < columns - 1 && !maze[row][column + 1].isWall()) {
                            newBorder = BorderFactory.createCompoundBorder(
                                    newBorder,
                                    BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLUE)
                            );
                        }

                        if (!maze[row][column - 1].isWall()) {
                            newBorder = BorderFactory.createCompoundBorder(
                                    newBorder,
                                    BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLUE)
                            );

                        }
                        if (row < rows - 1 && !maze[row + 1][column].isWall()) {
                            newBorder = BorderFactory.createCompoundBorder(
                                    newBorder,
                                    BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLUE)
                            );

                        }

                        if (!maze[row - 1][column].isWall()) {
                            newBorder = BorderFactory.createCompoundBorder(
                                    newBorder,
                                    BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLUE)
                            );

                        }
                        maze[row][column].setCellBorder(newBorder);


                    }
                }
            }

            for (int i = 1; i < columns - 1; i++) {
                if (maze[0][i].isWall()) {
                    GameCell wall = maze[0][i];
                    wall.setCellBorder(
                            BorderFactory.createCompoundBorder(
                                    wall.getCellBorder(),
                                    BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLUE)
                            )
                    );
                    maze[0][i] = wall;
                }
            }

            for (int i = 1; i < rows - 1; i++) {
                if (maze[i][0].isWall()) {
                    GameCell wall = maze[i][0];
                    wall.setCellBorder(
                            BorderFactory.createCompoundBorder(
                                    wall.getCellBorder(),
                                    BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLUE)
                            )
                    );

                    maze[i][0] = wall;
                }
            }


            return maze;
        }


    }
}
