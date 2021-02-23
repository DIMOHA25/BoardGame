package normandyPack.cards;
import java.util.ArrayList;
import java.lang.Math;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;

public class CardGroup {
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

    public void addCard(String name) {
        (this.cards).add(new Card(name, this));
    }
    public void addCard(String name, int squad) {
        (this.cards).add(new Card(name, squad, this));
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
        Card card = (deck.getCards()).get( (int) (Math.random() * (deck.getCards()).size()) );
        deck.deleteCard(card);
        this.addCard(card);
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

    public void printInfo() {
        for (Card card : this.cards) {
            System.out.print( card.getName() );
            switch ( card.getSquad() ) {
                case (-1):
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
            System.out.print(", ");
        }
        System.out.print("stop\n");
    }
}
