package Game;

import Objects.Bonus;
import Objects.GhostEntity;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GameCell extends JLabel {
    private Color color;
    private boolean hasFood = false;
    private boolean hasBonus = false;
    private Bonus bonus;
    private boolean hasPacman = false;
    private boolean hasEnemy = false;
    private GhostEntity enemy;
    private boolean isWall = false;
    private static int counter = 0;
    private final int id;
    private Border cellBorder;

    // private Bonus bonus;
    public GameCell() {
        super();
        id = counter++;
        cellBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void hasFood(boolean value) {
        this.hasFood = value;
    }

    public boolean hasFood() {
        return this.hasFood;
    }

    public void hasPacman(boolean value) {
        this.hasPacman = value;
    }

    public boolean hasPacman() {
        return hasPacman;
    }


    public void hasEnemy(boolean hasEnemy) {
        this.hasEnemy = hasEnemy;
    }

    public boolean hasEnemy() {
        return hasEnemy;
    }

    public void setWall(boolean isWall) {
        this.isWall = isWall;
    }

    public boolean isWall() {
        return isWall;
    }

    public int getId() {
        return id;
    }

    public void setCellBorder(Border cellBorder) {
        this.cellBorder = cellBorder;
    }

    public Border getCellBorder() {
        return cellBorder;
    }

    public boolean hasBonus() {
        return hasBonus;
    }

    public void hasBonus(boolean hasBonus) {
        this.hasBonus = hasBonus;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    public void setEnemy(GhostEntity enemy) {
        this.enemy = enemy;
    }

    public GhostEntity getEnemy() {
        return enemy;
    }
}
