package Main;

import Engine.Core;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Core();
            } catch (FontFormatException | IOException | LineUnavailableException
                     | UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
        });

    }
}