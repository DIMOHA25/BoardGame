package normandyPack.cards;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;

public class CardRifleman extends Card {
    public CardRifleman(String name, int squad, CardGroup cardGroup) {
        this.name = name;
        this.squad = squad;
        this.initiative = 5;
        this.cardGroup = cardGroup;
        this.team = (this.cardGroup).getTeam();
    }

    public void pickAction() {
        int choice;
        boolean success = false, tokenExists, suppressed = false;
        Token token;

        token = ((this.cardGroup).getGame()).findToken(this.name, this.squad, this.team);
        tokenExists = token != null;
        if (tokenExists) suppressed = token.getSuppressed();

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
        if (suppressed) {
            System.out.println("2 to unsuppress");
        } else {
            System.out.println("2 to move(1),");
            System.out.println("3 to attack(1),");
            System.out.println("4 to control");
        }

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
                if (suppressed) this.unsuppress(token);
                if (suppressed) this.playCard();
                if (!tokenExists) token = this.placeToken();
                if (!suppressed) success = this.move(1,token);
                if (!suppressed && success) this.playCard();
                if (!tokenExists && !success) token.deletDis();
                break;
            case (3):
                if (!tokenExists) token = this.placeToken();
                if (!suppressed) success = this.attack(1,token);
                if (!suppressed && success) this.playCard();
                if (!tokenExists && !success) token.deletDis();
                break;
            case (4):
                if (!tokenExists) token = this.placeToken();
                if (!suppressed) success = this.control(token);
                if (!suppressed && success) this.playCard();
                if (!tokenExists && !success) token.deletDis();
                break;
        }
    }
}
