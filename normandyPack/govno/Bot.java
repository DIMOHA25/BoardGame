abstract public class Bot {
    protected String input, output, name;

    public Bot() {
        //
    }

    public String getInput() {
        return this.input;
    }
    public String getOutput() {
        return this.output;
    }
    public String getName() {
        return this.name;
    }
    public boolean getRand() {
        return false;
    }

    public void formulateInput(int[][] grid) {
        this.input = "";

        for ( int i = 0; i < 3; i++ ) {
            for ( int j = 0; j < 3; j++ ) {
                this.input += 1+grid[i][j];
            }
        }

        this.calculateOutput();
    }
    protected void calculateOutput() {
        //
    }

    public int x() {
        return Integer.parseInt(this.output.substring(0,1));
    }
    public int y() {
        return Integer.parseInt(this.output.substring(1,2));
    }

    public void saveWeights() {
        //
    }
    public void importWeights() {
        //
    }
    public void rewardNactions(double reward, int actionCount) {
        //
    }
    public void printWeightsNBiases() {
        //
    }
}
