package Menu;

import Engine.Core;
import Engine.MenuListener;
import Engine.MusicPlayer;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public final class MenuFrame extends JFrame {
    private static final int MENU_WIDTH, MENU_HEIGHT;
    private final MenuLayeredPanel menuPanel;
    private final MenuListener menuListener;
    private final File backgroundMusic;
    private final JPanel upperPanel;
    private final JPanel leftPanel;
    private final JPanel rightPanel;
    private final JPanel downPanel;
    private final MusicPlayer backgroundPlayer;

    static {
        MENU_WIDTH = 458;
        MENU_HEIGHT = 600;
    }

    public MenuFrame()
            throws FontFormatException, IOException, LineUnavailableException, UnsupportedAudioFileException {
        backgroundMusic = new File("src/sounds/theme.wav");
        backgroundPlayer = new MusicPlayer(backgroundMusic);
        backgroundPlayer.start();

        // Set the volume if u would like to
        backgroundPlayer.setVolume(0f);

        // Panels
        upperPanel = new JPanel();
        upperPanel.setPreferredSize(new Dimension(1, 50));
        upperPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        upperPanel.setBorder(new EmptyBorder(new Insets(8, 16, 0, 8)));
        leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(15, 1));
        rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(15, 1));
        downPanel = new JPanel();
        Color panelColor = Color.white;
        downPanel.setPreferredSize(new Dimension(1, 50));
        upperPanel.setBackground(panelColor);
        leftPanel.setBackground(panelColor);
        rightPanel.setBackground(panelColor);
        downPanel.setBackground(panelColor);


//        System.out.println("This " + this);
        menuListener = new MenuListener(this);
        menuPanel = new MenuLayeredPanel(this);
        menuPanel.setPreferredSize(new Dimension(MENU_WIDTH, MENU_HEIGHT));
        menuListener.setLayeredPanel(menuPanel);
//        System.out.println("Menu listener " + menuListener);
        this.setLayout(new BorderLayout());
        this.getContentPane().add(upperPanel, BorderLayout.NORTH);
        this.getContentPane().add(leftPanel, BorderLayout.WEST);
        this.getContentPane().add(menuPanel, BorderLayout.CENTER);
        this.getContentPane().add(rightPanel, BorderLayout.EAST);
        this.getContentPane().add(downPanel, BorderLayout.SOUTH);
        this.setTitle("Menu");
        this.setName("Menu");
        this.setIconImage(Core.getIcon().getImage());
        this.setPreferredSize(new Dimension(MENU_WIDTH, MENU_HEIGHT));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // this.setResizable(false);
        this.setFocusable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.validate();
        this.setVisible(true);
        JRootPane rootPane = getRootPane();
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, "quit");
        rootPane.getActionMap().put("quit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getMenuPanel().getLastLayer() != null) {
                    getMenuPanel().addButtonsPanel();
                    getMenuPanel().removeLastLayer();
                }
            }
        });
//        System.out.println("Menu Frame created!");
    }

    public MenuLayeredPanel getMenuPanel() {
        return menuPanel;
    }

    public static int getMenuWidth() {
        return MENU_WIDTH;
    }

    public static int getMenuHeight() {
        return MENU_HEIGHT;
    }

    public MenuListener getMenuListener() {
        return menuListener;
    }

    public MusicPlayer getBackgroundPlayer() {
        return backgroundPlayer;
    }
}
