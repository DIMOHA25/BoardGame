package normandyPack.game;
import java.util.Scanner;
import java.util.ArrayList;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;
import normandyPack.bots.*;

public class Game {
    Scanner scanner = new Scanner(System.in);
    //
    private int americanScore, germanScore;
    private int americanGoal, germanGoal;
    private int initiative, scenarioNumber, gameMode;
    private String botSpawnInfo[] = new String[2];
    private boolean verticalLines;
    private boolean americansPinned, germansPinned;
    private boolean victorDecided, americansWon;
    private Bot teamBots[] = {null, null};
    private ArrayList<Token> americanTokens, germanTokens;
    private CardGroup decks[] = {new CardGroup(Constants.TEAM_AMERICANS,this),
        new CardGroup(Constants.TEAM_GERMANS,this)};
    private CardGroup supplies[] = {new CardGroup(Constants.TEAM_AMERICANS,this),
        new CardGroup(Constants.TEAM_GERMANS,this)};
    private CardGroup discards[] = {new CardGroup(Constants.TEAM_AMERICANS,this),
        new CardGroup(Constants.TEAM_GERMANS,this)};
    private CardGroup hands[] = {new CardGroup(Constants.TEAM_AMERICANS,this),
        new CardGroup(Constants.TEAM_GERMANS,this)};
    private CardGroup playAreas[] = {new CardGroup(Constants.TEAM_AMERICANS,this),
        new CardGroup(Constants.TEAM_GERMANS,this)};
    private Field field;

    public Game() {
        this.americanScore = 0;
        this.germanScore = 0;
        this.americansPinned = false;
        this.germansPinned = false;
        this.victorDecided = false;
        this.americanTokens = new ArrayList<Token>();
        this.germanTokens = new ArrayList<Token>();
    }
    
    public CardGroup getDeck(int team) {
        return this.decks[team];
    }
    public CardGroup getSupply(int team) {
        return this.supplies[team];
    }
    public CardGroup getDiscard(int team) {
        return this.discards[team];
    }
    public CardGroup getHand(int team) {
        return this.hands[team];
    }
    public CardGroup getPlayArea(int team) {
        return this.playAreas[team];
    }

    public String getBotSpawnInfo(int team) {
        return this.botSpawnInfo[team];
    }

    public int getInitiative() {
        return this.initiative;
    }

    public Field getField() {
        return this.field;
    }
    public ArrayList<Token> getTokenList(int team) {
        if ( team == Constants.TEAM_AMERICANS ) {
            return this.americanTokens;
        } else {
            return this.germanTokens;
        }
    }
    
    public int getTeamScore(int team) {
        if  (team == Constants.TEAM_AMERICANS ) return this.americanScore;
        else return this.germanScore;
    }
    public int getTeamGoal(int team) {
        if  (team == Constants.TEAM_AMERICANS ) return this.americanGoal;
        else return this.germanGoal;
    }
    
    public void addToAmericanScore(int addition) {
        this.americanScore += addition;
    }
    public void addToGermanScore(int addition) {
        this.germanScore += addition;
    }
    
    public void addToken(Token token, int team) {
        if ( team == Constants.TEAM_AMERICANS ) {
            (this.americanTokens).add(token);
        } else {
            (this.germanTokens).add(token);
        }
    }
    public void deleteToken(Token token, int team) {
        if ( team == Constants.TEAM_AMERICANS ) {
            (this.americanTokens).remove(token);
        } else {
            (this.germanTokens).remove(token);
        }
    }
    public Token findToken(String type, int squad, int team) {
        if ( team == Constants.TEAM_AMERICANS ) {
            for (Token token : this.americanTokens) {
                if ( token.getType() == type && token.getSquad() == squad ) {
                    return token;
                }        
            }
        } else {
            for (Token token : this.germanTokens) {
                if ( token.getType() == type && token.getSquad() == squad ) {
                    return token;
                }        
            }
        }

        return null;
    }

    public String botCardGroupInfo(CardGroup group) {
        String output = "";
        String[] names = Constants.BOT_CARD_NAMES;
        int[] squads = Constants.BOT_CARD_SQUADS;

        int found = group.findTypeCount(names[0],squads[0]);
        if (found < 10) output += "0";
        output += found;
        for ( int i = 1; i < 17; i++ ) {
            output += group.findTypeCount(names[i],squads[i]);
        }

        return output;
    }

