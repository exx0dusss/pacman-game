package Game;

import javax.swing.*;

public class GameTimer implements Runnable {
    int minutes = 0;
    int seconds = 0;
    int hours = 0;
    int delay = 1000;
    JLabel label;

    GameTimer(JLabel label) {
        this.label = label;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            label.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            if (++seconds == 60) {
                seconds = 0;
                if (++minutes == 60) {
                    minutes = 0;
                    hours++;
                }
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Preserve the interrupt status
                break;
            }
        }

    }


    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getHours() {
        return hours;
    }
}
