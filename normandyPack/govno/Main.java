import java.util.Scanner;

public class Main{
    public static int checkWin(int[][] grid) {
        int winner = 0;
        int first;
        boolean changed;

        for ( int i = 0; i < 3; i++ ) {
            changed = false;
            first = grid[i][0];
            for ( int j = 1; j < 3; j++ ) {
                if (first != grid[i][j]) changed = true;
            }
            if (!changed && first != 0) {
                // System.out.println( "stroka " + (i+1) );
                return first;
            }
        }

        for ( int i = 0; i < 3; i++ ) {
            changed = false;
            first = grid[0][i];
            for ( int j = 1; j < 3; j++ ) {
                if (first != grid[j][i]) changed = true;
            }
            if (!changed && first != 0) {
                // System.out.println( "stolbets " + (i+1) );
                return first;
            }
        }

        changed = false;
        first = grid[0][0];
        for ( int i = 1; i < 3; i++ ) {
            if (first != grid[i][i]) changed = true;
        }
        if (!changed && first != 0) {
            // System.out.println( "LV-PN diagonal" );
            return first;
        }

        changed = false;
        first = grid[0][2];
        for ( int i = 1; i < 3; i++ ) {
            if (first != grid[i][2-i]) changed = true;
        }
        if (!changed && first != 0) {
            // System.out.println( "PV-LN diagonal" );
            return first;
        }

        // System.out.println( "nihuya" );
        return 0;
    }
    
    public static int[][] getEmptyGrid() {
        int[][] emptyGrid = new int[3][3];
        for ( int i = 0; i < 3; i++ ) {
            for ( int j = 0; j < 3; j++ ) {
                emptyGrid[i][j] = 0;
            }
        }

        return emptyGrid;
    }
    
    public static String getSymbol(int value) {
        switch (value) {
            case (-1):
                return "X";
            case (0):
                return "-";
            case (1):
                return "O";
        }
        return "PIZDETS VSYO GORIT WIURFJRNKJGNKRJGNKRMLKJV";
    }
    public static void printGrid(int[][] grid) {
        System.out.print("\n");
        for ( int i = 0; i < 3; i++ ) {
            for ( int j = 0; j < 3; j++ ) {
                System.out.print( getSymbol(grid[i][j]) );
            }
            System.out.print("\n");
        }
    }

    public static int enterIntFromRange(int min, int max) {
        Scanner scanner = new Scanner(System.in);
        int i;

        if (min > max) {
            System.out.println("HUIHUIHUI");
            return -1;
        }

        while(true) {
            if ( scanner.hasNextInt() ) {
                i = scanner.nextInt();
                if ( i >= min && i <= max ) {
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
        scanner.nextLine();//nuzhno(?)

        return i;
    }
    
    public static boolean tryPlacing(int x, int y, int value, int[][] grid, Bot bot) {
        if ( value == -1 || value == 1 ) {
            if ( x < 0 || y < 0 || x > 2 || y > 2 ) {
                System.out.println("coordinate error");
                //tut mozhno dat pizdi botu
                return false;
            }
            if ( grid[y][x] != 0 ) {
                // System.out.println("already occupied");
                bot.rewardNactions(-1.0,1);
                return false;
            } else {
                grid[y][x] = value;
                return true;
            }
        } else {
            System.out.println("U WOT?!");
            return false;
        }
    }

    public static boolean isFull(int[][] grid) {
        for ( int i = 0; i < 3; i++ ) {
            for ( int j = 0; j < 3; j++ ) {
                if (grid[i][j] == 0) return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int[][] grid = getEmptyGrid();
        int[] wins = {0,0};
        int winner = 0, x, y, mode, numberOfGames = -1;
        boolean toBeContinued = true, numberedGames = false;
        Bot bots[] = {null, null};

        System.out.println("1-PvP\n2-PvE\n3-EvP\n4-EvE");
        mode = enterIntFromRange(1,4);
        if (mode >= 2 && mode != 3) bots[1] = new KNBot(false);
        if (mode >= 3) bots[0] = new KNBot(true);
        if (mode == 4) {
            numberedGames = true;
            System.out.println("numberOfGames:");
            numberOfGames = enterIntFromRange(1,1000000000);
        }

        while (toBeContinued) {
            while (winner == 0 && !isFull(grid)) {
                if (bots[0] == null) {
                    do {
                        printGrid(grid);
                        System.out.println("enter x coordinate for X");
                        x = enterIntFromRange(0,2);
                        System.out.println("enter y coordinate for X");
                        y = enterIntFromRange(0,2);
                    } while ( !tryPlacing(x,y,-1,grid,null) );
                } else {
                    do {
                        if ( !numberedGames ) printGrid(grid);
                        bots[0].formulateInput(grid);
                        x = bots[0].x();
                        y = bots[0].y();
                    } while ( !tryPlacing(x,y,-1,grid,bots[0]) );
                }
                winner = checkWin(grid);
                //
                if (winner == 0 && !isFull(grid)) {
                    if (bots[1] == null) {
                        do {
                            printGrid(grid);
                            System.out.println("enter x coordinate for O");
                            x = enterIntFromRange(0,2);
                            System.out.println("enter y coordinate for O");
                            y = enterIntFromRange(0,2);
                        } while ( !tryPlacing(x,y,1,grid,null) );
                    } else {
                        do {
                            if ( !numberedGames ) printGrid(grid);
                            bots[1].formulateInput(grid);
                            x = bots[1].x();
                            y = bots[1].y();
                        } while ( !tryPlacing(x,y,1,grid,bots[1]) );
                    }
                    winner = checkWin(grid);
                }
            }

            if ( !numberedGames ) {
                printGrid(grid);
                System.out.println( "pobedili:" + getSymbol(winner) );
            }

            switch (winner) {
                case (-1):
                    wins[0] += 1;
                    if (bots[0] != null) bots[0].rewardNactions(1.0,-1);
                    if (bots[1] != null) bots[1].rewardNactions(-1.0,-1);
                    break;
                case (0):
                    if (bots[0] != null) bots[0].rewardNactions(0.25,-1);
                    if (bots[1] != null) bots[1].rewardNactions(0.25,-1);
                    break;
                case (1):
                    wins[1] += 1;
                    if (bots[0] != null) bots[0].rewardNactions(-1.0,-1);
                    if (bots[1] != null) bots[1].rewardNactions(1.0,-1);
                    break;
            }


            winner = 0;
            grid = getEmptyGrid();
            if ( numberedGames ) {
                numberOfGames -= 1;
                if ( numberOfGames <= 0 ) toBeContinued = false;
            } else {
                System.out.println("do you want to continue? 0/1");
                toBeContinued = 1 == enterIntFromRange(0,1);
                System.out.println("\n\n\n");
            }
        }

        System.out.println("X wins :"+wins[0]);
        System.out.println("O wins :"+wins[1]);
        if (bots[0] != null) bots[0].saveWeights();
        if (bots[1] != null) bots[1].saveWeights();
    }
}
