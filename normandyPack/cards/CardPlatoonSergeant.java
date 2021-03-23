package normandyPack.cards;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;

public class CardPlatoonSergeant extends Card {
    public CardPlatoonSergeant(String name, int squad, CardGroup cardGroup) {
        this.name = name;
        this.squad = squad;
        this.initiative = 9;
        this.cardGroup = cardGroup;
        this.team = (this.cardGroup).getTeam();
    }

    public void pickAction() {
        int choice;
        boolean success;

        System.out.print( "(" + Constants.teamName(this.team) + ", " + this.name );
        switch ( this.squad ) {
            case (Constants.NO_SQUAD):
                break;
            case (Constants.SQUAD_A):
                System.out.print(" A");
                break;
            case (Constants.SQUAD_B):
                System.out.print(" B");
                break;
            case (Constants.SQUAD_C):
                System.out.print(" C");
                break;
        }
        System.out.print( ") Enter:\n" );
        System.out.println("1 to hunker down,");
        System.out.println("2 to bolster(3),");
        System.out.println("3 to command(2)");

        while(true) {
            if ( this.scanner.hasNextInt() ) {
                choice = this.scanner.nextInt();
                break;
            } else {
                System.out.println("Not a number");
                this.scanner.nextLine();
            }
        }
        this.scanner.nextLine();//nuzhno(?)

        switch (choice) {
            case (1):
                this.hunkerDown();
                break;
            case (2):
                success = this.bolster(3);
                if (success) this.playCard();
                break;
            case (3):
                success = this.command(2);
                if (success) this.playCard();
                break;
        }
    }
}
