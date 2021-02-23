package normandyPack.board;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;


public class Token {
    private int xCoord, yCoord, team, squad;
    private boolean suppressed;
    private String type;
    private Square square;

    public Token(int xCoord, int yCoord, int team, String type, Square square) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.team = team;
        this.type = type;
        this.squad = -1;
        this.suppressed = false;
        this.square = square;

        (((this.square).getField()).getGame()).addToken(this, this.team);
    }
    public Token(int xCoord, int yCoord, int team, String type, int squad, Square square) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.team = team;
        this.type = type;
        this.squad = squad;
        this.suppressed = false;
        this.square = square;

        (((this.square).getField()).getGame()).addToken(this, this.team);
    }

    public int getX() {
        return this.xCoord;
    }
    public int getY() {
        return this.yCoord;
    }
    public int getTeam() {
        return this.team;
    }
    public String getType() {
        return this.type;
    }
    public int getSquad() {
        return this.squad;
    }
    public boolean getSuppressed() {
        return this.suppressed;
    }
    public Square getSquare() {
        return this.square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    // public void move(Square square) {
    //     (this.square).deleteToken(this);
    //     square.addToken(this);
    // }
    public void deletDis() {
        (this.square).deleteToken(this);
        (((this.square).getField()).getGame()).deleteToken(this, this.team);
    }

    public void printInfo() {
        System.out.print("at:(" + this.getX() + "," + this.getY() + ")");
        System.out.print( " " + this.getTeam() + "\'s " );
        System.out.print( this.getType() );
        switch ( this.getSquad() ) {
            case (-1):
                break;
            case (Constants.SQUAD_A):
                System.out.print(" A");
                break;
            case (Constants.SQUAD_B):
                System.out.print(" B");
                break;
            case (Constants.SQUAD_C):
                System.out.print(" C");
                break;
        }
        System.out.print("\n");
    }
}
