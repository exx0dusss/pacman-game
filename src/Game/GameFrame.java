package Game;

import Engine.Core;
import Engine.GameListener;
import Engine.MusicPlayer;
import Engine.ResizableLayeredPane;
import Menu.MenuBackground;
import Menu.MenuButton;
import Menu.MenuFrame;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class GameFrame extends JFrame {
    private final GameTimer timer;
    private final JPanel upperPanel;
    private final JPanel leftPanel;
    private final JPanel rightPanel;
    private final JPanel downPanel;
    private final GameTable gameTable;
    private final Thread timeElapsed;
    private final ResizableLayeredPane layeredPane;
    private final GameFrame frame;
    private final JComponent rootPane;
    private ResultPanel resultPanel;
    private final GameListener gameListener;
    private int score = 0;
    private final int rows;
    private final int columns;
    private final int UNIT_SIZE;

    // Music
    private final File death;
    private final File win;
    private MusicPlayer soundPlayer;

    public GameFrame(int rows, int columns, int UNIT_SIZE) throws IOException, FontFormatException, UnsupportedAudioFileException, LineUnavailableException {
        this.rows = rows;
        this.columns = columns;
        this.UNIT_SIZE = UNIT_SIZE;

        // Music
        death = new File("src/sounds/pacman_death.wav");
        win = new File("src/sounds/pacman_intermission.wav");

        // Timer
        JLabel timerLabel = new JLabel();
        timerLabel.setForeground(Color.black);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setVerticalAlignment(SwingConstants.CENTER);
        timerLabel.setFont(Core.getMainFont());
        timer = new GameTimer(timerLabel);
        timeElapsed = new Thread(timer);


        // Table
        gameTable = new GameTable(rows, columns, UNIT_SIZE);
        int width = (int) gameTable.getPreferredSize().getWidth();
        int height = (int) gameTable.getPreferredSize().getHeight();

        // Panels
        upperPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                score = gameTable.getScore();
                g.setFont(Core.getMainFont());
                g.setColor(Color.black);
                g.drawString("Score: " + score, 16, 34);
            }
        };
        upperPanel.setPreferredSize(new Dimension(rows, 50));
        upperPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        upperPanel.setBorder(new EmptyBorder(new Insets(8, 16, 0, 8)));
        upperPanel.add(timerLabel);
        leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(15, height));
        rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(15, height));
        Image hpImage = new ImageIcon("src/img/hp.png").getImage();
        downPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setFont(Core.getMainFont());
                g.setColor(Color.black);
                g.drawString("Hp: ", 16, 34);
                for (int i = 0, step = 70; i < gameTable.getPacman().getHp(); i++, step += 25) {
                    g.setColor(Color.black);
//                    g.fillArc(step, 12, 25, 25, 45,
//                            360 - 2 * 45);
                    g.drawImage(hpImage, step, 12, 25, 25, null);


                }


            }
        };
        Color panelColor = Color.white;
        downPanel.setPreferredSize(new Dimension(width, 50));
        upperPanel.setBackground(panelColor);
        upperPanel.setFocusable(false);
        leftPanel.setBackground(panelColor);
        leftPanel.setFocusable(false);
        rightPanel.setBackground(panelColor);
        rightPanel.setFocusable(false);
        downPanel.setBackground(panelColor);
        downPanel.setFocusable(false);

        // Layered pane
        layeredPane = new ResizableLayeredPane();
        layeredPane.add(gameTable, Integer.valueOf(0));
        layeredPane.setBorder(BorderFactory.createEmptyBorder());
        layeredPane.setFocusable(false);

        // Frame
        this.setTitle("Pacman");
        this.setLayout(new BorderLayout());
        this.getContentPane().add(upperPanel, BorderLayout.NORTH);
        this.getContentPane().add(leftPanel, BorderLayout.WEST);
        this.getContentPane().add(layeredPane, BorderLayout.CENTER);
        this.getContentPane().add(rightPanel, BorderLayout.EAST);
        this.getContentPane().add(downPanel, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(Core.getIcon().getImage());
        this.setVisible(true);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setFocusable(false);
        gameListener = new GameListener(this);
        timeElapsed.start();
        frame = this;
        rootPane = this.getRootPane();

        Thread batchUpdate = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                upperPanel.repaint();
                downPanel.repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }

                // If game is finished
                if (gameTable.getPacman().getHp() <= 0 || gameTable.getPacman().getApplesEaten() == gameTable.getFoodQty()) {
                    try {
                        if (gameTable.getPacman().getHp() <= 0) {
                            soundPlayer = new MusicPlayer(death);
                            soundPlayer.start();
                        } else {
                            soundPlayer = new MusicPlayer(win);
                            soundPlayer.start();
                        }
                    } catch (Exception e) {
                        soundPlayer.stop();
                    }
                    score = gameTable.getScore();
                    downPanel.repaint();
                    stop();

                }
            }
            layeredPane.remove(gameTable);
            layeredPane.add(new MenuBackground(), Integer.valueOf(0));
            try {
                resultPanel = new ResultPanel();
                layeredPane.add(resultPanel, Integer.valueOf(1));
            } catch (Exception ignored) {
            }
            rootPane.repaint();
            rootPane.revalidate();

        });
        
        // Start updating
        batchUpdate.start();