    public String botTokenInfo(int team) {
        String output = "";
        String[] types = Constants.BOT_TOKEN_TYPES;
        int[] squads = Constants.BOT_TOKEN_SQUADS;
        int[] xCoordsT = {9,9};
        int[] yCoordsT = {9,9};
        Token bufferToken;
        boolean suppressed;

        for ( int t = 0; t < 2; t++ ) {
            team = Constants.otherTeam(team);
            for ( int i = 0; i < 11; i++ ) {
                bufferToken = this.findToken(types[i],squads[i],team);
                if (bufferToken != null) {
                    if (i == 0) {
                        Square target = bufferToken.getMortarTarget();
                        if (target != null) {
                            xCoordsT[team] = target.getX();
                            yCoordsT[team] = target.getY();
                        }
                    }
                    output += (bufferToken.getSquare()).getX();
                    output += (bufferToken.getSquare()).getY();
                    suppressed = bufferToken.getSuppressed();
                    if (suppressed) output += "1";
                    else output += "0";
                } else output += "999";
            }
        }

        output = "" + xCoordsT[Constants.otherTeam(team)] + yCoordsT[Constants.otherTeam(team)]
        + xCoordsT[team] + yCoordsT[team] + output;

        return output;
    }
    public void printTokenInfo(int team) {
        this.printTokenInfo(team, false);
    }
    public void printTokenInfo(int team, boolean printIndex) {
        int i = 1;
        if ( team == Constants.TEAM_AMERICANS ) {
            for (Token token : this.americanTokens) {
                if (!printIndex) token.printInfo();
                else System.out.print( "[" + i + "] " + token.stringInfo() );
                i++;
            }
        } else {
            for (Token token : this.germanTokens) {
                if (!printIndex) token.printInfo();
                else System.out.print( "[" + i + "] " + token.stringInfo() );
                i++;
            }
        }
    }

    private void victory(int team) {
        this.victorDecided = true;
        if ( team == Constants.TEAM_AMERICANS ) {
            this.americansWon = true;
        } else {
            this.americansWon = false;
        }
    }

    public void winCheck() {
        if ( this.americanGoal != Constants.PIN_GOAL && this.americanScore >= this.americanGoal ) {
            this.victory(Constants.TEAM_AMERICANS);
            // System.out.println(" DEBUG: american win via point goal");
        } else if ( this.germanGoal != Constants.PIN_GOAL && this.germanScore >= this.germanGoal ) {
            this.victory(Constants.TEAM_GERMANS);
            // System.out.println(" DEBUG: german win via point goal");
        } else if ( this.americanGoal == Constants.PIN_GOAL && this.germansPinned == true ) {
            this.victory(Constants.TEAM_AMERICANS);
            // System.out.println(" DEBUG: american win via pin goal");
        } else if ( this.germanGoal == Constants.PIN_GOAL && this.americansPinned == true ) {
            this.victory(Constants.TEAM_GERMANS);
            // System.out.println(" DEBUG: german win via pin goal");
        } else if ( this.germansPinned == true && this.americansPinned == true ) {
            if ( americanScore > germanScore ) {
                this.victory(Constants.TEAM_AMERICANS);
            // System.out.println(" DEBUG: american win via points in a double pin");
            } else if ( americanScore < germanScore ) {
                this.victory(Constants.TEAM_GERMANS);
            // System.out.println(" DEBUG: german win via points in a double pin");
            } else {
                this.victory(this.initiative);
            // System.out.println( " DEBUG: " + Constants.teamName(this.initiative)
            // + " win via initiative in a double pin" );
            }
        } else {
            // System.out.println(" DEBUG: no victor");
        }
    }

    public void pinCheck() {
        boolean foundRiflemen = false;
        for (Token token : this.americanTokens) {
            if ( token.getType() == Constants.TYPE_NAMES[3] ) {
                foundRiflemen = true;
                break;
            }        
        }
        this.americansPinned = !foundRiflemen;

        foundRiflemen = false;
        for (Token token : this.germanTokens) {
            if ( token.getType() == Constants.TYPE_NAMES[3] ) {
                foundRiflemen = true;
                break;
            }        
        }
        this.germansPinned = !foundRiflemen;

        if ( this.americansPinned == true || this.germansPinned == true ) {
            this.winCheck();
        }
    }

