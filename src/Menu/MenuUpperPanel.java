package Menu;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public class MenuUpperPanel extends JPanel {
        private final static int WIDTH;
        private static final int HEIGHT;
        private MenuButton backButton;
        private MenuFrame parent;
        static {
                WIDTH = MenuFrame.getMenuWidth();
                HEIGHT = 50;
        }

        public MenuUpperPanel(MenuFrame parent) {
                this.parent = parent;
                backButton = new MenuButton();
                backButton.setText("Back");
                backButton.setPreferredSize(new Dimension(125, 50));
                backButton.addActionListener(parent.getMenuListener());
                this.add(backButton);

                this.setPreferredSize(new Dimension((int) WIDTH, HEIGHT));
                this.setBackground(Color.black);
                this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
                this.setBorder(
                                new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.yellow),
                                                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.red)));

        }

        public MenuButton getBackButton() {
                return backButton;
        }

}
