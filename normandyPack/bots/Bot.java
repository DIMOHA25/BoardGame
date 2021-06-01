package normandyPack.bots;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;
import normandyPack.bots.*;

abstract public class Bot {
    protected int team;
    protected String input, output, name;
    protected Game game;

    public Bot() {
        //
    }

    public String getInput() {
        return this.input;
    }
    public String getOutput() {
        return this.output;
    }
    public String getName() {
        return this.name;
    }

    public void formulateInput() {
        int scoreBuffer;

        if ( this.team == this.game.getInitiative() ) this.input = "1";
        else this.input = "0";

        scoreBuffer = this.game.getTeamScore( Constants.otherTeam(this.team) );
        if (scoreBuffer < 10) this.input += "0";
        this.input += scoreBuffer;
        scoreBuffer = this.game.getTeamScore( this.team );
        if (scoreBuffer < 10) this.input += "0";
        this.input += scoreBuffer;

        scoreBuffer = this.game.getTeamGoal( Constants.otherTeam(this.team) );
        if (scoreBuffer < 10) this.input += "0";
        this.input += scoreBuffer;
        scoreBuffer = this.game.getTeamGoal( this.team );
        if (scoreBuffer < 10) this.input += "0";
        this.input += scoreBuffer;

        this.input += ((this.game).getField()).getBotInfo(this.team);

        this.input += (this.game).getBotSpawnInfo( Constants.otherTeam(this.team) );
        this.input += (this.game).getBotSpawnInfo( this.team );

        this.input += (this.game).botTokenInfo( this.team );

        this.input += (this.game).botCardGroupInfo( (this.game).getSupply(Constants.otherTeam(this.team)) );
        this.input += (this.game).botCardGroupInfo( (this.game).getSupply(this.team) );
        this.input += (this.game).botCardGroupInfo( (this.game).getDeck(this.team) );
        this.input += (this.game).botCardGroupInfo( (this.game).getDiscard(this.team) );
        this.input += (this.game).botCardGroupInfo( (this.game).getHand(this.team) );
        this.input += (this.game).botCardGroupInfo( (this.game).getPlayArea(this.team) );
        
        this.calculateOutput();
    }
    protected void calculateOutput() {
        //
    }

    public Card intiativePick() {
        String[] names = Constants.BOT_CARD_NAMES;
        int[] squads = Constants.BOT_CARD_SQUADS;
        int buffer;
        CardGroup hand = (this.game).getHand(this.team);
        Card pick = null;

        for ( int i = 0; i < 17; i++ ) {
            buffer = Integer.parseInt(this.output.substring(i,i+1));
            if (buffer == 1) {
                pick = hand.findType(names[i],squads[i]);
                if (pick != null) return pick;
            }
        }

        //tut nado dat pizdi botu 

        System.out.println( this.name + " obosralsya" );
        return (hand.getCards()).get(0);
    }
    public Card playPick() {
        String[] names = Constants.BOT_CARD_NAMES;
        int[] squads = Constants.BOT_CARD_SQUADS;
        int buffer;
        CardGroup hand = (this.game).getHand(this.team);
        Card pick = null;

        for ( int i = 1; i < 17; i++ ) {
            buffer = Integer.parseInt(this.output.substring(18+i,19+i));
            if (buffer == 1) {
                pick = hand.findType(names[i],squads[i]);
                if (pick != null) return pick;
            }
        }

        //tut nado dat pizdi botu 

        return null;//hand.findNonType(Constants.TYPE_NAMES[8], Constants.NO_SQUAD);
    }
    public Card chooseCardFromGroup(CardGroup group) {
        String[] names = Constants.BOT_CARD_NAMES;
        int[] squads = Constants.BOT_CARD_SQUADS;
        int buffer;
        Card pick = null;

        for ( int i = 0; i < 17; i++ ) {
            buffer = Integer.parseInt(this.output.substring(50+i,51+i));
            if (buffer == 1) {
                pick = group.findType(names[i],squads[i]);
                if (pick != null) return pick;
            }
        }

        //tut nado dat pizdi botu 

        return null;
    }
    public Token targetTokenPick(int team) {
        String[] types = Constants.BOT_TOKEN_TYPES;
        int[] squads = Constants.BOT_TOKEN_SQUADS;
        int buffer;
        Token pick = null;

        for ( int i = 0; i < 11; i++ ) {
            buffer = Integer.parseInt(this.output.substring(50+i,51+i));
            if (buffer == 1) {
                pick = this.game.findToken(types[i],squads[i],team);
                if (pick != null) return pick;
            }
        }

        //tut nado dat pizdi botu 

        return null;//(this.game.getTokenList(team)).get(0);
    }
    public boolean turnSkip() {
        return Integer.parseInt(this.output.substring(17,18)) == 1;
    }
    public boolean actionSkip() {
        return Integer.parseInt(this.output.substring(18,19)) == 1;
    }
    public int[] targetSquare() {
        int[] output = {Integer.parseInt(this.output.substring(66,67)),
        Integer.parseInt(this.output.substring(67,68))};
        return output;
    }
}

/*
input {
initiative ownership 0/1
scores (0-10 enemy,own)
target scores (0-10 enemy,own)
squares (armor,points,enemyControl,ownControl) 6x7
token info (
enemy spawns ((x;y) A,B,C,mortar,snipers)
own spawns ((x;y) A,B,C,mortar,snipers)
enemy target (x;y)
own target (x;y)
enemy tokens (v)
own tokens ((x;y;suppressed 0/1) Mortar,MGA,MGB,MGC,Sniper,RiA,RiB,RiC,ScA,ScB,ScC)
)
card groups (
enemy supply (v)
own supply (v)
own deck (00-10 FoW, 0-9 Mortar,MGA,MGB,MGC,Sniper,RiA,RiB,RiC,ScA,ScB,ScC,SqLeA,SqLeB,SqLeC,PlGui,PlSerg)
own discard (^)
own hand (^)
own play area (^)
)
}
output {
                    1     2    3   4   5     6    7   8   9   10  11  12   13    14    15    16    17
intiative pick(0/1 FoW,Mortar,MGA,MGB,MGC,Sniper,RiA,RiB,RiC,ScA,ScB,ScC,SqLeA,SqLeB,SqLeC,PlGui,PlSerg)
           18
turn skip(0/1)
             19
action skip(0/1)
                   20    21  22  23   24    25  26  27  28  29  30   31    32    33    34    35
card to play(0/1 Mortar,MGA,MGB,MGC,Sniper,RiA,RiB,RiC,ScA,ScB,ScC,SqLeA,SqLeB,SqLeC,PlGui,PlSerg)
            36    37    38    39    40   41  42    43    44      45     46     47      48      49      50
action(0/1Hunker,Move,Stalk,Scout,Guide,Att,Supp,Blast,Target,Control,Recon,Conceal,Bolster,Inspire,Command)
                        51    52  53  54   55    56  57  58  59  60  61   62    63    64    65    66
target card/token(0/1 Mortar,MGA,MGB,MGC,Sniper,RiA,RiB,RiC,ScA,ScB,ScC,SqLeA,SqLeB,SqLeC,PlGui,PlSerg)
             67 68
target square(x;y)
}
*/
