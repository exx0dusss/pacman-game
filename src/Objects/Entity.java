package Objects;

import Game.GameCell;
import Game.GameTable;

import javax.swing.*;

public abstract class Entity extends ImageIcon implements Runnable {

    // Time
    protected int animationDelay = 7;

    // Movement
    protected Direction direction = Direction.RIGHT;
    protected int angle = 0;
    
    // Size
    protected int width, height;
    protected int row;
    protected int column;
    protected int scale = 7;

    // Outer classes
    protected GameTable table;
    protected GameCell[][] maze;

    public Entity(int row, int column, int width, int height, GameTable table) {
        this.width = width;
        this.height = height;
        this.table = table;
        this.maze = table.getMaze();
        this.row = row;
        this.column = column;

    }

    public abstract void animate();

    public abstract void move();

    public abstract void checkCollision();


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;

    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;

    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }


}
