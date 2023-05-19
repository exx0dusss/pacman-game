package Menu;

import Engine.Core;
import Engine.MusicPlayer;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class MenuButton extends JButton {
    private static final Color mainColor;
    private static final Color extraColor;
    private static final Color mainPressedColor;
    private static final Color extraPressedColor;
    private Border staticBorder;
    private Border hoveredBorder;
    private Border pressedBorder;
    private Border marginBorder;

    static {
        mainColor = Color.red;
        extraColor = Color.yellow;
        mainPressedColor = Color.white;
        extraPressedColor = Color.black;
    }

    public MenuButton() {
        marginBorder = new EmptyBorder(0, 10, 0, 0);
        staticBorder = BorderFactory.createEtchedBorder(mainColor, extraColor);
        hoveredBorder = BorderFactory.createEtchedBorder(extraColor, mainColor);
        pressedBorder = BorderFactory.createEtchedBorder(mainPressedColor, mainPressedColor);

        this.setFocusable(false);
        this.setPreferredSize(new Dimension(250, 100));
        this.setFont(Core.getMainFont());
        this.setVerticalAlignment(SwingConstants.CENTER);
        this.setHorizontalAlignment(SwingConstants.LEFT);
        this.setForeground(Color.white);
        this.setBackground(Core.getBlueColor());
        this.setOpaque(true);
        this.setBorder(new CompoundBorder(staticBorder, marginBorder));
        this.setUI(new MenuButtonUI());
        this.addMouseListener(new MouseListener() {

            @Override
            public void mousePressed(MouseEvent e) {
//                System.out.println("Mouse pressed");
                try {
                    new MusicPlayer(new File("src/sounds/release.wav")).start();
                } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
                    System.out.println("Error reading press sound!");
                }
                ((MenuButton) e.getSource()).setPressed();

            }

            @Override
            public void mouseReleased(MouseEvent e) {
//                System.out.println("Mouse released");
                try {
                    new MusicPlayer(new File("src/sounds/release.wav")).start();
                } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
                    System.out.println("Error reading release sound!");
                }
                ((MenuButton) e.getSource()).setStatic();

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // System.out.println("Mouse entered");
                try {
                    new MusicPlayer(new File("src/sounds/hover.wav")).start();
                } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
                    System.out.println("Error reading hover sound!");
                }
                ((MenuButton) e.getSource()).setHovered();

            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((MenuButton) e.getSource()).setStatic();

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // IGNORE
            }
        });
    }

    public void setStatic() {
        this.setBorder(new CompoundBorder(staticBorder, marginBorder));
    }

    public void setHovered() {
        this.setBorder(new CompoundBorder(hoveredBorder, marginBorder));
    }

    public void setPressed() {
        this.setBorder(new CompoundBorder(pressedBorder, marginBorder));
    }

    public Border getStaticBorder() {
        return this.staticBorder;
    }

    public void setStaticBorder(Border staticBorder) {
        this.staticBorder = staticBorder;
        this.setBorder(new CompoundBorder(staticBorder, marginBorder));
    }

    public Border getHoveredBorder() {
        return this.hoveredBorder;
    }

    public void setHoveredBorder(Border hoveredBorder) {
        this.hoveredBorder = hoveredBorder;
    }

    public Border getPressedBorder() {
        return this.pressedBorder;
    }

    public void setPressedBorder(Border pressedBorder) {
        this.pressedBorder = pressedBorder;
    }

    public Border getMarginBorder() {
        return this.marginBorder;
    }

    public void setMarginBorder(Border marginBorder) {
        this.marginBorder = marginBorder;
    }

    public static Color getMainColor() {
        return mainColor;
    }

    public static Color getExtraColor() {
        return extraColor;
    }

    public static Color getMainPressedColor() {
        return mainPressedColor;
    }

    public static Color getExtraPressedColor() {
        return extraPressedColor;
    }

    static class MenuButtonUI extends BasicButtonUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            AbstractButton button = (AbstractButton) c;
            ButtonModel model = button.getModel();
            Color bg = button.getBackground();

            if (model.isPressed()) {
                bg = new Color(7, 0, 30);
            } else {
                bg = Core.getBlueColor();
            }
            button.setBackground(bg);
            super.paint(g, button);
        }
    }
}