    private int enterIntFromRange(int min, int max) {
        int i;

        if (min > max) {
            System.out.println("HUIHUIHUI");
            return -1;
        }

        while(true) {
            if ( this.scanner.hasNextInt() ) {
                i = this.scanner.nextInt();
                if ( i >= min && i <= max ) {
                    break;
                } else {
                    System.out.println("Not a valid number");
                    this.scanner.nextLine();
                }
            } else {
                System.out.println("Not a number");
                this.scanner.nextLine();
            }
        }
        this.scanner.nextLine();//nuzhno(?)

        return i;
    }
    private void pickBot(int team) {
        System.out.println("Pick a bot for " + Constants.teamName(team) + ":");
        System.out.println("0 - Vlad");
        System.out.println("1 - Timofey");
        System.out.println("2 - Dima");
        int chosenBot = this.enterIntFromRange(0,Constants.NUMBER_OF_BOTS-1);
        switch (chosenBot) {
            case (Constants.KRASNIY_KORPUS):
                this.teamBots[team] = new KKBot(team, this);
                break;
            case (Constants.SHOLOHOV):
                this.teamBots[team] = new SholoBot(team, this);
                break;
            case (Constants.SCRIPTI):
                this.teamBots[team] = new ScriptoBot(team, this);
                break;
        }
    }

    private void readScenario(String scenarioString) {
        int sizeX, sizeY;

        this.americanGoal = Integer.parseInt(scenarioString.substring(0,2));
        this.germanGoal = Integer.parseInt(scenarioString.substring(2,4));
        this.initiative = Integer.parseInt(scenarioString.substring(4,5));
        sizeX = Integer.parseInt(scenarioString.substring(5,6));
        sizeY = Integer.parseInt(scenarioString.substring(6,7));
        this.verticalLines = Integer.parseInt(scenarioString.substring(7,8)) == 1;
        this.field = new Field(sizeX,sizeY,this);

        for ( int t = 0; t < 2; t++ ) {
            for ( int i = 0; i < 2; i++ ) {
                for ( int n = Integer.parseInt(
                scenarioString.substring(8+(i*2)+(t*34),9+(i*2)+(t*34)) ); n > 0; n-- ) {
                    (this.decks[t]).addCard(Constants.TYPE_NAMES[i],Constants.NO_SQUAD);
                }
                for ( int n = Integer.parseInt(
                scenarioString.substring(9+(i*2)+(t*34),10+(i*2)+(t*34)) ); n > 0; n-- ) {
                    (this.supplies[t]).addCard(Constants.TYPE_NAMES[i],Constants.NO_SQUAD);
                }
            }
            for ( int i = 0; i < 4; i++ ) {
                for ( int s = 0; s < 3; s++ ) {
                    for ( int n = Integer.parseInt(
                    scenarioString.substring(12+(s*2)+(i*6)+(t*34),13+(s*2)+(i*6)+(t*34)) ); n > 0; n-- ) {
                        (this.decks[t]).addCard(Constants.TYPE_NAMES[2+i],s);
                    }
                    for ( int n = Integer.parseInt(
                    scenarioString.substring(13+(s*2)+(i*6)+(t*34),14+(s*2)+(i*6)+(t*34)) ); n > 0; n-- ) {
                        (this.supplies[t]).addCard(Constants.TYPE_NAMES[2+i],s);
                    }
                }
            }
            for ( int i = 0; i < 3; i++ ) {
                for ( int n = Integer.parseInt(
                scenarioString.substring(36+(i*2)+(t*34),37+(i*2)+(t*34)) ); n > 0; n-- ) {
                    (this.decks[t]).addCard(Constants.TYPE_NAMES[6+i],Constants.NO_SQUAD);
                }
                for ( int n = Integer.parseInt(
                scenarioString.substring(37+(i*2)+(t*34),38+(i*2)+(t*34)) ); n > 0; n-- ) {
                    (this.supplies[t]).addCard(Constants.TYPE_NAMES[6+i],Constants.NO_SQUAD);
                }
            }
        }

        for ( int y = 0; y < sizeY; y++ ) {
            for ( int x = 0; x < sizeX; x++ ) {
                int armorValue = Integer.parseInt(
                scenarioString.substring(76+(x*10)+(y*sizeX*10),77+(x*10)+(y*sizeX*10)) );
                if ( armorValue != 9 ) {
                    int pointValue = Integer.parseInt(
                    scenarioString.substring(77+(x*10)+(y*sizeX*10),78+(x*10)+(y*sizeX*10)) );
                    int controlInfo = Integer.parseInt(
                    scenarioString.substring(78+(x*10)+(y*sizeX*10),80+(x*10)+(y*sizeX*10)) );
                    int spawnInfo = Integer.parseInt(
                    scenarioString.substring(80+(x*10)+(y*sizeX*10),82+(x*10)+(y*sizeX*10)) );
                    (this.field).addSquare(x, y, armorValue, pointValue, controlInfo, spawnInfo);

                    int tokenInfo = Integer.parseInt(
                    scenarioString.substring(82+(x*10)+(y*sizeX*10),86+(x*10)+(y*sizeX*10)) );
                    if ( tokenInfo > 1 ) {
                        if ( (tokenInfo>>11)%2 == 1 ) ((this.field).getSquare(x,y)).addToken
                            (tokenInfo%2,Constants.TYPE_NAMES[3],Constants.SQUAD_A);
                        if ( (tokenInfo>>10)%2 == 1 ) ((this.field).getSquare(x,y)).addToken
                            (tokenInfo%2,Constants.TYPE_NAMES[3],Constants.SQUAD_B);
                        if ( (tokenInfo>>9)%2 == 1 ) ((this.field).getSquare(x,y)).addToken
                            (tokenInfo%2,Constants.TYPE_NAMES[3],Constants.SQUAD_C);
                        if ( (tokenInfo>>8)%2 == 1 ) ((this.field).getSquare(x,y)).addToken
                            (tokenInfo%2,Constants.TYPE_NAMES[4],Constants.SQUAD_A);
                        if ( (tokenInfo>>7)%2 == 1 ) ((this.field).getSquare(x,y)).addToken
                            (tokenInfo%2,Constants.TYPE_NAMES[4],Constants.SQUAD_B);
                        if ( (tokenInfo>>6)%2 == 1 ) ((this.field).getSquare(x,y)).addToken
                            (tokenInfo%2,Constants.TYPE_NAMES[4],Constants.SQUAD_C);
                        if ( (tokenInfo>>5)%2 == 1 ) ((this.field).getSquare(x,y)).addToken
                            (tokenInfo%2,Constants.TYPE_NAMES[5],Constants.SQUAD_A);
                        if ( (tokenInfo>>4)%2 == 1 ) ((this.field).getSquare(x,y)).addToken
                            (tokenInfo%2,Constants.TYPE_NAMES[5],Constants.SQUAD_B);
                        if ( (tokenInfo>>3)%2 == 1 ) ((this.field).getSquare(x,y)).addToken
                            (tokenInfo%2,Constants.TYPE_NAMES[5],Constants.SQUAD_C);
                        if ( (tokenInfo>>2)%2 == 1 ) ((this.field).getSquare(x,y)).addToken
                            (tokenInfo%2,Constants.TYPE_NAMES[6],Constants.NO_SQUAD);
                        if ( (tokenInfo>>1)%2 == 1 ) ((this.field).getSquare(x,y)).addToken
                            (tokenInfo%2,Constants.TYPE_NAMES[7],Constants.NO_SQUAD);
                    }
                }
            }
        }
        //
        for ( int t = 0; t < 2; t++ ) {
            this.botSpawnInfo[t] = (this.field).getBotSpawns(t);
        }
    }

