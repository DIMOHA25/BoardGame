package com.bgym.undauntednormandy.cards;

public abstract class AbstractCard {
    public String name = "CardName";
    public char squad = ' '; // Squad A/Squad B/Squad C
    public final int id; /*Correct id:
     * Platoon Sergeant = 9
     * Platoon Guide = 8
     * Squad leader = 7
     * Scout = 6
     * Rifleman = 5
     * Sniper = 4
     * Machine Gunner = 3
     * Mortar = 2
     * Fog of war = 1
     * */
    public boolean support = false;

    public AbstractCard(String name, char squad, int id, boolean support) {
        this.name = name;
        this.squad = squad;
        this.id = id;
        this.support = support;
    }
}
