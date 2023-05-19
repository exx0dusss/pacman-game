package Menu;

import java.io.Serializable;

public class GameScore implements Serializable {
    private String playerName;
    private int result;

    public GameScore(String playerName, int result) {
        this.playerName = playerName;
        this.result = result;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getResult() {
        return this.result;
    }

    public void setResult(int result) {
        this.result = result;
    }

}
