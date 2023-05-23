package Game;

import Objects.Food;
import Objects.PacmanEntity;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class GameTableRenderer extends DefaultTableCellRenderer {
    private final PacmanEntity pacman;
    private final Food food;


    public GameTableRenderer(GameTable table) {
        this.pacman = table.getPacman();
        this.food = table.getFood();


    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        JLabel component = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                row,
                column);
        GameCell cell = (GameCell) value;
        int cellWidth = table.getColumnModel().getColumn(column).getWidth();
        int cellHeight = table.getRowHeight(row);
        Color color = cell.getColor();
        component.setBackground(Color.black);
        component.setFocusable(false);
        Border border = BorderFactory.createLineBorder(color);
        // Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
        component.setBorder(cell.getCellBorder());
        component.setText("");
        component.setIcon(null);

        if (cell.hasFood()) {
            component.setIcon(food);
        }
        if (cell.hasBonus()) {
            component.setIcon(cell.getBonus());
        }
        if (cell.hasPacman()) {
            component.setIcon(pacman);
        }
        if (cell.hasEnemy()) {
            component.setIcon(cell.getEnemy());
        }
        if (cell.isWall()) {
            component.setIcon(null);

        }

        return component;
    }

}
