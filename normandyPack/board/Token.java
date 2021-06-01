package normandyPack.board;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;
import normandyPack.bots.*;


public class Token {
    private int team, squad, armor;
    private boolean suppressed;
    private String type;
    private Square square, mortarTarget;

    public Token(int team, String type, int squad, Square square) {
        this.team = team;
        this.type = type;
        this.squad = squad;
        this.suppressed = false;
        this.square = square;
        this.mortarTarget = null;

        switch (type) {
            case ("Rifleman"):
                this.armor = 4;
                break;
            case ("Scout"):
                this.armor = 5;
                break;
            case ("Machine Gunner"):
                this.armor = 4;
                break;
            case ("Mortar"):
                this.armor = 4;
                break;
            case ("Sniper"):
                this.armor = 6;
                break;
        }

        (((this.square).getField()).getGame()).addToken(this, this.team);
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
    public int getArmor() {
        return this.armor;
    }
    public boolean getSuppressed() {
        return this.suppressed;
    }
    public Square getSquare() {
        return this.square;
    }
    public Square getMortarTarget() {
        return this.mortarTarget;
    }

    public void setSquare(Square square) {
        this.square = square;
    }
    public void setMortarTarget(Square mortarTarget) {
        this.mortarTarget = mortarTarget;
    }
    public void setSuppressed(boolean suppressing) {
        this.suppressed = suppressing;
    }

    public void moveTo(Square square) {
        (this.square).deleteToken(this);
        square.addToken(this);
    }
    public void deletDis() {
        (this.square).deleteToken(this);
        (((this.square).getField()).getGame()).deleteToken(this, this.team);
        (((this.square).getField()).getGame()).pinCheck();
    }

    public void printInfo() {
        System.out.print("at:(" + (this.square).getX() + "," + (this.square).getY() + ")");
        System.out.print( " " + Constants.teamName(this.team) + " " );
        System.out.print( this.type );
        switch ( this.squad ) {
            case (Constants.NO_SQUAD):
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
        if (this.mortarTarget != null) {
            System.out.print(" targeting:(" + (this.mortarTarget).getX() + ","
            + (this.mortarTarget).getY() + ")");
        }
        System.out.print("\n");
    }
}
