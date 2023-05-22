package Objects;

import Engine.MusicPlayer;
import Game.GameTable;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public final class PacmanEntity extends Entity implements Resizable {
    // Sounds
    private final File eatFruit;
    private final File eatGhost;
    private final File waka;

    private MusicPlayer soundPlayer;
    private MusicPlayer wakaPlayer;
    // Animation
    private int arcAngle = 0;
    private int animationDirection = 1;

    // Movement
    private Direction nextDirection = Direction.RIGHT;

    // Condition
    private int hp = 3;
    private int applesEaten = 0;

    // Bonuses
    private boolean isImmune = false;
    private boolean scoreX2 = false;
    private boolean areGhostsFrozen = false;

    public PacmanEntity(int row, int column, int width, int height, GameTable table) {
        super(row, column, width, height, table);
        scale = 7;
        maze[row][column].hasPacman(true);

        eatFruit = new File("src/sounds/pacman_eatfruit.wav");
        eatGhost = new File("src/sounds/pacman_eatghost.wav");
        waka = new File("src/sounds/waka.wav");
        try {
            wakaPlayer = new MusicPlayer(waka);
        } catch (Exception ignored) {
        }

    }

    @Override
    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(new Color(254, 204, 0));
        g.fillArc((x + scale / 2) - 2, y - (height / 2) + scale / 2, width - scale,
                height - scale, angle + arcAngle, 360 - 2 * arcAngle);
    }

    @Override
    public void animate() {
        arcAngle += animationDirection * 5;
        if (arcAngle == 70 || arcAngle == -15)
            animationDirection *= -1;
    }


    public void checkNextDirection() {
        switch (nextDirection) {
            case UP -> {
                try {
                    if (!maze[row - 1][column].isWall()) {
                        direction = nextDirection;
                    }
                } catch (Exception ignored) {
                    maze[row][column].hasPacman(false);
                    row = 0;
                }

                break;
            }
            case DOWN -> {
                try {
                    if (!maze[row + 1][column].isWall()) {
                        direction = nextDirection;
                    }
                } catch (Exception ignored) {
                    maze[row][column].hasPacman(false);
                    row = table.getRowCount() - 1;
                }

                break;
            }
            case LEFT -> {
                try {
                    if (!maze[row][column - 1].isWall()) {
                        direction = nextDirection;
                    }
                } catch (Exception ignored) {
                    maze[row][column].hasPacman(false);
                    column = table.getColumnCount() - 1;

                }

                break;
            }
            case RIGHT -> {
                try {
                    if (!maze[row][column + 1].isWall()) {
                        direction = nextDirection;
                    }
                } catch (Exception ignored) {
                    maze[row][column].hasPacman(false);
                    column = 0;


                }

                break;
            }

        }

    }

    @Override
    public void move() {
        maze[row][column].hasPacman(false);
        switch (direction) {
            case UP -> {
                try {
                    if (!maze[row - 1][column].isWall()) {
                        row -= 1;
                        angle = 90;
                    }
                } catch (Exception e) {
                    row = table.getRowCount() - 1;
                }
                break;
            }
            case DOWN -> {
                try {
                    if (!maze[row + 1][column].isWall()) {
                        row += 1;
                        angle = 270;
                    }
                } catch (Exception e) {
                    row = 0;
                }
                break;
            }
            case LEFT -> {
                try {
                    if (!maze[row][column - 1].isWall()) {
                        column -= 1;
                        angle = 180;
                    }
                } catch (Exception e) {
                    column = table.getColumnCount() - 1;
                }

                break;
            }
            case RIGHT -> {
                try {

                    if (!maze[row][column + 1].isWall()) {
                        column += 1;
                        angle = 0;
                    }
                } catch (Exception e) {
                    column = 0;
                }
                break;
            }
        }
        maze[row][column].hasPacman(true);


    }

    //1. +50% movement speed for 2 secs orange
    //2. Immune to ghosts for 2 secs green
    //3. Can eat ghosts for 3 secs white
    //4. Ghost freeze for 3 secs red
    //5. score x2 yellow
    @Override
    public void checkCollision() {

        if (maze[row][column].hasFood()) {
            if (!wakaPlayer.isPlaying()) {
                try {
                    wakaPlayer = new MusicPlayer(waka);
                    wakaPlayer.start();

                } catch (Exception ignored) {

                }
            }
            maze[row][column].hasFood(false);
            if (scoreX2) {
                table.setScore(table.getScore() + 2);
            } else {
                table.setScore(table.getScore() + 1);
            }
            applesEaten++;

        }

        if (maze[row][column].hasBonus()) {

            try {
                soundPlayer = new MusicPlayer(eatFruit);
                soundPlayer.start();

            } catch (Exception ignored) {

            }
            switch (maze[row][column].getBonus().getState()) {
                case 1 -> increaseSpeed(); // orange
                case 2 -> setImmune(); // green
                case 3 -> eatGhosts(); // white
                case 4 -> freezeGhosts(); // red
                case 5 -> scoreX2(); // yellow
            }
            maze[row][column].hasBonus(false);
            maze[row][column].setBonus(null);
        }

        if (table.areGhostsEatable() && maze[row][column].hasEnemy()) {
            try {
                soundPlayer = new MusicPlayer(eatGhost);
                soundPlayer.start();
            } catch (Exception ignored) {

            }
            GhostEntity ghost = maze[row][column].getEnemy();
            int ghostsDelay = table.getInitialGhostSpeed();
            setImmune();
            new Thread(() -> {
                ghost.setEaten(true);
                table.setGhostsSpeed(ghostsDelay / 3);
                for (int i = 1; i <= 5; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                Thread currentThread = Thread.currentThread();
                currentThread.interrupt();
                ghost.setEaten(false);

            }).start();
            table.setGhostsSpeed(ghostsDelay);
            table.setScore(table.getScore() + 20);
        }
    }

    private void increaseSpeed() {
        System.out.println("SPEED +50%!");
        int currentDelay = table.getInitialPacmanSpeed();
        new Thread(() -> {
            setImmune(true);
            table.setPacmanMoveDelay(currentDelay / 2);
            for (int i = 1; i <= 4; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
            }
            table.setPacmanMoveDelay(currentDelay);

            Thread currentThread = Thread.currentThread();
            currentThread.interrupt();
        }).start();

    }

    private void setImmune() {
        System.out.println("Immune to Ghosts!");
        new Thread(() -> {
            setImmune(true);
            for (int i = 1; i <= 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
            }
            setImmune(false);
            Thread currentThread = Thread.currentThread();
            currentThread.interrupt();
        }).start();
    }

    private void eatGhosts() {
        System.out.println("Can eat Ghosts!");
        int ghostDelay = table.getInitialGhostSpeed();
        new Thread(() -> {
            setImmune(true);
            table.setGhostsEatable(true);
            table.setGhostsSpeed(ghostDelay / 2);
            for (int i = 1; i <= 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
            }
            setImmune(false);
            table.setGhostsEatable(false);
            table.setGhostsSpeed(ghostDelay);
            Thread currentThread = Thread.currentThread();
            currentThread.interrupt();
        }).start();
    }

    private void freezeGhosts() {
        System.out.println("Freeze Ghosts!");
        new Thread(() -> {
            setGhostsFrozen(true);
            for (int i = 1; i <= 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
            }
            setGhostsFrozen(false);


            Thread currentThread = Thread.currentThread();
            currentThread.interrupt();
        }).start();
    }

    private void scoreX2() {
        System.out.println("Score x2!");
        new Thread(() -> {
            setScoreX2(true);
            for (int i = 1; i <= 6; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
            }
            setScoreX2(false);

            Thread currentThread = Thread.currentThread();
            currentThread.interrupt();
        }).start();


    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            animate();
            try {
                Thread.sleep(animationDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Preserve the interrupt status
                return; // or return; depending on your code structure
            }
        }
    }


    public Direction getNextDirection() {
        return nextDirection;
    }

    public void setNextDirection(Direction nextDirection) {
        this.nextDirection = nextDirection;
    }

    public void setHp(int hp) {
        this.hp = hp;

    }

    public int getHp() {
        return hp;
    }

    public int getApplesEaten() {
        return applesEaten;
    }

    public boolean isImmune() {
        return isImmune;
    }

    public void setImmune(boolean immune) {
        isImmune = immune;
    }

    public boolean isScoreX2() {
        return scoreX2;
    }

    public void setScoreX2(boolean scoreX2) {
        this.scoreX2 = scoreX2;
    }

    public boolean areGhostsFrozen() {
        return areGhostsFrozen;
    }

    public void setGhostsFrozen(boolean areGhostsFrozen) {
        this.areGhostsFrozen = areGhostsFrozen;
    }
}