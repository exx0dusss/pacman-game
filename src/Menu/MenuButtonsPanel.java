package Menu;

import javax.swing.*;
import java.awt.*;

public class MenuButtonsPanel extends JPanel {
    private static final int PANEL_WIDTH, PANEL_HEIGHT;
    private final MenuButton newGame;
    private final MenuButton highScores;
    private final MenuButton exit;

    private final NewGamePanel newGamePanel;
    private final HighScoresPanel highScoresPanel;

    static {
        PANEL_WIDTH = MenuFrame.getMenuWidth();
        PANEL_HEIGHT = MenuFrame.getMenuHeight();
    }

    public MenuButtonsPanel(MenuFrame parent) {

        newGamePanel = new NewGamePanel(parent);
        highScoresPanel = new HighScoresPanel(parent);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(18, 0, 18, 0);

        newGame = new MenuButton();
        newGame.setText("New Game");
        newGame.addActionListener(parent.getMenuListener());

        highScores = new MenuButton();
        highScores.setText("High Scores");
        highScores.addActionListener(parent.getMenuListener());

        exit = new MenuButton();
        exit.setText("Exit");
        exit.addActionListener(parent.getMenuListener());

        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        this.setLayout(new GridBagLayout());
        this.setOpaque(false);
        this.setFocusable(true);

        this.add(newGame, gbc);
        gbc.gridy = 1;
        this.add(highScores, gbc);
        gbc.gridy = 2;
        this.add(exit, gbc);

//        System.out.println("Menu buttons panel created!");

    }

    public MenuButton getNewGame() {
        return newGame;
    }

    public MenuButton getHighScores() {
        return highScores;
    }

    public MenuButton getExit() {
        return exit;
    }

    public NewGamePanel getNewGamePanel() {
        return newGamePanel;
    }

    public HighScoresPanel getHighScoresPanel() {
        return highScoresPanel;
    }

}
