package normandyPack.board;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;
import normandyPack.bots.*;

public class Field {
    private Square grid[][];
    private int xSize, ySize;
    private Game game;

    public Field(int xSize, int ySize, Game game) {
        this.grid = new Square[ySize][xSize];
        this.xSize = xSize;
        this.ySize = ySize;
        this.game = game;
    }

    public int getXsize() {
        return this.xSize;
    }
    public int getYsize() {
        return this.ySize;
    }
    public Square getSquare(int x, int y) {
        if ( x < xSize && y < ySize && x >= 0 && y >= 0 ) return this.grid[y][x];
        return null;
    }
    public Game getGame() {
        return this.game;
    }

    public Square findSpawn(Card card) {
        int index = 0;

        if (card.getSquad() != Constants.NO_SQUAD) {
            switch ( card.getSquad() ) {
                case (Constants.SQUAD_A):
                    index = 0;
                    break;
                case (Constants.SQUAD_B):
                    index = 1;
                    break;
                case (Constants.SQUAD_C):
                    index = 2;
                    break;
            }
        } else {
            switch (card.getName()) {
                case ("Mortar"):
                    index = 3;
                    break;
                case ("Sniper"):
                    index = 4;
                    break;
            }
        }

        for ( int y = 0; y < ySize; y++ ) {
            for ( int x = 0; x < xSize; x++ ) {
                if ( this.grid[y][x] != null ) {
                    if ( ((this.grid[y][x]).getSpawns())[card.getTeam()][index] == true ) {
                        return this.grid[y][x];
                    }
                }
            }
        }

        System.out.println("HUIHUIHUI");
        return null;
    }

    public void addSquare(int xCoord, int yCoord, int armor, int points, int controlInfo, int spawnInfo) {
        this.grid[yCoord][xCoord] = new Square(xCoord, yCoord, armor, points, controlInfo, spawnInfo, this);
    }

    public void printInfo() {
        for ( int y = 0; y < ySize; y++ ) {
            for ( int x = 0; x < xSize; x++ ) {
                if ( this.grid[y][x] != null ) {
                    (this.grid[y][x]).printInfo();
                }
            }
        }
    }

    public String getBotInfo(int team) {
        String output = "";

        for ( int y = 0; y < ySize; y++ ) {
            for ( int x = 0; x < xSize; x++ ) {
                if ( this.grid[y][x] != null ) {
                    output += (this.grid[y][x]).getRawArmor();
                    output += (this.grid[y][x]).getPoints();
                    output += (this.grid[y][x]).getLevelOfControl(Constants.otherTeam(team));
                    output += (this.grid[y][x]).getLevelOfControl(team);
                } else output += "9999";
            }
            for ( int x = xSize; x < 6; x++ ) {
                output += "9999";
            }
        }
        for ( int y = ySize; y < 7; y++ ) {
            for ( int x = 0; x < 6; x++ ) {
                output += "9999";
            }
        }

        return output;
    }
    public String getBotSpawns(int team) {
        String output = "";
        int[] xCoords = {9,9,9,9,9};
        int[] yCoords = {9,9,9,9,9};
        boolean[][] spawnsBuffer;

        for ( int y = 0; y < ySize; y++ ) {
            for ( int x = 0; x < xSize; x++ ) {
                if ( this.grid[y][x] != null ) {
                    spawnsBuffer = (this.grid[y][x]).getSpawns();
                    for ( int i = 0; i < 5; i++ ) {
                        if (spawnsBuffer[team][i] == true) {
                            xCoords[i] = x;
                            yCoords[i] = y;
                        }
                    }
                }
            }
        }

        for ( int i = 0; i < 5; i++ ) {
            output += xCoords[i];
            output += yCoords[i];
        }

        return output;
    }
}
