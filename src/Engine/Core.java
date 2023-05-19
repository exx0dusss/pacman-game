package Engine;

import Game.GameFrame;
import Menu.MenuFrame;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public final class Core {
    private static final Color blueColor;
    private static Font mainFont;
    private static MenuFrame menuFrame;
    private static GameFrame gameFrame;
    private static final ImageIcon icon;

    static {
        blueColor = new Color(11, 9, 40);
        icon = new ImageIcon("src/img/logo.png");

    }

    public Core() throws FontFormatException, IOException, LineUnavailableException, UnsupportedAudioFileException {
        mainFont = Font.createFont(Font.TRUETYPE_FONT,
                new File("src/fonts/bitfont.ttf")).deriveFont(Font.BOLD, 16f);
        MusicPlayer.setGeneralVolume(0.7f);
//        MusicPlayer.setGeneralVolume(0f);
        createMenuFrame();
    }

    public static void createMenuFrame() {
        SwingUtilities.invokeLater(() -> {
            try {
                menuFrame = new MenuFrame();
            } catch (FontFormatException | IOException | LineUnavailableException | UnsupportedAudioFileException e) {
                System.out.println("Error creating menu frame in Core.java");
            }
        });

    }

    public static void createGameFrame(int rows, int columns, int UNIT_SIZE) {
        SwingUtilities.invokeLater(() -> {
            try {
                new GameFrame(rows, columns, UNIT_SIZE);
            } catch (Exception ignored) {
            }

        });
    }


    public static Color getBlueColor() {
        return blueColor;
    }

    public static Font getMainFont() {
        return mainFont;
    }

    public static ImageIcon getIcon() {
        return icon;
    }

}