    private void turn(int team) {
        String input;
        Card card;

        System.out.println("\n\n\n\n\nTurn: " + Constants.teamName(team) + "\n");

        while ( this.victorDecided == false &&
        (this.hands[team]).findNonType(Constants.TYPE_NAMES[8], Constants.NO_SQUAD) != null ) {
            if (this.teamBots[team] == null) {
                System.out.println();
                this.hands[team].printInfo("hand");
                this.playAreas[team].printInfo("play area");
                this.supplies[0].printInfo("supplies");
                this.supplies[1].printInfo("supplies");
                this.field.printInfo();
                this.printTokenInfo(0);
                this.printTokenInfo(1);
                System.out.println( "americans:" + this.americanScore + "/" + this.americanGoal );
                System.out.println( "germans:" + this.germanScore + "/" + this.germanGoal );

                System.out.println("\nDo you want to end the turn early? y/n");
                input = this.scanner.nextLine();
                if ( input.length() == 1 && input.charAt(0) == 'y' ) {
                    break;
                } else {
                    card = (this.hands[team]).chooseCard("hand");
                    card.pickAction(null);
                }
            } else {
                (this.teamBots[team]).formulateInput();
                // System.out.println( Constants.teamName(team) +
                // " input:\n" + (this.teamBots[team]).getFormattedInput() );
                // System.out.println( Constants.teamName(team) +
                // " output:\t" + (this.teamBots[team]).getOutput() );

                if ( (this.teamBots[team]).turnSkip() == true ) {
                    System.out.println( (this.teamBots[team]).getName() + " skipped this turn" );
                    break;
                } else {
                    card = (this.teamBots[team]).playPick();
                    if (card != null) {
                        System.out.println( (this.teamBots[team]).getName() +
                        ": tried playing " + card.getInfoString() );
                        card.pickAction(this.teamBots[team]);
                    } else System.out.println( (this.teamBots[team]).getName() +
                    ": couldn't play a card" );
                }
            }
        }
        (this.discards[team]).allPlayAreaToDiscard(team);
        (this.discards[team]).allHandToDiscard(team);
    }

