package normandyPack.cards;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Math;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;
import normandyPack.bots.*;

abstract public class Card {
    Scanner scanner = new Scanner(System.in);
    //
    protected String name;
    protected int squad, initiative, team;
    protected CardGroup cardGroup;
    protected Square spawnPoint;

    public Card() {
        this.initiative = 0;
        this.spawnPoint = null;
    }
    // public Card(String name, int squad, CardGroup cardGroup) {
    //     this.name = name;
    //     this.squad = squad;
    //     this.initiative = 0;
    //     this.cardGroup = cardGroup;
    // }

    public String getName() {
        return this.name;
    }
    public int getSquad() {
        return this.squad;
    }
    public String getInfoString() {
        String ouput = "";
        ouput += this.name;
        switch (this.squad) {
            case (Constants.NO_SQUAD):
                break;
            case (Constants.SQUAD_A):
                ouput += " A";
                break;
            case (Constants.SQUAD_B):
                ouput += " B";
                break;
            case (Constants.SQUAD_C):
                ouput += " C";
                break;
        }
        return ouput;
    }
    public int getTeam() {
        return this.team;
    }
    public int getInitiative() {
        return this.initiative;
    }
    public CardGroup getCardGroup() {
        return this.cardGroup;
    }

    public void setCardGroup(CardGroup cardGroup) {
        this.cardGroup = cardGroup;
    }

