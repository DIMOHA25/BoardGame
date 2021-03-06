package normandyPack.cards;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;
import normandyPack.bots.*;

public class CardSquadLeader extends Card {
    public CardSquadLeader(String name, int squad, CardGroup cardGroup) {
        this.name = name;
        this.squad = squad;
        this.initiative = 7;
        this.cardGroup = cardGroup;
        this.team = (this.cardGroup).getTeam();
    }

    public void pickAction(Bot bot) {
        int choice;
        boolean success;

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
            System.out.println("2 to bolster(2,squad),");
            System.out.println("3 to inspire(1,squad)");

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
                    success = this.bolster(2,this.squad,bot);
                    if (success) this.playCard();
                    break;
                case (3):
                    success = this.inspire(1,bot);
                    if (success) this.playCard();
                    break;
            }
        } else {
            String choices = bot.getOutput();
            boolean willHunker = Integer.parseInt(choices.substring(35,36)) == 1;
            boolean willBolster = Integer.parseInt(choices.substring(47,48)) == 1;
            boolean willInspire = Integer.parseInt(choices.substring(48,49)) == 1;

            if (willHunker) { 
                this.hunkerDown();
                System.out.println( bot.getName() + ": successfully hunkered down" );
                return;
            }
            if (willBolster) {
                success = this.bolster(2,this.squad,bot);
                System.out.println( bot.getName() + ": tried bolstering" );
                if (success) {
                    this.playCard();
                    System.out.println( bot.getName() + ": success" );
                } else {
                    System.out.println( bot.getName() + ": failure" );
                    //tut nado dat pizdi botu 
                }
                return;
            }
            if (willInspire) {
                success = this.inspire(1,bot);
                System.out.println( bot.getName() + ": tried inspiring" );
                if (success) {
                    this.playCard();
                    System.out.println( bot.getName() + ": success" );
                }
                else {
                    System.out.println( bot.getName() + ": failure" );
                    //tut nado dat pizdi botu 
                }
                return;
            }
            System.out.println( bot.getName() + ": was too dumb to pick an action" );

            //tut nado dat pizdi botu 
        }
    }
}
