package Engine;

import Game.GameFrame;
import Menu.MenuButtonsPanel;
import Menu.MenuFrame;
import Menu.MenuLayeredPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuListener implements ActionListener {
    private final MenuFrame frame;

    private MenuLayeredPanel layeredPanel;
    private MenuButtonsPanel buttonsPanel;

    public MenuListener(MenuFrame frame) {
        this.frame = frame;

//        System.out.println("Main listener created!");
    }

    @Override
    public void actionPerformed(ActionEvent e) { // New Game
        if (e.getSource() == buttonsPanel.getNewGame()) {
            frame.getMenuPanel().addPanel(buttonsPanel.getNewGamePanel());
            frame.getMenuPanel().removeButtonsPanel();
            frame.getMenuPanel().validate();
            // System.out.println("New Game pressed!");


        }

        if (e.getSource() == buttonsPanel.getNewGamePanel().getSumbitButton()) { // New Game -> New GameFrame
            int rows = buttonsPanel.getNewGamePanel().getRowsBox().getSelectedIndex() + 10;
            int columns = buttonsPanel.getNewGamePanel().getColumnsBox().getSelectedIndex() + 10;
            frame.getBackgroundPlayer().stop();
            frame.dispose();
            try {
                new GameFrame(rows, columns, 25);
            } catch (Exception ignored) {
            }
            // System.out.println("Submit pressed!");
        }

        if (e.getSource() == buttonsPanel.getHighScores()) { // High Scores
            frame.getMenuPanel().addPanel(buttonsPanel.getHighScoresPanel());
            frame.getMenuPanel().removeButtonsPanel();
            frame.getMenuPanel().validate();
//            System.out.println("High scores pressed");

        }

        if (e.getSource() == buttonsPanel.getExit()) { // Exit
            // System.out.println("Exit pressed");
            System.exit(0);
        }

        if (e.getSource() == buttonsPanel.getHighScoresPanel().getUpperPanel().getBackButton() // Back button
                || e.getSource() == buttonsPanel.getNewGamePanel().getUpperPanel().getBackButton()) {
            frame.getMenuPanel().addButtonsPanel();
            frame.getMenuPanel().removeLastLayer();
            frame.getMenuPanel().validate();
//            System.out.println("Back pressed!");

        }

        if (e.getSource() == buttonsPanel.getHighScoresPanel().getResetButton()) { // reset
            buttonsPanel.getHighScoresPanel().reset();
            frame.getMenuPanel().removePanel(buttonsPanel.getHighScoresPanel());
            frame.getMenuPanel().addPanel(buttonsPanel.getHighScoresPanel());
//            System.out.println("Reset pressed");

        }

    }

    public void setLayeredPanel(MenuLayeredPanel layeredPanel) {
        this.layeredPanel = layeredPanel;
        this.buttonsPanel = layeredPanel.getButtonsPanel();
    }
}
