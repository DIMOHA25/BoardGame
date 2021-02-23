package normandyPack.constantValues;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;

public class Constants {
    public static final int NUMBER_OF_SCENARIOS = 1;
    //format: AmericanWin,GermanWin (0=pin), Intiative(American-0,German-1), Xsize,Ysize,
    //
    //AmericanDeck,
    //GermanDeck,
    //decks: (PlatoonSergeantDeck/Supply)PlSD,PlSS, PlGD,PlGS, SLAD,SLAS,SLBD,SLBS,SLCD,SLCS,
    //RiAD,RiAS,RiBD,RiBS,RiCD,RiCS, ScAD,ScAS,ScBD,ScBS,ScCD,ScCS, MGAD,MGAS,MGBD,MGBS,MGCD,MGCS,
    //MorD,MorS, SniD,SniS, FoWD,FoWS,
    //
    //square_on_0_0,square_on_0_1, ... ,square_on_Xsize-1_Ysize-1
    //squares: armorValue(4-hill,9-empty),
    //pointValue,
    //levelOfControl(binary: 0/1 scoutedA,scoutedG,controlledA,controlledG)##,
    //spawns(binary: 0/1 A,B,C,Mortar,Snipers + team A-0,G-1)##,
    //tokenPresence(binary: 0/1 RiA,RiB,RiC,ScA,ScB,ScC,MGA,MGB,MGC,Mortar,Snipers + team A-0,G-1)####
    public static final String[] SCENARIOS = {
        "55"+"0"+"44"+
        //
        "10"+"00"+"101000"+
        "141400"+"121200"+"000000"+
        "00"+"00"+"28"+
        //
        "10"+"00"+"101000"+
        "141400"+"121200"+"000000"+
        "00"+"00"+"28"+
        //
        "2108000000"+"3008623456"+"2100000000"+"9999999999"+
        "2100000000"+"1200000000"+"0000000000"+"0000000000"+
        "1200000000"+"0000000000"+"2004000000"+"1301633457"+
        "9999999999"+"9999999999"+"9999999999"+"0000000000"//,
        //
        //
        //"55"...
    };

    public static final String[] TYPE_NAMES = {"Platoon Sergeant", "Platoon Guide", "Squad Leader",
    "Rifleman", "Scout", "Machine Gunner", "Mortar", "Sniper", "Fog of War"};

    public static final int TEAM_AMERICANS = 0;
    public static final int TEAM_GERMANS = 1;

    public static final int SQUAD_A = 0;
    public static final int SQUAD_B = 1;
    public static final int SQUAD_C = 2;

    public static final int PIN_GOAL = 0;

    public static int otherTeam(int team) {
        if (team == Constants.TEAM_AMERICANS) return Constants.TEAM_GERMANS;
        if (team == Constants.TEAM_GERMANS) return Constants.TEAM_AMERICANS;
        return 25;
    }
}