    protected int enterAnyInt(String description) {
        int i;

        while(true) {
            System.out.println("Enter " + description + ":");
            if ( this.scanner.hasNextInt() ) {
                i = this.scanner.nextInt();
                break;
            } else {
                System.out.println("Not a number");
                this.scanner.nextLine();
            }
        }
        this.scanner.nextLine();//nuzhno(?)

        return i;
    }
    protected int enterIntFromRange(String description, int min, int max) {
        int i;

        if (min > max) {
            System.out.println("HUIHUIHUI");
            return -1;
        }

        while(true) {
            System.out.println("Enter " + description + ":");
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

    protected void playCard() {
        CardGroup playArea = ((this.cardGroup).getGame()).getPlayArea(this.team);

        playArea.handToPlayArea(team, this);
    }
    protected void hunkerDown() {
        CardGroup supply = ((this.cardGroup).getGame()).getSupply(this.team);

        supply.handToSupply(this.team, this);
    }
    protected Token placeToken() {
        if (this.spawnPoint == null) {
            this.spawnPoint = (((this.cardGroup).getGame()).getField()).findSpawn(this);
        }

        if (this.spawnPoint != null) {
            return (this.spawnPoint).addToken(this.team, this.name, this.squad);
        }
        System.out.println("HUIHUIHUIpt");
        return null;
    }
    protected void unsuppress(Token token) {
        token.setSuppressed(false);
    }

    protected boolean stalk(int distance, Token executor, Bot bot) {
        String input;
        Square targetSquare;
        int x, y;
        boolean moved = false;

        for ( int i = 0; i < distance; ) {
            if (bot == null) {
                System.out.println("Do you want to end the move early? y/n");
                input = this.scanner.nextLine();
                if ( input.length() == 1 && input.charAt(0) == 'y' ) break;

                x = this.enterAnyInt("x coordinate of target square");
                y = this.enterAnyInt("y coordinate of target square");
            } else {
                int[] coords = bot.targetSquare();
                x = coords[0];
                y = coords[1];
            }

            targetSquare = (((this.cardGroup).getGame()).getField()).getSquare(x,y);
            if (targetSquare == null) {
                System.out.println("Invalid square");
                if (bot != null) break;
            } else if ( targetSquare.measureDistanceTo(executor.getSquare()) == 1 ) {
                executor.moveTo(targetSquare);
                moved = true;
                i++;
            } else {
                System.out.println("Square too far or the same");
                if (bot != null) break;
            }
        }

        return moved;
    }
    protected boolean move(int distance, Token executor, Bot bot) {
        String input;
        Square targetSquare;
        int x, y;
        boolean moved = false;

        for ( int i = 0; i < distance; ) {
            if (bot == null) {
                System.out.println("Do you want to end the move early? y/n");
                input = this.scanner.nextLine();
                if ( input.length() == 1 && input.charAt(0) == 'y' ) break;

                x = this.enterAnyInt("x coordinate of target square");
                y = this.enterAnyInt("y coordinate of target square");
            } else {
                int[] coords = bot.targetSquare();
                x = coords[0];
                y = coords[1];
            }

            targetSquare = (((this.cardGroup).getGame()).getField()).getSquare(x,y);
            if (targetSquare == null) {
                System.out.println("Invalid square");
                if (bot != null) break;
            } else if ( targetSquare.measureDistanceTo(executor.getSquare()) == 1 &&
            targetSquare.getLevelOfControl( executor.getTeam() ) > 0 ) {
                executor.moveTo(targetSquare);
                moved = true;
                executor.setMortarTarget(null);
                i++;
            } else {
                System.out.println("Square too far, the same or not scouted/controlled");
                if (bot != null) break;
            }
        }

        return moved;
    }
    protected boolean scout(int distance, Token executor, Bot bot) {
        String input;
        Square targetSquare;
        int x, y;
        boolean scouted;
        boolean moved = false;
        CardGroup supply = ((this.cardGroup).getGame()).getSupply(this.team);
        CardGroup discard = ((this.cardGroup).getGame()).getDiscard(this.team);
        Card fogInSupply;

        for ( int i = 0; i < distance; ) {
            if (bot == null) {
                System.out.println("Do you want to end the move early? y/n");
                input = this.scanner.nextLine();
                if ( input.length() == 1 && input.charAt(0) == 'y' ) break;

                x = this.enterAnyInt("x coordinate of target square");
                y = this.enterAnyInt("y coordinate of target square");
            } else {
                if ( i > 0 && bot.actionSkip() ) break;

                int[] coords = bot.targetSquare();
                x = coords[0];
                y = coords[1];
            }

            targetSquare = (((this.cardGroup).getGame()).getField()).getSquare(x,y);
            if (targetSquare == null) {
                System.out.println("Invalid square");
                if (bot != null) break;
            } else if ( targetSquare.measureDistanceTo(executor.getSquare()) == 1 ) {
                executor.moveTo(targetSquare);
                moved = true;
                scouted = targetSquare.scoutAction(executor.getTeam());
                if (scouted) {
                    fogInSupply = supply.findType(Constants.TYPE_NAMES[8], Constants.NO_SQUAD);
                    if (fogInSupply != null) discard.supplyToDiscard(this.team,fogInSupply);
                }
                if (bot != null) bot.formulateInput();
                i++;
            } else {
                System.out.println("Square too far or the same");
                if (bot != null) break;
            }
        }

        return moved;
    }
    protected boolean guide(int distance, Bot bot) {
        int choice;
        ArrayList<Token> friendlyTokenList = ((this.cardGroup).getGame()).getTokenList(this.team);
        Token guidedToken;
        boolean foundNonSuppressed = false;

        for (Token token : friendlyTokenList) {
            if ( token.getSuppressed() != true ) {
                foundNonSuppressed = true;
                break;
            }        
        }

        if ( foundNonSuppressed ) {
            if (bot == null) {
                ((this.cardGroup).getGame()).printTokenInfo(this.team,true);
                choice = this.enterIntFromRange("the index(>0) of target token",1,friendlyTokenList.size());
                guidedToken = friendlyTokenList.get(choice-1);
            } else {
                guidedToken = bot.targetTokenPick(this.team);
                if (guidedToken == null) return false;
            }

            if ( guidedToken.getSuppressed() == true ) {
                System.out.println("Token is suppressed");
                return false;
            }

            return this.move(distance,guidedToken,bot);
        } else {
            System.out.println("HUIHUIHUI");
            return false;
        }
    }

    protected boolean attack(int power, Token executor, Bot bot) {
        int otherTeam = Constants.otherTeam(this.team);
        int choice, defence, roll;
        ArrayList<Token> enemyTokenList = ((this.cardGroup).getGame()).getTokenList(otherTeam);
        Token targetToken;
        boolean success = false;

        if ( !enemyTokenList.isEmpty() ) {
            if (bot == null) {
                ((this.cardGroup).getGame()).printTokenInfo(otherTeam,true);
                choice = this.enterIntFromRange( "the index(>0) of target token", 1, enemyTokenList.size() );
                targetToken = enemyTokenList.get(choice-1);
            } else {
                targetToken = bot.targetTokenPick(otherTeam);
                if (targetToken == null) return false;
            }

            defence = targetToken.getArmor();
            defence += (targetToken.getSquare()).getArmor(executor);
            defence += (targetToken.getSquare()).measureDistanceTo(executor.getSquare());

            for ( int i = 0; i < power; i++ ) {
                roll = (int)(Math.random()*10);
                System.out.println( ">roll:" + roll + " vs defence:" + defence );
                if ( roll == 0 || roll >= defence ) {
                    success = true;
                    break;
                }
            }

            if (success) {
                CardGroup enemyHand = ((this.cardGroup).getGame()).getHand(otherTeam);
                CardGroup enemyDiscard = ((this.cardGroup).getGame()).getDiscard(otherTeam);
                CardGroup enemyDeck = ((this.cardGroup).getGame()).getDeck(otherTeam);
                Card cardInHand = enemyHand.findType( targetToken.getType(), targetToken.getSquad() );
                Card cardInDiscard = enemyDiscard.findType( targetToken.getType(), targetToken.getSquad() );
                Card cardInDeck = enemyDeck.findType( targetToken.getType(), targetToken.getSquad() );

                if ( cardInHand != null ) {
                    enemyHand.deleteCard(cardInHand);
                    System.out.println(">deleted card in hand");
                } else if ( cardInDiscard != null ) {
                    enemyDiscard.deleteCard(cardInDiscard);
                    System.out.println(">deleted card in discard");
                } else if ( cardInDeck != null ) {
                    enemyDeck.deleteCard(cardInDeck);
                    System.out.println(">deleted card in deck");
                } else {
                    targetToken.deletDis();
                    System.out.println(">deleted token");
                }
            } else {
                System.out.println(">fail");
            }
            return true;
        } else {
            System.out.println("HUIHUIHUIatk");
            return false;
        }
    }
    protected boolean suppress(int power, Token executor, Bot bot) {
        int otherTeam = Constants.otherTeam(this.team);
        int choice, defence, roll;
        ArrayList<Token> enemyTokenList = ((this.cardGroup).getGame()).getTokenList(otherTeam);
        Token targetToken;
        boolean success = false;

        if ( !enemyTokenList.isEmpty() ) {
            if (bot == null) {
                ((this.cardGroup).getGame()).printTokenInfo(otherTeam,true);
                choice = this.enterIntFromRange( "the index(>0) of target token", 1, enemyTokenList.size() );
                targetToken = enemyTokenList.get(choice-1);
            } else {
                targetToken = bot.targetTokenPick(otherTeam);
                if (targetToken == null) return false;
            }

            defence = targetToken.getArmor();
            defence += (targetToken.getSquare()).getArmor(executor);
            defence += (targetToken.getSquare()).measureDistanceTo(executor.getSquare());

            for ( int i = 0; i < power; i++ ) {
                roll = (int)(Math.random()*10);
                System.out.println( ">roll:" + roll + " vs defence:" + defence );
                if ( roll == 0 || roll >= defence ) {
                    success = true;
                    break;
                }
            }

            if (success) {
                targetToken.setSuppressed(true);
                System.out.println(">suppresed target");
            } else {
                System.out.println(">fail");
            }
            return true;
        } else {
            System.out.println("HUIHUIHUI");
            return false;
        }
    }
    protected boolean blast(int power, Token executor) {
        if ( executor.getMortarTarget() != null ) {
            int defence, roll, tokenTeam;
            ArrayList<Token> targetTokenList = (executor.getMortarTarget()).getTokensInside();
            Token targetToken;
            boolean success = false;

            for ( int i = targetTokenList.size(); i > 0; i-- ) {
                targetToken = targetTokenList.get(i-1);
                tokenTeam = targetToken.getTeam();
                success = false;

                defence = targetToken.getArmor();
                defence += (targetToken.getSquare()).getArmor(executor);

                for ( int j = 0; j < power; j++ ) {
                    roll = (int)(Math.random()*10);
                    System.out.println( ">roll:" + roll + " vs defence:" + defence );
                    if ( roll == 0 || roll >= defence ) {
                        success = true;
                        break;
                    }
                }

                if (success) {
                    CardGroup tokensHand = ((this.cardGroup).getGame()).getHand(tokenTeam);
                    CardGroup tokensDiscard = ((this.cardGroup).getGame()).getDiscard(tokenTeam);
                    CardGroup tokensDeck = ((this.cardGroup).getGame()).getDeck(tokenTeam);
                    Card cardInHand = tokensHand.findType( targetToken.getType(), targetToken.getSquad() );
                    Card cardInDiscard = tokensDiscard.findType( targetToken.getType(), targetToken.getSquad() );
                    Card cardInDeck = tokensDeck.findType( targetToken.getType(), targetToken.getSquad() );

                    if ( cardInHand != null ) {
                        tokensHand.deleteCard(cardInHand);
                        System.out.println(">deleted card in hand");
                    } else if ( cardInDiscard != null ) {
                        tokensDiscard.deleteCard(cardInDiscard);
                        System.out.println(">deleted card in discard");
                    } else if ( cardInDeck != null ) {
                        tokensDeck.deleteCard(cardInDeck);
                        System.out.println(">deleted card in deck");
                    } else {
                        targetToken.deletDis();
                        System.out.println(">deleted token");
                    }
                } else {
                    System.out.println(">fail");
                }
            }
            return true;
        } else {
            System.out.println("HUIHUIHUI");
            return false;
        }
    }

    protected boolean target(Token executor, Bot bot) {
        Square targetSquare;
        int x, y;

        if (bot == null) {
            x = this.enterAnyInt("x coordinate of target square");
            y = this.enterAnyInt("y coordinate of target square");
        } else {
            int[] coords = bot.targetSquare();
            x = coords[0];
            y = coords[1];
        }

        targetSquare = (((this.cardGroup).getGame()).getField()).getSquare(x,y);
        if (targetSquare == null) {
            System.out.println("Invalid square");
            return false;
        } else if ( targetSquare.measureDistanceTo(executor.getSquare()) >= 3 ) {
            executor.setMortarTarget(targetSquare);
            return true;
        } else {
            System.out.println("Square too close");
            return false;
        }
    }
    protected boolean control(Token executor) {
        return (executor.getSquare()).controlAction(executor.getTeam());
    }
    protected boolean recon() {
        CardGroup hand = this.cardGroup;
        Card fogInHand = hand.findType(Constants.TYPE_NAMES[8], Constants.NO_SQUAD);

        if (fogInHand != null) {
            hand.deleteCard(fogInHand);
            return true;
        } else {
            System.out.println("HUIHUIHUI");
            return false;
        }
    }
    protected boolean conceal() {
        int otherTeam = Constants.otherTeam(this.team);
        CardGroup enemySupply = ((this.cardGroup).getGame()).getSupply(otherTeam);
        CardGroup enemyDiscard = ((this.cardGroup).getGame()).getDiscard(otherTeam);
        Card fogInSupply = enemySupply.findType(Constants.TYPE_NAMES[8], Constants.NO_SQUAD);

        if (fogInSupply != null) {
            enemyDiscard.supplyToDiscard(otherTeam,fogInSupply);
            return true;
        } else {
            System.out.println("HUIHUIHUI");
            return false;
        }
    }
    protected boolean bolster(int numberOfCards, Bot bot) {
        String input;
        CardGroup supply = ((this.cardGroup).getGame()).getSupply(this.team);
        CardGroup discard = ((this.cardGroup).getGame()).getDiscard(this.team);
        int numberOfCardsInSupply = (supply.getCards()).size();
        Card chosenCard;
        boolean bolstered = false;

        if (numberOfCardsInSupply == 0) {
            System.out.println("HUIHUIHUI");
            return false;
        } else if (numberOfCardsInSupply < numberOfCards) {
            numberOfCards = numberOfCardsInSupply;
        }

        for ( int i = 0; i < numberOfCards; i++ ) {
            if (bot == null) {
                System.out.println("Do you want to end the bolster early? y/n");
                input = this.scanner.nextLine();
                if ( input.length() == 1 && input.charAt(0) == 'y' ) break;

                chosenCard = supply.chooseCard("supplies");
            } else {
                if ( i > 0 && bot.actionSkip() ) break;

                chosenCard = bot.chooseCardFromGroup(supply);
                if (chosenCard == null) break;
            }

            discard.supplyToDiscard( this.team, chosenCard );
            if (bot != null) bot.formulateInput();
            bolstered = true;
        }

        return bolstered;
    }
    protected boolean bolster(int numberOfCards, int squad, Bot bot) {
        String input;
        CardGroup supply = ((this.cardGroup).getGame()).getSupply(this.team);
        CardGroup discard = ((this.cardGroup).getGame()).getDiscard(this.team);
        int numberOfCardsInSupply = (supply.getCards()).size();
        Card chosenCard;
        boolean foundSquad, bolstered = false;

        foundSquad = supply.findType(Constants.TYPE_NAMES[2],squad) != null;
        if (!foundSquad) foundSquad = supply.findType(Constants.TYPE_NAMES[3],squad) != null;
        if (!foundSquad) foundSquad = supply.findType(Constants.TYPE_NAMES[4],squad) != null;
        if (!foundSquad) foundSquad = supply.findType(Constants.TYPE_NAMES[5],squad) != null;

        if (numberOfCardsInSupply == 0 || !foundSquad) {
            System.out.println("HUIHUIHUI");
            return false;
        } else if ( numberOfCardsInSupply < numberOfCards ) {
            numberOfCards = numberOfCardsInSupply;
        }

        for ( int i = 0; i < numberOfCards; ) {
            if (bot == null) {
                System.out.println("Do you want to end the bolster early? y/n");
                input = this.scanner.nextLine();
                if ( input.length() == 1 && input.charAt(0) == 'y' ) break;

                chosenCard = supply.chooseCard("supplies");
            } else {
                if ( i > 0 && bot.actionSkip() ) break;

                chosenCard = bot.chooseCardFromGroup(supply);
                if (chosenCard == null) break;
            }

            if (chosenCard.getSquad() != squad) {
                System.out.println("Wrong squad");
                if (bot != null) break;
            } else {
                discard.supplyToDiscard(this.team,chosenCard);
                bolstered = true;
                if (bot != null) bot.formulateInput();
                i++;

                foundSquad = supply.findType(Constants.TYPE_NAMES[2],squad) != null;
                if (!foundSquad) foundSquad = supply.findType(Constants.TYPE_NAMES[3],squad) != null;
                if (!foundSquad) foundSquad = supply.findType(Constants.TYPE_NAMES[4],squad) != null;
                if (!foundSquad) foundSquad = supply.findType(Constants.TYPE_NAMES[5],squad) != null;

                if (!foundSquad) break;
            }
        }

        return bolstered;
    }
    protected boolean command(int numberOfCards, Bot bot) {
        String input;
        CardGroup hand = ((this.cardGroup).getGame()).getHand(this.team);
        CardGroup deck = ((this.cardGroup).getGame()).getDeck(this.team);
        CardGroup discard = ((this.cardGroup).getGame()).getDiscard(this.team);
        int numberOfCardsInDeck = (deck.getCards()).size();
        int numberOfCardsInDiscard = (discard.getCards()).size();
        boolean commanded = false;

        if (numberOfCardsInDeck == 0 && numberOfCardsInDiscard == 0) {
            System.out.println("HUIHUIHUI");
            return false;
        } else if ( (numberOfCardsInDeck + numberOfCardsInDiscard) < numberOfCards ) {
            numberOfCards = numberOfCardsInDeck + numberOfCardsInDiscard;
        }

        for ( int i = 0; i < numberOfCards; i++ ) {
            if (bot == null) {
                System.out.println("Do you want to end the command early? y/n");
                input = this.scanner.nextLine();
                if ( input.length() == 1 && input.charAt(0) == 'y' ) break;
            } else {
                if ( i > 0 && bot.actionSkip() ) break;
            }

            hand.randomDeckToHand(this.team);
            if (bot == null) hand.printInfo("hand");
            else bot.formulateInput();
            commanded = true;
        }

        return commanded;
    }
    protected boolean inspire(int numberOfCards, Bot bot) {
        String input;
        CardGroup hand = ((this.cardGroup).getGame()).getHand(this.team);
        CardGroup playArea = ((this.cardGroup).getGame()).getPlayArea(this.team);
        int numberOfCardsInPlayArea = (playArea.getCards()).size();
        Card chosenCard;
        boolean foundSquad, inspired = false;

        foundSquad = playArea.findType(Constants.TYPE_NAMES[2],this.squad) != null;
        if (!foundSquad) foundSquad = playArea.findType(Constants.TYPE_NAMES[3],this.squad) != null;
        if (!foundSquad) foundSquad = playArea.findType(Constants.TYPE_NAMES[4],this.squad) != null;
        if (!foundSquad) foundSquad = playArea.findType(Constants.TYPE_NAMES[5],this.squad) != null;

        if (numberOfCardsInPlayArea == 0 || !foundSquad) {
            System.out.println("HUIHUIHUI");
            return false;
        } else if ( numberOfCardsInPlayArea < numberOfCards ) {
            numberOfCards = numberOfCardsInPlayArea;
        }

        for ( int i = 0; i < numberOfCards; ) {
            if (bot == null) {
                System.out.println("Do you want to end the inspire early? y/n");
                input = this.scanner.nextLine();
                if ( input.length() == 1 && input.charAt(0) == 'y' ) break;

                chosenCard = playArea.chooseCard("play area");
            } else {
                // if ( i > 0 && bot.actionSkip() ) break;

                chosenCard = bot.chooseCardFromGroup(playArea);
                if (chosenCard == null) break;
            }

            if (chosenCard.getSquad() != this.squad) {
                System.out.println("Wrong squad");
                if (bot != null) break;
            } else {
                hand.playAreaToHand(this.team,chosenCard);
                inspired = true;
                // if (bot != null) bot.formulateInput();
                i++;

                foundSquad = playArea.findType(Constants.TYPE_NAMES[2],this.squad) != null;
                if (!foundSquad) foundSquad = playArea.findType(Constants.TYPE_NAMES[3],this.squad) != null;
                if (!foundSquad) foundSquad = playArea.findType(Constants.TYPE_NAMES[4],this.squad) != null;
                if (!foundSquad) foundSquad = playArea.findType(Constants.TYPE_NAMES[5],this.squad) != null;

                if (!foundSquad) break;
            }
        }

        return inspired;
    }

    //hui

    public void pickAction(Bot bot) {
        System.out.println("No actions!!!(abstract)");
    }
}
