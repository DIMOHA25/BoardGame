package normandyPack.board;
import java.util.ArrayList;
import java.lang.Math;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;

public class Square {
    private int xCoord, yCoord, armor, points, levelOfControl[] = {0,0};
    //                       A,    B,    C, Mortar,Snipers
    private boolean spawns[][] = {{false,false,false,false,false},{false,false,false,false,false}};
    private boolean targeting;
    private int targetX, targetY;
    private Field field;
    private ArrayList<Token> tokensInside;

    public Square(int xCoord, int yCoord, int armor, int points, int controlInfo, int spawnInfo, Field field) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.armor = armor;
        this.points = points;
        this.targeting = false;
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
    public Field getField() {
        return this.field;
    }

    public void addToken(int team, String type) {
        (this.tokensInside).add( new Token(this.xCoord, this.yCoord, team, type, this) );
    }
    public void addToken(int team, String type, int squad) {
        (this.tokensInside).add( new Token(this.xCoord, this.yCoord, team, type, squad, this) );
    }
    public void addToken(Token token) {
        (this.tokensInside).add(token);
        token.setSquare(this);
    }
    public void deleteToken(Token token) {
        (this.tokensInside).remove(token);
    }

    public int measureDistance(Square otherSquare) {
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
                if (this.spawns[i][0] == true) System.out.print( i + "\'s A squad" );
                if (this.spawns[i][0] == true) System.out.print( ", " + i + "\'s B squad" );
                if (this.spawns[i][0] == true) System.out.print( ", " + i + "\'s C squad" );
                if (this.spawns[i][0] == true) System.out.print( ", " + i + "\'s snipers" );
                if (this.spawns[i][0] == true) System.out.print( ", " + i + "\'s mortar" );
                System.out.print( ")" );
            }
        }
        System.out.print( "\n" );
    }
}