//         CTRL + SHIFT + Q
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, "quit");
        rootPane.getActionMap().put("quit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
                try {
                    new MenuFrame();
                } catch (Exception ignored) {
                }
                dispose();
            }
        });

    }

    public void stop() {
        gameTable.getThemePlayer().stop();
        if (soundPlayer != null) {
            soundPlayer.stop();
        }
        gameTable.getBatchUpdate().interrupt();
        for (Thread thread : gameTable.getThreadList()) {
            thread.interrupt();
        }
        timeElapsed.interrupt();
        Thread.currentThread().interrupt();
    }

    public ResultPanel getResultPanel() {
        return resultPanel;
    }


    public GameTable getGameTable() {
        return gameTable;
    }

    public GameTimer getTimer() {
        return timer;
    }

    public JPanel getUpperPanel() {
        return upperPanel;
    }

    public JPanel getLeftPanel() {
        return leftPanel;
    }

    public JPanel getRightPanel() {
        return rightPanel;
    }

    public JPanel getDownPanel() {
        return downPanel;
    }

    public Thread getTimeElapsed() {
        return timeElapsed;
    }

    public GameFrame getFrame() {
        return frame;
    }

    public GameListener getGameListener() {
        return gameListener;
    }

    public int getScore() {
        return score;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getUNIT_SIZE() {
        return UNIT_SIZE;
    }

    public class ResultPanel extends JPanel {
        JLabel nicknameLabel;
        JTextField textField;
        MenuButton submitButton;
        MenuButton restartButton;
        MenuButton menuBackButton;
        MenuButton exitButton;

        public ResultPanel() throws IOException, FontFormatException {
            int height = gameTable.getMonitorHeight();
            int width = gameTable.getMonitorHeight();
            this.setPreferredSize(new Dimension(width, height));
            layeredPane.setPreferredSize(new Dimension(width, height));
            layeredPane.setSize(new Dimension(width, height));
            GridBagConstraints gbc = new GridBagConstraints();
            this.setLayout(new GridBagLayout());

            Font biggerFont = Font.createFont(Font.TRUETYPE_FONT,
                    new File("src/fonts/bitfont.ttf")).deriveFont(Font.BOLD, 25f);

            Color loseWinColor = gameTable.getPacman().getHp() <= 0 ? Color.red : Color.green;
            String loseWinText = gameTable.getPacman().getHp() <= 0 ? "Game Over!" : "You won!";
            JLabel loseWinLabel = new JLabel();
            Dimension gameButtonDimension = new Dimension(292, 80);

            loseWinLabel.setText(String.format("%s", loseWinText));
            loseWinLabel.setFont(biggerFont);
            loseWinLabel.setForeground(loseWinColor);
            loseWinLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel scoreLabel = new JLabel();
            scoreLabel.setText(String.format("Score: %3s", score));
            scoreLabel.setFont(biggerFont);
            scoreLabel.setForeground(Color.white);
            scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

            nicknameLabel = new JLabel();
            nicknameLabel.setText("Enter your nickname: ");
            nicknameLabel.setFont(biggerFont);
            nicknameLabel.setForeground(Color.white);
            nicknameLabel.setHorizontalAlignment(SwingConstants.CENTER);

            textField = new JTextField(16);
            textField.setPreferredSize(new Dimension(0, 75));
            textField.setBackground(Core.getBlueColor());
            textField.setFont(
                    Font.createFont(Font.TRUETYPE_FONT,
                            new File("src/fonts/bitfont.ttf")).deriveFont(Font.BOLD, 20f)
            );
            textField.setForeground(Color.white);
            textField.setDocument(new LimitDocument(10));
            textField.setHorizontalAlignment(SwingConstants.CENTER);
            textField.setCaretColor(new Color(255, 204, 0));
            textField.setBorder(BorderFactory.createEtchedBorder(MenuButton.getMainColor(), MenuButton.getExtraColor()));

            submitButton = new MenuButton();
            submitButton.setPreferredSize(new Dimension(73, 73));
            submitButton.setBackground(Color.white);
            submitButton.setText(">");
            submitButton.setHorizontalAlignment(SwingConstants.CENTER);
            submitButton.addActionListener(gameListener);

            restartButton = new MenuButton();
            restartButton.setText("Restart");
            restartButton.setPreferredSize(gameButtonDimension);
            restartButton.addActionListener(gameListener);

            menuBackButton = new MenuButton();
            menuBackButton.setText("Back to Menu");
            menuBackButton.setPreferredSize(gameButtonDimension);
            menuBackButton.addActionListener(gameListener);

            exitButton = new MenuButton();
            exitButton.setText("Exit");
            exitButton.setPreferredSize(gameButtonDimension);
            exitButton.addActionListener(gameListener);
            gbc.insets = new Insets(18, 40, 18, 0);
            this.add(loseWinLabel, gbc);

            gbc.gridy = 1;
            this.add(scoreLabel, gbc);

            gbc.insets = new Insets(18, 43, 18, 0);
            gbc.gridy = 2;
            this.add(nicknameLabel, gbc);

            gbc.gridy = 3;
            this.add(textField, gbc);

            gbc.insets = new Insets(18, -30, 18, 0);
            this.add(submitButton, gbc);
            gbc.insets = new Insets(18, 42, 18, 0);
            gbc.gridy = 4;
            this.add(restartButton, gbc);

            gbc.gridy = 5;
            this.add(menuBackButton, gbc);

            gbc.gridy = 6;
            this.add(exitButton, gbc);

            this.setFocusable(false);
            this.setOpaque(false);

            rootPane.repaint();
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

        }


        public JLabel getNicknameLabel() {
            return nicknameLabel;
        }

        public JTextField getTextField() {
            return textField;
        }

        public MenuButton getSubmitButton() {
            return submitButton;
        }

        public MenuButton getRestartButton() {
            return restartButton;
        }

        public MenuButton getMenuBackButton() {
            return menuBackButton;
        }

        public MenuButton getExitButton() {
            return exitButton;
        }
    }

    private static class LimitDocument extends PlainDocument {
        private final int limit;

        public LimitDocument(int limit) {
            super();
            this.limit = limit;

        }

        @Override
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null)
                return;
            if ((getLength() + str.length()) <= limit) {
                super.insertString(offset, str, attr);
            }
        }
    }


}
