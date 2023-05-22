package Engine;

import java.io.*;
import javax.sound.sampled.*;

public class MusicPlayer {
    private File file;
    private Clip clip;
    private float volume = 1f;
    private static float generalVolume = 1f;
    private AudioInputStream audioInputStream;
    private static boolean play = true;

    public MusicPlayer(File file) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        setFile(file);
    }

    public void setFile(File file) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        this.file = file;
        audioInputStream = AudioSystem.getAudioInputStream(file);
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);

    }

    public void start() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        if (play) {
            setVolume(generalVolume);
            if (volume == 0) {
                setVolume(volume);
            }
            clip.start();

        }
    }
    public boolean isPlaying() {
        return clip.isRunning();
    }
    
    public void stop() {
        clip.stop();
    }

    public Clip getClip() {
        return clip;
    }

    public void setVolume(float volume) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(volume));
        }
    }

    public static void setGeneralVolume(float generalVolume) {
        MusicPlayer.generalVolume = generalVolume;
    }

    public static void setPlay(boolean play) {
        MusicPlayer.play = play;
    }

}
