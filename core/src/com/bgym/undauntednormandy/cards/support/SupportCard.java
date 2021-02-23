package com.bgym.undauntednormandy.cards.support;

import com.bgym.undauntednormandy.cards.AbstractCard;

public abstract class SupportCard extends AbstractCard {

    private int bolster;

    public SupportCard(String name, char squad, int id, int bolster) {
        super(name, squad, id, true);
        this.bolster = bolster;
    }

    public void bolster(int bolster) {

    }
}
