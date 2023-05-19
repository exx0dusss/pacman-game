package Objects;

import Game.GameTable;

import java.awt.*;

public final class Bonus extends Food {
    private int state;
    private final boolean isEaten = true;
    private final GameTable table;

    public Bonus(int height, int width, int state, GameTable table) {
        super(height, width);
        this.table = table;
        setState(state);
        divisor = 2;

    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        this.width = table.getColumnWidth();
        this.height = table.getRowHeight();
        g.setColor(color);
        g.fillOval(x + width / 6, y - height / (divisor * 2), width / divisor, height / divisor);
    }

    public void setState(int state) {
        this.state = state;
        switch (state) {
            case 1 -> this.color = Color.orange;
            case 2 -> this.color = Color.green;
            case 3 -> this.color = Color.white;
            case 4 -> this.color = Color.red;
            case 5 -> this.color = Color.yellow;

        }
    }


    public int getState() {
        return state;
    }

    public boolean isEaten() {
        return isEaten;
    }
}
