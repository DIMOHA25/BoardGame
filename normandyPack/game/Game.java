package normandyPack.game;
import java.util.Scanner;
import java.util.ArrayList;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;

public class Game {
    private int americanScore, germanScore;
    private int americanGoal, germanGoal;
    private int initiative, scenarioNumber;
    private boolean americansPinned, germansPinned;
    private boolean victorDecided, americansWon;
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

    public void printTokenInfo(int team) {
        if ( team == Constants.TEAM_AMERICANS ) {
            for (Token token : this.americanTokens) {
                token.printInfo();
            }
        } else {
            for (Token token : this.germanTokens) {
                token.printInfo();
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
            System.out.println(" DEBUG: american win via point goal");
        } else if ( this.germanGoal != Constants.PIN_GOAL && this.germanScore >= this.germanGoal ) {
            this.victory(Constants.TEAM_GERMANS);
            System.out.println(" DEBUG: german win via point goal");
        } else if ( this.americanGoal == Constants.PIN_GOAL && this.germansPinned == true ) {
            this.victory(Constants.TEAM_AMERICANS);
            System.out.println(" DEBUG: american win via pin goal");
        } else if ( this.germanGoal == Constants.PIN_GOAL && this.americansPinned == true ) {
            this.victory(Constants.TEAM_GERMANS);
            System.out.println(" DEBUG: german win via pin goal");
        } else if ( this.germansPinned == true && this.americansPinned == true ) {
            if ( americanScore > germanScore ) {
                this.victory(Constants.TEAM_AMERICANS);
            System.out.println(" DEBUG: american win via points in a double pin");
            } else if ( americanScore < germanScore ) {
                this.victory(Constants.TEAM_GERMANS);
            System.out.println(" DEBUG: german win via points in a double pin");
            } else {
                this.victory(this.initiative);
            System.out.println(" DEBUG: " + this.initiative + " win via initiative in a double pin");
            }
        } else {
            System.out.println(" DEBUG: no victor");
        }
    }

    public void pinCheck() {
        boolean foundRiflemen = false;
        for (Token token : this.americanTokens) {
            if ( token.getType() == Constants.TYPE_NAMES[3]) {
                foundRiflemen = true;
                break;
            }        
        }
        this.americansPinned = !foundRiflemen;

        foundRiflemen = false;
        for (Token token : this.germanTokens) {
            if ( token.getType() == Constants.TYPE_NAMES[3]) {
                foundRiflemen = true;
                break;
            }        
        }
        this.germansPinned = !foundRiflemen;

        if ( this.americansPinned == true || this.germansPinned == true ) {
            this.winCheck();
        }
    }

    private void readScenario(String scenarioString) {
        int sizeX, sizeY;

        this.americanGoal = Integer.parseInt(scenarioString.substring(0,1));
        this.germanGoal = Integer.parseInt(scenarioString.substring(1,2));
        this.initiative = Integer.parseInt(scenarioString.substring(2,3));
        sizeX = Integer.parseInt(scenarioString.substring(3,4));
        sizeY = Integer.parseInt(scenarioString.substring(4,5));
        this.field = new Field(sizeX,sizeY,this);

        for ( int t = 0; t < 2; t++ ) {
            for ( int i = 0; i < 2; i++ ) {
                for ( int n = Integer.parseInt(
                scenarioString.substring(5+(i*2)+(t*34),6+(i*2)+(t*34)) ); n > 0; n-- ) {
                    (this.decks[t]).addCard(Constants.TYPE_NAMES[i]);
                }
                for ( int n = Integer.parseInt(
                scenarioString.substring(6+(i*2)+(t*34),7+(i*2)+(t*34)) ); n > 0; n-- ) {
                    (this.supplies[t]).addCard(Constants.TYPE_NAMES[i]);
                }
            }
            for ( int i = 0; i < 4; i++ ) {
                for ( int s = 0; s < 3; s++ ) {
                    for ( int n = Integer.parseInt(
                    scenarioString.substring(9+(s*2)+(i*6)+(t*34),10+(s*2)+(i*6)+(t*34)) ); n > 0; n-- ) {
                        (this.decks[t]).addCard(Constants.TYPE_NAMES[2+i],s);
                    }
                    for ( int n = Integer.parseInt(
                    scenarioString.substring(10+(s*2)+(i*6)+(t*34),11+(s*2)+(i*6)+(t*34)) ); n > 0; n-- ) {
                        (this.supplies[t]).addCard(Constants.TYPE_NAMES[2+i],s);
                    }
                }
            }
            for ( int i = 0; i < 3; i++ ) {
                for ( int n = Integer.parseInt(
                scenarioString.substring(33+(i*2)+(t*34),34+(i*2)+(t*34)) ); n > 0; n-- ) {
                    (this.decks[t]).addCard(Constants.TYPE_NAMES[6+i]);
                }
                for ( int n = Integer.parseInt(
                scenarioString.substring(34+(i*2)+(t*34),35+(i*2)+(t*34)) ); n > 0; n-- ) {
                    (this.supplies[t]).addCard(Constants.TYPE_NAMES[6+i]);
                }
            }
        }

        for ( int y = 0; y < sizeY; y++ ) {
            for ( int x = 0; x < sizeX; x++ ) {
                int armorValue = Integer.parseInt(
                scenarioString.substring(73+(x*10)+(y*sizeX*10),74+(x*10)+(y*sizeX*10)) );
                if ( armorValue != 9 ) {
                    int pointValue = Integer.parseInt(
                    scenarioString.substring(74+(x*10)+(y*sizeX*10),75+(x*10)+(y*sizeX*10)) );
                    int controlInfo = Integer.parseInt(
                    scenarioString.substring(75+(x*10)+(y*sizeX*10),77+(x*10)+(y*sizeX*10)) );
                    int spawnInfo = Integer.parseInt(
                    scenarioString.substring(77+(x*10)+(y*sizeX*10),79+(x*10)+(y*sizeX*10)) );
                    (this.field).addSquare(x, y, armorValue, pointValue, controlInfo, spawnInfo);

                    int tokenInfo = Integer.parseInt(
                    scenarioString.substring(79+(x*10)+(y*sizeX*10),83+(x*10)+(y*sizeX*10)) );
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
                            (tokenInfo%2,Constants.TYPE_NAMES[6]);
                        if ( (tokenInfo>>1)%2 == 1 ) ((this.field).getSquare(x,y)).addToken
                            (tokenInfo%2,Constants.TYPE_NAMES[7]);
                    }
                }
            }
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Pick a scenario 1-" + Constants.NUMBER_OF_SCENARIOS);
        while(true) {
            if ( scanner.hasNextInt() ) {
                this.scenarioNumber = scanner.nextInt();
                if ( this.scenarioNumber >= 1 && this.scenarioNumber <= Constants.NUMBER_OF_SCENARIOS ) {
                    break;
                } else {
                    System.out.println("Not a valid number");
                    scanner.nextLine();
                }
            } else {
                System.out.println("Not a number");
                scanner.nextLine();
            }
        }

        this.readScenario(Constants.SCENARIOS[this.scenarioNumber-1]);

        // while(this.victorDecided == false) {
        //     this.round();
        // }

        this.decks[0].printInfo();
        this.supplies[0].printInfo();
        this.decks[1].printInfo();
        this.supplies[1].printInfo();
        this.field.printInfo();
        this.printTokenInfo(0);
        this.printTokenInfo(1);

        this.americanScore = 4;
        this.germanScore = 6;

        this.pinCheck();
        this.winCheck();
        System.out.println( "americans:" + this.americanScore + "/" + this.americanGoal );
        System.out.println( "germans:" + this.germanScore + "/" + this.germanGoal );

        System.out.print("victory of team: ");
        if (this.americansWon) {
            System.out.println("US");
        } else {
            System.out.println("Reich");
        }
    }

    // private void round(int team) {
    //     int turn;
    // }
}
