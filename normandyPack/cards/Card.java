package normandyPack.cards;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;

public class Card {
    private String name;
    private int squad;
    private CardGroup cardGroup;

    public Card(String name, CardGroup cardGroup) {
        this.name = name;
        this.squad = -1;
        this.cardGroup = cardGroup;
    }
    public Card(String name, int squad, CardGroup cardGroup) {
        this.name = name;
        this.squad = squad;
        this.cardGroup = cardGroup;
    }

    public String getName() {
        return this.name;
    }
    public int getSquad() {
        return this.squad;
    }
    public CardGroup getCardGroup() {
        return this.cardGroup;
    }

    public void setCardGroup(CardGroup cardGroup) {
        this.cardGroup = cardGroup;
    }
}
