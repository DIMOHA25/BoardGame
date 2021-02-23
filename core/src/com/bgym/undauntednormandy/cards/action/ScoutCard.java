package com.bgym.undauntednormandy.cards.action;

public class ScoutCard extends ActionCard{
    public ScoutCard(char squad) {
        super("Scout", squad, 6, 2, 1);
    }

    @Override
    public void move(int move) {

    }

    @Override
    public void attack(int attack) {
        super.attack(attack);
    }

    public void conceal() {

    }

    public void recon() {

    }
}