    private void round() {
        Card chosenInitiativeCards[] = new Card[2];

        System.out.println( "\n\n\n\n\nInitiative: " + Constants.teamName(this.initiative) + "\n" );

        for ( int i = 0; i < 2; i++ ) {
            (this.hands[i]).randomDeckToHand(i);
            (this.hands[i]).randomDeckToHand(i);
            (this.hands[i]).randomDeckToHand(i);
            (this.hands[i]).randomDeckToHand(i);

            if (this.teamBots[i] != null) {
                (this.teamBots[i]).formulateInput();
                // System.out.println( Constants.teamName(i) + " input:\n" +
                //     (this.teamBots[i]).getFormattedInput() );
                // System.out.println( Constants.teamName(i) + " output:\t" + (this.teamBots[i]).getOutput() );
                chosenInitiativeCards[i] = (this.teamBots[i]).intiativePick();
                System.out.println( (this.teamBots[i]).getName() + " intiative pick: " +
                (chosenInitiativeCards[i]).getInfoString() );//SPOILER?!?!?!?
            } else {
                chosenInitiativeCards[i] = (this.hands[i]).chooseCard("hand", true);
            }

            (this.discards[i]).handToDiscard(i,chosenInitiativeCards[i]);
        }

        if ( chosenInitiativeCards[this.initiative].getInitiative() < 
        chosenInitiativeCards[Constants.otherTeam(this.initiative)].getInitiative() ) {
            this.initiative = Constants.otherTeam(this.initiative);
        }

        if (this.victorDecided == false) this.turn(this.initiative);
        if (this.victorDecided == false) this.turn(Constants.otherTeam(this.initiative));
    }

    public void start() {
        System.out.println("Pick a scenario 1-" + Constants.NUMBER_OF_SCENARIOS);
        this.scenarioNumber = this.enterIntFromRange(1,Constants.NUMBER_OF_SCENARIOS);
        this.readScenario(Constants.SCENARIOS[this.scenarioNumber-1]);
        this.victorDecided = false;

        System.out.println("Pick a game mode:");
        System.out.println("0 - Local PvP");
        System.out.println("1 - vs Bot");
        System.out.println("2 - Bot vs Bot");
        // System.out.println("3 - Online PvP");
        this.gameMode = this.enterIntFromRange(0,Constants.NUMBER_OF_GAME_MODES-1);
        switch (this.gameMode) {
            case (Constants.LOCAL_PVP):
                break;
            case (Constants.VS_BOT):
                int botTeam;
                String input;
                System.out.println("Do you want to play as americans? y/n");
                input = scanner.nextLine();
                if ( input.length() == 1 && input.charAt(0) == 'y' ) {
                    botTeam = Constants.TEAM_GERMANS;
                } else {
                    botTeam = Constants.TEAM_AMERICANS;
                }
                this.pickBot(botTeam);
                break;
            case (Constants.BOT_VS_BOT):
                for ( int i = 0; i < 2; i++ ) {
                    this.pickBot(i);
                }
                break;
        }

        System.out.println("\n\n\n\n\nstart\n");
        // this.decks[0].printInfo("deck");
        this.supplies[0].printInfo("supplies");
        // System.out.println(this.supplies[0].stringInfo());
        // this.decks[1].printInfo("deck");
        this.supplies[1].printInfo("supplies");
        // System.out.println(this.supplies[1].stringInfo());
        this.field.printInfo();
        this.printTokenInfo(0);
        this.printTokenInfo(1);
        System.out.println( "americans:" + this.americanScore + "/" + this.americanGoal );
        System.out.println( "germans:" + this.germanScore + "/" + this.germanGoal );
        // this.hands[0].printInfo("hand");
        // this.playAreas[0].printInfo("play area");

        while(this.victorDecided == false) {
            this.round();
        }

        System.out.print("\n\n\n\n\n\n\n\n\n\nvictory of team: ");
        if (this.americansWon) {
            System.out.println("US\n\n");
        } else {
            System.out.println("Reich\n\n");
        }
    }
}
