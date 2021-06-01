package normandyPack.cards;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Math;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;
import normandyPack.bots.*;

public class CardGroup {
    Scanner scanner = new Scanner(System.in);
    //
    private int team;
    private ArrayList<Card> cards;
    private Game game;

    public CardGroup(int team, Game game) {
        this.team = team;
        this.cards = new ArrayList<Card>();
        this.game = game;
    }

    public int getTeam() {
        return this.team;
    }
    public ArrayList<Card> getCards() {
        return this.cards;
    }
    public Game getGame() {
        return this.game;
    }

    public Card chooseCard(String name) {
        return this.chooseCard(name, false);
    }
    public Card chooseCard(String name, boolean printInitiative) {
        int index, groupSize = (this.cards).size();

        this.printInfo(name, printInitiative);
        if ( groupSize > 0 ) {
            while(true) {
                System.out.println( "Pick a card (index)" );
                if ( this.scanner.hasNextInt() ) {
                    index = this.scanner.nextInt();
                    if ( index >= 1 && index <= groupSize ) {
                        break;
                    } else {
                        System.out.println("Not a valid number");
                        this.scanner.nextLine();
                    }
                } else {
                    System.out.println("Not a number");
                    this.scanner.nextLine();
                }
            }
            this.scanner.nextLine();//nuzhno(?)
            return (this.cards).get(index-1);
        }
        return null;
    }

    public void addCard(String name, int squad) {
        switch (name) {
            case ("Platoon Sergeant"):
                (this.cards).add(new CardPlatoonSergeant(name, squad, this));
                break;
            case ("Platoon Guide"):
                (this.cards).add(new CardPlatoonGuide(name, squad, this));
                break;
            case ("Squad Leader"):
                (this.cards).add(new CardSquadLeader(name, squad, this));
                break;
            case ("Rifleman"):
                (this.cards).add(new CardRifleman(name, squad, this));
                break;
            case ("Scout"):
                (this.cards).add(new CardScout(name, squad, this));
                break;
            case ("Machine Gunner"):
                (this.cards).add(new CardMachineGunner(name, squad, this));
                break;
            case ("Mortar"):
                (this.cards).add(new CardMortar(name, squad, this));
                break;
            case ("Sniper"):
                (this.cards).add(new CardSniper(name, squad, this));
                break;
            case ("Fog of War"):
                (this.cards).add(new CardFogOfWar(name, squad, this));
                break;
        }
    }
    public void addCard(Card card) {
        (this.cards).add(card);
        card.setCardGroup(this);
    }
    public void deleteCard(Card card) {
        (this.cards).remove(card);
    }

    public void allDiscardToDeck(int team) {
        CardGroup discard = (this.game).getDiscard(team);
        if ( !(discard.getCards()).isEmpty() ) {
            Card card;

            System.out.println("sent discard to deck");
            for ( int i = (discard.getCards()).size(); i > 0; i-- ) {
                card = (discard.getCards()).get(0);
                discard.deleteCard(card);
                this.addCard(card);

            }
        }
    }
    public void randomDeckToHand(int team) {
        CardGroup deck = (this.game).getDeck(team);
        if ( (deck.getCards()).isEmpty() ) {
            deck.allDiscardToDeck(team);
        }
        if ( !(deck.getCards()).isEmpty() ) {
            Card card = (deck.getCards()).get( (int) (Math.random() * (deck.getCards()).size()) );
            deck.deleteCard(card);
            this.addCard(card);
        } else {
            System.out.println("HUIHUIHUI");
        }
    }
    public void handToDiscard(int team, Card card) {
        CardGroup hand = (this.game).getHand(team);
        hand.deleteCard(card);
        this.addCard(card);
    }
    public void handToPlayArea(int team, Card card) {
        CardGroup hand = (this.game).getHand(team);
        hand.deleteCard(card);
        this.addCard(card);
    }
    public void handToSupply(int team, Card card) {
        CardGroup hand = (this.game).getHand(team);
        hand.deleteCard(card);
        this.addCard(card);
    }
    public void supplyToDiscard(int team, Card card) {
        CardGroup supply = (this.game).getSupply(team);
        supply.deleteCard(card);
        this.addCard(card);
    }
    public void playAreaToHand(int team, Card card) {
        CardGroup playArea = (this.game).getPlayArea(team);
        playArea.deleteCard(card);
        this.addCard(card);
    }
    public void allPlayAreaToDiscard(int team) {
        CardGroup playArea = (this.game).getPlayArea(team);
        if ( !(playArea.getCards()).isEmpty() ) {
            Card card;
            for ( int i = (playArea.getCards()).size(); i > 0; i-- ) {
                card = (playArea.getCards()).get(0);
                playArea.deleteCard(card);
                this.addCard(card);

            }
        }
    }
    public void allHandToDiscard(int team) {
        CardGroup hand = (this.game).getHand(team);
        if ( !(hand.getCards()).isEmpty() ) {
            Card card;
            for ( int i = (hand.getCards()).size(); i > 0; i-- ) {
                card = (hand.getCards()).get(0);
                hand.deleteCard(card);
                this.addCard(card);

            }
        }
    }

    public int findTypeCount(String name, int squad) {
        int count = 0;
        for (Card card : this.cards) {
            if ( card.getName() == name && card.getSquad() == squad ) {
                count += 1;
            }        
        }
        return count;
    }
    public Card findType(String name, int squad) {
        for (Card card : this.cards) {
            if ( card.getName() == name && card.getSquad() == squad ) {
                return card;
            }        
        }
        return null;
    }
    public Card findNonType(String name, int squad) {
        for (Card card : this.cards) {
            if ( card.getName() != name || card.getSquad() != squad ) {
                return card;
            }        
        }
        return null;
    }

    public void printInfo(String name) {
        this.printInfo(name, false);
    }
    public void printInfo(String name, boolean printInitiative) {
        System.out.print( "Cards in " + name + " (team " + Constants.teamName(this.team) + "): " );
        for (Card card : this.cards) {
            System.out.print( card.getInfoString() );
            if (printInitiative) System.out.print( " I:" + card.getInitiative() );
            System.out.print(", ");
        }
        System.out.print("end\n");
    }
}
