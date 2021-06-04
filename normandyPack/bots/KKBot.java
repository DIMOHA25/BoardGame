package normandyPack.bots;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;
import normandyPack.bots.*;

public class KKBot extends Bot {
    public KKBot(int team, Game game) {
        this.team = team;
        this.game = game;
        this.name = "Vlad";
        this.output = "11111111111111111"+"11"+"1111111111111111"+"111111111111111"+"1111111111111111"+"99";
    }

    public void formulateInput() {
        //
    }
}
