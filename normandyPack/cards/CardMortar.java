package normandyPack.cards;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;
import normandyPack.bots.*;

public class CardMortar extends Card {
    public CardMortar(String name, int squad, CardGroup cardGroup) {
        this.name = name;
        this.squad = squad;
        this.initiative = 2;
        this.cardGroup = cardGroup;
        this.team = (this.cardGroup).getTeam();
    }

    public void pickAction(Bot bot) {
        int choice;
        boolean success = false, tokenExists, suppressed = false;
        Token token;

        token = ((this.cardGroup).getGame()).findToken(this.name, this.squad, this.team);
        tokenExists = token != null;
        if (tokenExists) suppressed = token.getSuppressed();

        if (bot == null) {
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
                System.out.println("3 to blast(1),");
                System.out.println("4 to target");
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
                    if (!suppressed) success = this.move(1,token,bot);
                    if (!suppressed && success) this.playCard();
                    if (!tokenExists && !success) token.deletDis();
                    break;
                case (3):
                    if (!tokenExists) token = this.placeToken();
                    if (!suppressed) success = this.blast(1,token);
                    if (!suppressed && success) this.playCard();
                    if (!tokenExists && !success) token.deletDis();
                    break;
                case (4):
                    if (!tokenExists) token = this.placeToken();
                    if (!suppressed) success = this.target(token,bot);
                    if (!suppressed && success) this.playCard();
                    if (!tokenExists && !success) token.deletDis();
                    break;
            }
        } else {
            String choices = bot.getOutput();
            boolean willHunker = Integer.parseInt(choices.substring(35,36)) == 1;
            boolean willMove = Integer.parseInt(choices.substring(36,37)) == 1;
            boolean willBlast = Integer.parseInt(choices.substring(42,43)) == 1;
            boolean willTarget = Integer.parseInt(choices.substring(43,44)) == 1;

            if ( willHunker ) { 
                this.hunkerDown();
                System.out.println( bot.getName() + ": successfully hunkered down" );
                return;
            }
            if ( willMove || willBlast || willTarget ) {
                if ( suppressed ) {
                    this.unsuppress(token);
                    this.playCard();
                    System.out.println( bot.getName() + ": successfully unsuppressed" );
                    return;
                }
                if (!tokenExists) token = this.placeToken();

                if (willMove) {
                    success = this.move(1,token,bot);
                    System.out.println( bot.getName() + ": tried moving" );
                } else if (willBlast) {
                    success = this.blast(1,token);
                    System.out.println( bot.getName() + ": tried blasting" );
                } else {
                    success = this.target(token,bot);
                    System.out.println( bot.getName() + ": tried targeting" );
                }

                if (!tokenExists && !success) token.deletDis();
                if (success) {
                    this.playCard();
                    System.out.println( bot.getName() + ": success" );
                }
                else {
                    System.out.println( bot.getName() + ": failure" );
                    //tut nado dat pizdi botu 
                }
            } else {
                System.out.println( bot.getName() + ": was too dumb to pick an action" );
                //tut nado dat pizdi botu 
            }
        }
    }
}
