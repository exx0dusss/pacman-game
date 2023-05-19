package Menu;

import Engine.ResizableLayeredPane;

import javax.swing.*;
import java.awt.*;

public class MenuLayeredPanel extends ResizableLayeredPane {
    private static final int PANEL_WIDTH, PANEL_HEIGHT;
    private final MenuButtonsPanel buttonsPanel;
    private JPanel lastLayer;
    private final MenuFrame parent;

    static {
        PANEL_WIDTH = MenuFrame.getMenuWidth();
        PANEL_HEIGHT = MenuFrame.getMenuHeight();
    }

    public MenuLayeredPanel(MenuFrame parent) {
        this.parent = parent;
//        System.out.println(parent.getMenuListener());
        Dimension dimension = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
        buttonsPanel = new MenuButtonsPanel(parent);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setName("Menu panel");
        this.add(new MenuBackground(), Integer.valueOf(0));
        this.add(buttonsPanel, Integer.valueOf(1));
        // System.out.println("Menu panel created!");
    }

    public void addPanel(Component component) {
        this.add(component, Integer.valueOf(3));
        lastLayer = (JPanel) component;
        // System.out.println("Added new layer! : " + 3);

    }

    public void removePanel(Component component) {
        this.remove(component);
    }

    public void removePanel(int index) {
        remove(getComponent(Integer.valueOf(index)));

    }

    public void removeLastLayer() {
        this.remove(lastLayer);
        lastLayer = null;

    }

    public void removeButtonsPanel() {
        this.remove(buttonsPanel);

    }

    public void addButtonsPanel() {
        this.add(buttonsPanel, Integer.valueOf(1));

    }

    public MenuButtonsPanel getButtonsPanel() {
        return buttonsPanel;
    }

    public JPanel getLastLayer() {
        return lastLayer;
    }
}
