package normandyPack.board;
import normandyPack.board.*;
import normandyPack.cards.*;
import normandyPack.game.*;
import normandyPack.constantValues.*;

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
}
