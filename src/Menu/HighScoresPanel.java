package Menu;

import Engine.Core;
import Menu.HighScoresPanel.HighScoresScrollPane;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;

public class HighScoresPanel extends JPanel {
    private static final int WIDTH, HEIGHT;
    private static JScrollPane scrollPane;
    private final MenuUpperPanel upperPanel;
    private final MenuButton resetButton;
    private static JList<GameScore> scoresList;
    private final MenuFrame parent;

    static {
        WIDTH = MenuFrame.getMenuWidth();
        HEIGHT = MenuFrame.getMenuHeight();
    }

    public HighScoresPanel(MenuFrame parent) {
        this.parent = parent;
        ArrayList<GameScore> gameScores = loadData("src/data/HighScores.ser");
        if (gameScores != null) {
            Collections.sort(gameScores, (a, b) -> b.getResult() - a.getResult());
        }
        assert gameScores != null;
        scoresList = new JList<GameScore>(gameScores.toArray(new GameScore[0]));
        scoresList.setCellRenderer(new GameScoreLabel());
        scoresList.setBackground(Color.BLACK);
        scoresList.setOpaque(false);
        upperPanel = new MenuUpperPanel(parent);

        resetButton = new MenuButton();
        resetButton.setText("Reset");
        resetButton.setPreferredSize(new Dimension(125, 50));
        resetButton.addActionListener(parent.getMenuListener());
        upperPanel.add(resetButton);
        scrollPane = new HighScoresScrollPane();
        scrollPane.setBackground(Color.BLACK);


        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Core.getBlueColor());
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        this.add(upperPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);


    }

    public MenuButton getResetButton() {
        return resetButton;
    }

    public ArrayList<GameScore> loadData(String path) {
        ArrayList<GameScore> gameScores = new ArrayList<GameScore>();

        try (FileInputStream fileIn = new FileInputStream(path)) {
            ObjectInputStream in = new ObjectInputStream(fileIn);
            gameScores = (ArrayList<GameScore>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException ignored) {
        }
        return gameScores;
    }

    public void resetData() {
        try {
            new FileWriter("src/data/HighScores.ser", false).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        scoresList = new JList<>();
        scoresList.setOpaque(false);
        this.remove(scrollPane);
        scrollPane = new HighScoresScrollPane();
        this.add(scrollPane);
        repaint();
    }

    class HighScoresScrollPane extends JScrollPane {
        public HighScoresScrollPane() {
            super(scoresList);
            this.setOpaque(false);
            this.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
            this.getVerticalScrollBar().setUnitIncrement(10);
            this.getVerticalScrollBar().setBlockIncrement(30);
            this.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
            JScrollBar scrollBar = this.getVerticalScrollBar();

            // Set the color of the scrollbar's slider
            scrollBar.setUI(new BasicScrollBarUI() {

                @Override
                protected void configureScrollBarColors() {
                    // Set the color of the track and thumb
                    this.thumbColor = new Color(15, 30, 100);
                    this.trackColor = Color.black;
                }

                @Override
                protected JButton createDecreaseButton(int orientation) {
                    return createZeroButton();
                }

                @Override
                protected JButton createIncreaseButton(int orientation) {
                    return createZeroButton();
                }

                private JButton createZeroButton() {
                    JButton button = new JButton();
                    button.setPreferredSize(new Dimension(0, 0));
                    button.setMinimumSize(new Dimension(0, 0));
                    button.setMaximumSize(new Dimension(0, 0));
                    return button;
                }
            });

            getViewport().setOpaque(false);
            setOpaque(false);
//            System.out.println("Scroll Pane created!");

        }

    }

    public static JList<GameScore> getScoresList() {
        return scoresList;
    }

    public MenuUpperPanel getUpperPanel() {
        return upperPanel;
    }
}

class GameScoreLabel extends JLabel implements ListCellRenderer<GameScore> {
    private Border margin;

    @Override
    public Component getListCellRendererComponent(JList<? extends GameScore> list, GameScore value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        margin = new EmptyBorder(0, 20, 0, 0);
        Border outer = new CompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 1, 1, MenuButton.getMainColor()),
                BorderFactory.createMatteBorder(0, 1, 1, 1, MenuButton.getExtraColor()));
        Border border = new CompoundBorder(outer, margin);
        setText(String.format("Name: %10s, Score: %3s",
                value.getPlayerName(), value.getResult()));
        setPreferredSize(new Dimension(HighScoresScrollPane.WIDTH, 60));
        setBorder(border);
        setBackground(Core.getBlueColor());
        setFont(Core.getMainFont());
        setForeground(Color.white);
        setVerticalAlignment(SwingConstants.CENTER);
        setOpaque(true);
        return this;
    }

}