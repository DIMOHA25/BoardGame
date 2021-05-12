package normandyPack.game;
import java.util.Scanner;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;

public class Menu {
    public Menu() {
        //
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        String input;

        while(true) {
            System.out.println("Do you want to start a game? y/n");
            input = scanner.nextLine();
            if ( input.length() == 1 && input.charAt(0) == 'y' ) {
                System.out.println( "ok masta, let's kill da ho..." );
                Game game = new Game();
                game.start();
                game = null;
            } else {
                break;
            }
        }
    }
}
