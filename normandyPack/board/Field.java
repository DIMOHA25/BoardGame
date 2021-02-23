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
        return this.grid[y][x];
    }
    public Game getGame() {
        return this.game;
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
