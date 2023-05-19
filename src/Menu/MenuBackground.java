package Menu;

import javax.swing.*;
import java.awt.*;

public class MenuBackground extends JPanel {
    private static final int BG_WIDTH, BG_HEIGHT;
    private static Image background;

    static {
        BG_WIDTH = MenuFrame.getMenuWidth();
        BG_HEIGHT = MenuFrame.getMenuHeight();
    }

    public MenuBackground() {
        background = new ImageIcon("src/img/background.png").getImage();
        this.setBounds(0, 0, BG_WIDTH,
                BG_HEIGHT);
        this.setFocusable(false);

        // System.out.println("Background initialized!");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

    }
}
