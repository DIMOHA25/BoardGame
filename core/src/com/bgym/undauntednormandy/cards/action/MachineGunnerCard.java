package com.bgym.undauntednormandy.cards.action;

public class MachineGunnerCard extends ActionCard{
    public MachineGunnerCard(char squad) {
        super("Machine Gunner", squad, 3, 1, 2);
    }

    @Override
    public void move(int move) {
        super.move(move);
    }

    @Override
    public void attack(int attack) {
        super.attack(attack);
    }

    public void suppress() {

    }
}
