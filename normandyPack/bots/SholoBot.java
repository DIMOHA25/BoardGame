package normandyPack.bots;
import java.lang.Math;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;
import normandyPack.bots.*;

public class SholoBot extends Bot {
    public SholoBot(int team, Game game) {
        this.team = team;
        this.game = game;
        this.name = "Timofey";
    }

    public void formulateInput() {
        this.calculateOutput();
    }
    protected void calculateOutput() {
        this.output = "";
        for ( int i = 1; i <= 17; i++ ) {
            this.output += (int)(Math.random()*2);
        }
        this.output += (int)(Math.random()*101) / 100;
        this.output += (int)(Math.random()*101) / 100;
        for ( int i = 20; i <= 35; i++ ) {
            this.output += (int)(Math.random()*2);
        }
        this.output += "0";
        for ( int i = 37; i <= 66; i++ ) {
            this.output += (int)(Math.random()*2);
        }
        this.output += (int)(Math.random()*7);
        this.output += (int)(Math.random()*8);
    }
}
