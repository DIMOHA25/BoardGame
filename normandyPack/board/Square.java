package normandyPack.board;
import java.util.ArrayList;
import java.lang.Math;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;
import normandyPack.bots.*;

public class Square {
    private int xCoord, yCoord, armor, points, levelOfControl[] = {0,0};
    //                             A,    B,    C, Mortar,Snipers
    private boolean spawns[][] = {{false,false,false,false,false},{false,false,false,false,false}};
    private Field field;
    private ArrayList<Token> tokensInside;

    public Square(int xCoord, int yCoord, int armor, int points, int controlInfo, int spawnInfo, Field field) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.armor = armor;
        this.points = points;
        this.field = field;
        this.tokensInside = new ArrayList<Token>();

        this.levelOfControl[Constants.TEAM_AMERICANS] = ( 2 * ( (controlInfo>>1)%2 ) ) + ( (controlInfo>>3)%2 );
        this.levelOfControl[Constants.TEAM_GERMANS] = ( 2*(controlInfo%2) ) + ( (controlInfo>>2)%2 );
        for ( int i = 0; i < 5; i++ ) {
            this.spawns[spawnInfo%2][4-i] = ( ( spawnInfo>>(i+1) ) % 2 ) == 1;
        }

        if (this.levelOfControl[Constants.TEAM_AMERICANS] == 2) {
            ((this.field).getGame()).addToAmericanScore(this.points);
        }
        if (this.levelOfControl[Constants.TEAM_GERMANS] == 2) {
            ((this.field).getGame()).addToGermanScore(this.points);
        }
    }

    public int getX() {
        return this.xCoord;
    }
    public int getY() {
        return this.yCoord;
    }
    public boolean[][] getSpawns() {
        return this.spawns;
    }
    public Field getField() {
        return this.field;
    }
    public int getLevelOfControl(int team) {
        return this.levelOfControl[team];
    }
    public int getPoints() {
        return this.points;
    }
    public int getRawArmor() {
        return this.armor;
    }
    public int getArmor(Token attackerToken) {
        if (this.armor != 4) return this.armor;

        if ( (attackerToken.getSquare()).getRawArmor() == 4 ||
        attackerToken.getType() == Constants.TYPE_NAMES[6] ) {
            return 1;
        }
        return 3;
    }
    public ArrayList<Token> getTokensInside() {
        return this.tokensInside;
    }

    public Token addToken(int team, String type, int squad) {
        Token token = new Token(team, type, squad, this);
        
        (this.tokensInside).add(token);
        ((this.field).getGame()).pinCheck();
        return token;
    }
    public void addToken(Token token) {
        (this.tokensInside).add(token);
        token.setSquare(this);
    }
    public void deleteToken(Token token) {
        (this.tokensInside).remove(token);
    }

    public boolean controlAction(int teamOfController) {
        boolean foundEnemy = false;

        for (Token token : this.tokensInside) {
            if ( token.getTeam() != teamOfController ) {
                foundEnemy = true;
                break;
            }        
        }

        if ( !foundEnemy && this.levelOfControl[teamOfController] == 1 ) {
            this.levelOfControl[teamOfController] = 2;
            switch (teamOfController) {
                case (Constants.TEAM_AMERICANS):
                    ((this.field).getGame()).addToAmericanScore(this.points);
                    break;
                case (Constants.TEAM_GERMANS):
                    ((this.field).getGame()).addToGermanScore(this.points);
                    break;
            }
            if (this.levelOfControl[Constants.otherTeam(teamOfController)] == 2) {
                this.levelOfControl[Constants.otherTeam(teamOfController)] = 1;
                switch (teamOfController) {
                    case (Constants.TEAM_AMERICANS):
                        ((this.field).getGame()).addToGermanScore(-this.points);
                        break;
                    case (Constants.TEAM_GERMANS):
                        ((this.field).getGame()).addToAmericanScore(-this.points);
                        break;
                }
            }
            ((this.field).getGame()).winCheck();
            return true;
        } else {
            System.out.println("HUIHUIHUI");
            return false;
        }
    }
    public boolean scoutAction(int teamOfScouter) {
        if ( this.levelOfControl[teamOfScouter] == 0 ) {
            this.levelOfControl[teamOfScouter] = 1;
            return true;
        } else {
            return false;
        }
    }

    public int measureDistanceTo(Square otherSquare) {
        int xDifference = this.xCoord - otherSquare.getX();
        int yDifference = this.yCoord - otherSquare.getY();
        int directionIndicator = xDifference * yDifference;

        if (directionIndicator >= 0) {
            return Math.max( Math.abs(xDifference), Math.abs(yDifference) );
        } else {
            return Math.abs(xDifference) + Math.abs(yDifference);
        }
    }

    public void printInfo() {
        System.out.print("X:" + this.xCoord + ", Y:" + this.yCoord);
        System.out.print(", armor:" + this.armor + ", points:" + this.points);
        if (this.levelOfControl[Constants.TEAM_AMERICANS]!=0 || this.levelOfControl[Constants.TEAM_GERMANS]!=0) {
            System.out.print(", level of control:(");
            if ( this.levelOfControl[Constants.TEAM_AMERICANS] > 0 ) {
                System.out.print( "American:" + this.levelOfControl[Constants.TEAM_AMERICANS] );
                if ( this.levelOfControl[Constants.TEAM_GERMANS] > 0 ) {
                    System.out.print(", ");
                }
            }
            if ( this.levelOfControl[Constants.TEAM_GERMANS] > 0 ) {
                System.out.print( "German:" + this.levelOfControl[Constants.TEAM_GERMANS] );
            }
            System.out.print(")");
        }
        boolean squareSpawns[] = {false, false};
        for ( int i = 0; i < 2; i++ ) {
            for ( int j = 0; j < 5; j++ ) {
                if ( this.spawns[i][j] == true ) {
                    squareSpawns[i] = true;
                    break;
                }
            }
        }
        for ( int i = 0; i < 2; i++ ) {
            if ( squareSpawns[i] == true ) {
                System.out.print( ", spawns:(" );
                if (this.spawns[i][0] == true) System.out.print( Constants.teamName(i) + " A squad, " );
                if (this.spawns[i][1] == true) System.out.print( Constants.teamName(i) + " B squad, " );
                if (this.spawns[i][2] == true) System.out.print( Constants.teamName(i) + " C squad, " );
                if (this.spawns[i][3] == true) System.out.print( Constants.teamName(i) + " mortar, " );
                if (this.spawns[i][4] == true) System.out.print( Constants.teamName(i) + " snipers, " );
                System.out.print( ")" );
            }
        }
        System.out.print( "\n" );
    }
}
