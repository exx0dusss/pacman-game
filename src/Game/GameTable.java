package Game;

import Engine.MusicPlayer;
import Objects.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameTable extends JTable {
    private final GameAbstractTable gameAbstractTable;
    private final Color wallColor = Color.blue;
    private final Color pathColor = Color.black;
    private int score;
    private final PacmanEntity pacman;
    private final Food food;

    private int foodQty = 0;
    private final GhostEntity ghost1;
    private final GhostEntity ghost2;
    private final GhostEntity ghost3;
    private final GameCell[][] maze;
    private int rowHeight;
    private int columnWidth;
    private final ArrayList<Resizable> entityList;
    private final ArrayList<Thread> threadList;
    private final Thread batchUpdate;
    private final int monitorHeight;
    private final int initialPacmanSpeed = 150;
    private final int initialGhostSpeed = 100;
    private int ghostsSpeed = initialGhostSpeed;
    private int pacmanMoveDelay = initialPacmanSpeed;
    private boolean ghostsEatable = false;

    // Music
    private final File theme;
    private final MusicPlayer themePlayer;


    GameTable(int rows, int columns, int UNIT_SIZE) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        double ratio = rows > columns ? (double) rows / columns : (double) columns / rows;
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        monitorHeight = gd.getDisplayMode().getHeight() - 250;
        this.setPreferredSize(new Dimension(
                (int) (monitorHeight / (rows > columns ? ratio : 1)),
                (int) (monitorHeight / (columns > rows ? ratio : 1))
        ));


        gameAbstractTable = new GameAbstractTable(rows, columns);
        maze = gameAbstractTable.getMaze();

        score = 0;
        entityList = new ArrayList<Resizable>();
        threadList = new ArrayList<Thread>();
        ArrayList<GhostEntity> ghostList = new ArrayList<GhostEntity>();
        food = new Food(UNIT_SIZE, UNIT_SIZE);

        pacman = new PacmanEntity(1, 1, UNIT_SIZE, UNIT_SIZE, this);
        ghost1 = new GhostEntity(rows - 2, columns - 2, UNIT_SIZE, UNIT_SIZE, this);
        ghost2 = new GhostEntity(rows - 2, columns - 2, UNIT_SIZE, UNIT_SIZE, this);
        ghost3 = new GhostEntity(rows - 2, columns - 2, UNIT_SIZE, UNIT_SIZE, this);
        ghostList.add(ghost1);
        ghostList.add(ghost2);
        ghostList.add(ghost3);

        entityList.add(food);
        entityList.add(pacman);
        entityList.addAll(ghostList);
        for (GameCell[] gameCells : maze) {
            for (GameCell gameCell : gameCells) {
                if (gameCell.hasFood()) {
                    foodQty++;

                }
            }
        }

        threadList.add(new Thread(pacman));
        threadList.add(
                new Thread(() -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        pacman.checkCollision();
                        pacman.checkNextDirection();
                        pacman.checkCollision();
                        pacman.move();
                        try {
                            Thread.sleep(pacmanMoveDelay);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }


                })
        );
        threadList.add(new Thread(ghost1));
        threadList.add(new Thread(ghost2));
        threadList.add(new Thread(ghost3));

        for (GhostEntity ghost : ghostList) {
            threadList.add(
                    new Thread(() -> {
                        while (!Thread.currentThread().isInterrupted()) {
                            if (!pacman.areGhostsFrozen()) {
                                ghost.move();
                                try {
                                    Thread.sleep(ghostsSpeed);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }

                        }

                    })
            );
            threadList.add(
                    new Thread(() -> {
                        while (!Thread.currentThread().isInterrupted()) {
                            System.out.println("Running");
                            if (!pacman.areGhostsFrozen()) {
                                ghost.checkCollision();

                                try {
                                    Thread.sleep(1);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }

                        }

                    })
            );

        }

        for (GhostEntity ghost : ghostList) {
            threadList.add(
                    new Thread(() -> {
                        while (!Thread.currentThread().isInterrupted()) {
                            Random random = new Random();
                            int randomNumber = random.nextInt(4) + 1;
                            if (randomNumber == 4 && !maze[ghost.getRow()][ghost.getColumn()].hasBonus()) {
                                int bonus = random.nextInt(5) + 1;
                                maze[ghost.getRow()][ghost.getColumn()].hasBonus(true);
                                maze[ghost.getRow()][ghost.getColumn()].setBonus(new Bonus(UNIT_SIZE, UNIT_SIZE, bonus, this));
                            }
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }

                    })
            );

        }


        this.rowHeight = UNIT_SIZE;
        this.columnWidth = UNIT_SIZE;
        GameTableRenderer gameTableRenderer = new GameTableRenderer(this);
        this.setModel(gameAbstractTable);
        this.setCellSelectionEnabled(false);
        this.setIntercellSpacing(new Dimension(0, 0));

        this.setDefaultRenderer(Object.class, gameTableRenderer);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                rowHeight = getHeight() / rows;
                columnWidth = getWidth() / columns;
                setRowHeight(rowHeight);
                for (int i = 0; i < columns; i++) {
                    getColumnModel().getColumn(i).setPreferredWidth(columnWidth);
                }
                for (Resizable entity : entityList) {
                    entity.setWidth(columnWidth);
                    entity.setHeight(rowHeight);
                }


            }
        });

        setRowHeight(UNIT_SIZE);
        for (int i = 0; i < getColumnCount(); i++) {
            getColumnModel().getColumn(i).setPreferredWidth(UNIT_SIZE);
        }
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> pacman.setNextDirection(Direction.UP);
                    case KeyEvent.VK_DOWN -> pacman.setNextDirection(Direction.DOWN);
                    case KeyEvent.VK_LEFT -> pacman.setNextDirection(Direction.LEFT);
                    case KeyEvent.VK_RIGHT -> pacman.setNextDirection(Direction.RIGHT);
                }
            }
        });
        setFocusable(true);

        validate();

        // Repainting
        batchUpdate = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                repaint();
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        });


        for (Thread thread : threadList) {
            thread.start();
        }
        batchUpdate.start();

        // Music
        theme = new File("src/sounds/pacman_beginning.wav");
        themePlayer = new MusicPlayer(theme);
        themePlayer.start();
        themePlayer.setVolume(0.1f);

    }

    public GameAbstractTable getGameAbstractTable() {
        return gameAbstractTable;
    }

    public Color getWallColor() {
        return wallColor;
    }

    public Color getPathColor() {
        return pathColor;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public PacmanEntity getPacman() {
        return pacman;
    }

    public Food getFood() {
        return food;
    }

    public int getFoodQty() {
        return foodQty;
    }

    public void setFoodQty(int foodQty) {
        this.foodQty = foodQty;
    }

    public GhostEntity getGhost1() {
        return ghost1;
    }

    public GhostEntity getGhost2() {
        return ghost2;
    }

    public GhostEntity getGhost3() {
        return ghost3;
    }

    public GameCell[][] getMaze() {
        return maze;
    }


    public int getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    public ArrayList<Resizable> getEntityList() {
        return entityList;
    }

    public ArrayList<Thread> getThreadList() {
        return threadList;
    }

    public Thread getBatchUpdate() {
        return batchUpdate;
    }

    public int getMonitorHeight() {
        return monitorHeight;
    }

    public int getInitialPacmanSpeed() {
        return initialPacmanSpeed;
    }

    public int getInitialGhostSpeed() {
        return initialGhostSpeed;
    }

    public int getGhostsSpeed() {
        return ghostsSpeed;
    }

    public void setGhostsSpeed(int ghostsSpeed) {
        this.ghostsSpeed = ghostsSpeed;
    }

    public int getPacmanMoveDelay() {
        return pacmanMoveDelay;
    }

    public void setPacmanMoveDelay(int pacmanMoveDelay) {
        this.pacmanMoveDelay = pacmanMoveDelay;
    }

    public boolean areGhostsEatable() {
        return ghostsEatable;
    }

    public void setGhostsEatable(boolean ghostsEatable) {
        this.ghostsEatable = ghostsEatable;
    }

    public MusicPlayer getThemePlayer() {
        return themePlayer;
    }


}