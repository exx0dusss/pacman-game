package Objects;

import Game.GameTable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GhostEntity extends Entity implements Resizable {
    private Color color;
    private File eatableGhost;
    private File eatenGhost;
    private BufferedImage image;
    private int frameX;
    private int frameY;
    private int frameWidth;
    private int frameHeight;
    private BufferedImage spriteSheet;
    private int imgPadX;
    private int imgPadY;

    private boolean eatable = false;
    private boolean isEaten = false;

    public GhostEntity(int row, int column, int width, int height, GameTable table) {
        super(row, column, width, height, table);
        scale = 7;
        try {
            spriteSheet = ImageIO.read(new File("src/img/ghosts.png"));
            frameX = 190;
            frameY = 0;
            frameWidth = 160;
            frameHeight = 160;


        } catch (IOException e) {
            e.printStackTrace();
        }

        eatableGhost = new File("src/img/eatable.png");
        eatenGhost = new File("src/img/damaged.png");
        Random random = new Random();
        int randomNumber = random.nextInt(4) + 1;
        switch (randomNumber) {
            case 1 -> { // Red
                imgPadX = 0;
                imgPadY = 0;
            }
            case 2 -> { // Cyan
                imgPadX = 400;
                imgPadY = 0;
            }
            case 3 -> { // Pink
                imgPadX = 0;
                imgPadY = 380;
            }
            case 4 -> { // Orange
                imgPadX = 400;
                imgPadY = 380;
            }
        }
        maze[row][column].hasEnemy(true);
        maze[row][column].setEnemy(this);


    }

    @Override
    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {


        if (!table.areGhostsEatable()) {
            g.setColor(color);
            image = spriteSheet.getSubimage(frameX + imgPadX, frameY + imgPadY, frameWidth, frameHeight);

        } else {
            try {
                image = ImageIO.read(eatableGhost);
            } catch (IOException ignored) {
            }
            if (isEaten) {
                try {
                    image = ImageIO.read(eatenGhost);
                } catch (IOException ignored) {
                }
            }
        }


        g.drawImage(image, (x + scale / 2) - 2, y - (height / 2) + scale / 2, width - scale,
                height - scale, null);


    }

    @Override
    public void animate() {

    }


    @Override
    public void move() {
        maze[row][column].hasEnemy(false);
        boolean changeDirection = false;
        switch (direction) {
            case UP -> {
                try {
                    if (!maze[row - 1][column].isWall()) {
                        row -= 1;
                        angle = 90;
                        frameX = 190;
                        frameY = 0;

                    }
                } catch (Exception ignored) {
                    maze[row][column].hasPacman(false);
                    row = 0;

                }

            }
            case DOWN -> {
                try {
                    if (!maze[row + 1][column].isWall()) {
                        row += 1;
                        angle = 270;
                        frameX = 0;
                        frameY = 190;
                    }
                } catch (Exception ignored) {
                    maze[row][column].hasPacman(false);
                    row = maze.length - 1;

                }

            }
            case LEFT -> {
                try {
                    if (!maze[row][column - 1].isWall()) {
                        column -= 1;
                        angle = 180;
                        frameX = 190;
                        frameY = 190;

                    }

                } catch (Exception ignored) {
                    maze[row][column].hasPacman(false);
                    column = maze.length - 1;

                }

            }
            case RIGHT -> {
                try {
                    if (!maze[row][column + 1].isWall()) {
                        column += 1;
                        angle = 0;
                        frameX = 0;
                        frameY = 0;
                    }
                } catch (Exception ignored) {
                    maze[row][column].hasPacman(false);
                    column = 0;

                }

            }

        }

        maze[row][column].hasEnemy(true);
        maze[row][column].setEnemy(this);
        Random random = new Random();
        int randomNumber = random.nextInt(4) + 1;
        switch (randomNumber) {
            case 1 -> direction = Direction.UP;
            case 2 -> direction = Direction.DOWN;
            case 3 -> direction = Direction.LEFT;
            case 4 -> direction = Direction.RIGHT;
        }
    }

    @Override
    public void checkCollision() {
        if (maze[row][column].hasPacman() && !table.getPacman().isImmune()) {
            table.getPacman().setHp(table.getPacman().getHp() - 1);
//            System.out.println("Ghost hit Pacman!");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Preserve the interrupt status
            }
        }
    }


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            animate();
            try {
                Thread.sleep(animationDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Preserve the interrupt status
                break; // or return; depending on your code structure
            }
        }
    }

    public void setEatable(boolean eatable) {
        this.eatable = eatable;
    }

    public boolean isEaten() {
        return isEaten;
    }

    public void setEaten(boolean eaten) {
        isEaten = eaten;
    }

    public boolean isEatable() {
        return eatable;
    }


}
