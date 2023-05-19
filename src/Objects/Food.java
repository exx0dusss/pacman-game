package Objects;

import javax.swing.*;
import java.awt.*;

public class Food extends ImageIcon implements Resizable {
    protected int width, height;
    protected int marginLeft, marginTop;
    protected Color color = new Color(255, 0, 128);
    protected int divisor = 4;

    public Food(int height, int width) {
        this.width = width;
        this.height = height;

    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);
        g.fillRect(x + width / divisor, y - height / (divisor * 2), width / divisor, height / divisor);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;

    }

    public void setHeight(int height) {
        this.height = height;

    }

}
