package com.bgym.undauntednormandy.cards.action;

import com.bgym.undauntednormandy.cards.AbstractCard;

public abstract class ActionCard extends AbstractCard {
    private int move;
    private int attack;

    public ActionCard(String name, char squad, int id, int move, int attack) {
        super(name, squad, id, false);
        this.move = move;
        this.attack = attack;
    }

    public void move(int move) {

    }

    public void attack(int attack) {

    }

}
