package com.bgym.undauntednormandy.cards.support;

public class LeaderCard extends SupportCard{

    public LeaderCard(char squad) {
        super("Squad Leader", squad, 7, 2);
    }

    @Override
    public void bolster(int bolster) {
        super.bolster(bolster);
    }

    public void inspire(){

    }
}
