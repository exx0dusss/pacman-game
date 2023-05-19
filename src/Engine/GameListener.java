package Engine;

import Game.GameFrame;
import Menu.GameScore;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.io.*;
import java.util.ArrayList;

public class GameListener extends KeyAdapter implements ActionListener {
    private final GameFrame gameFrame;

    public GameListener(GameFrame frame) {
        this.gameFrame = frame;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == gameFrame.getResultPanel().getSubmitButton()) {
            String filename = "src/data/HighScores.ser";
            ArrayList<GameScore> gameScores = new ArrayList<GameScore>();

            // Open serializable file
            try (FileInputStream fileIn = new FileInputStream(filename)) {
                ObjectInputStream in = new ObjectInputStream(fileIn);
                gameScores = (ArrayList<GameScore>) in.readObject();
                in.close();
            } catch (IOException | ClassNotFoundException ignored) {
            }

            // Add GameScore to the Object
            String nickname = gameFrame.getResultPanel().getTextField().getText();
            int score = gameFrame.getGameTable().getScore();
            gameScores.add(new GameScore(nickname, score));

            // Write to serializable file
            try (FileOutputStream fileOut = new FileOutputStream(filename)) {
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(gameScores);
                out.close();
            } catch (IOException ignored) {
            }
            gameFrame.getResultPanel().getSubmitButton().setEnabled(false);
        }

        if (e.getSource() == gameFrame.getResultPanel().getRestartButton()) {
            int rows = gameFrame.getRows();
            int columns = gameFrame.getColumns();
            int UNIT_SIZE = gameFrame.getUNIT_SIZE();
            try {
                new GameFrame(rows, columns, UNIT_SIZE);
            } catch (Exception ignored) {
            }
            gameFrame.dispose();
        }

        if (e.getSource() == gameFrame.getResultPanel().getMenuBackButton()) {
            gameFrame.dispose();
            Core.createMenuFrame();
        }


        if (e.getSource() == gameFrame.getResultPanel().getExitButton()) {
            System.exit(0);
        }

    }
}
