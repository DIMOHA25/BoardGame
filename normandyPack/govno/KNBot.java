import java.lang.Math;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class KNBot extends Bot {
    protected String fileName = null;
    protected boolean rand;
    protected double discountRate = 0.9, learningRate = 0.001;
    protected final int[] sizes = {9,8,8,6};
    protected ArrayList<double[]> neurons = new ArrayList<double[]>();
    protected ArrayList<double[]> biases = new ArrayList<double[]>();
    protected ArrayList<double[][]> weights = new ArrayList<double[][]>();
    protected ArrayList<String> outputHistory = new ArrayList<String>();
    protected ArrayList<ArrayList<double[]>> neuronHistory = new ArrayList<ArrayList<double[]>>();

    public KNBot(boolean rand) {
        this.name = "Krestonol";
        if (rand) this.name += "(rand)";
        this.rand = rand;

        for ( int i = 0; i < this.sizes.length; i++ ) {
            this.neurons.add(new double[this.sizes[i]]);
        }
        for ( int i = 1; i < this.sizes.length; i++ ) {
            this.biases.add(new double[this.sizes[i]]);
        }
        for ( int i = 1; i < this.sizes.length; i++ ) {
            this.weights.add(new double[this.sizes[i]][this.sizes[i-1]]);
        }
        if (this.fileName == null) {
            this.fillWeights();
            this.saveWeights();
        } else this.importWeights();
    }

    public boolean getRand() {
        return this.rand;
    }

    protected double relu(double input) {
        if (input > 0) return input;
        else return 0;
    }
    protected double sigmoid(double input) {
        return 1.0 / ( 1.0 + Math.pow(Math.E,-input) );
    }
    protected double eToPower(double input) {
        return Math.exp(input);
    }
    protected void softmax(double[] outputs, int index1, int index2) {
        double sum = 0;

        for ( int i = index1; i < index2; i++ ) {
            sum += outputs[i];
        }
        for ( int i = index1; i < index2; i++ ) {
            outputs[i] /= sum;
            // if ( Double.isNaN(outputs[i]) ) System.out.println( "4HUI"+i );
        }
    }

    protected void fillWeights() {
        Random gen = new Random();
        double sigma;
        for ( int s = 1; s < this.sizes.length; s++ ) {
            sigma = Math.sqrt( 2.0 / (double)this.sizes[s-1] );//1 ili 2 hz
            for ( int i = 0; i < this.sizes[s]; i++ ) {
                for ( int j = 0; j < this.sizes[s-1]; j++ ) {
                    (this.weights.get(s-1))[i][j] = gen.nextGaussian()*sigma;
                }
            }
            for ( int i = 0; i < this.sizes[s]; i++ ) {
                (this.biases.get(s-1))[i] = 0.0;
            }
        }
    }
    public void saveWeights() {
        this.fileName = "" + this.sizes[0];
        for ( int i = 1; i < this.sizes.length; i++ ) {
            this.fileName += "-" + this.sizes[i];
        }
        this.fileName += "_"+(int)(Math.random()*1000000000)+""+(int)(Math.random()*1000000000);
        File file = new File(this.fileName+".txt");
        try {
            FileWriter writer = new FileWriter(file);

            System.out.println("creating "+this.fileName+".txt");
            file.createNewFile();

            for ( int s = 1; s < this.sizes.length; s++ ) {
                for ( int i = 0; i < this.sizes[s]; i++ ) {
                    for ( int j = 0; j < this.sizes[s-1]; j++ ) {
                        writer.write("" + (this.weights.get(s-1))[i][j] + "\n");
                    }
                }
                for ( int i = 0; i < this.sizes[s]; i++ ) {
                    writer.write("" + (this.biases.get(s-1))[i] + "\n");
                }
            }

            writer.close();
        } catch (Exception e) {
            System.out.println("HUUUUUUUUUUUUUUI1");
        }
    }
    public void importWeights() {
        File file = new File(this.fileName+".txt");
        try {
            Scanner scanner = new Scanner(file);

            for ( int s = 1; s < this.sizes.length; s++ ) {
                for ( int i = 0; i < this.sizes[s]; i++ ) {
                    for ( int j = 0; j < this.sizes[s-1]; j++ ) {
                        (this.weights.get(s-1))[i][j] = Double.parseDouble(scanner.nextLine());
                    }
                }
                for ( int i = 0; i < this.sizes[s]; i++ ) {
                    (this.biases.get(s-1))[i] = Double.parseDouble(scanner.nextLine());
                }
            }

            scanner.close();
        } catch (Exception e) {
            System.out.println("HUUUUUUUUUUUUUUI2");
        }
    }

    protected void calculateOutput() {
        if (this.rand) {
            this.output = "";
            this.output += (int)(Math.random()*3);
            this.output += (int)(Math.random()*3);
        } else {
            for ( int i = 0; i < this.sizes[0]; i++ ) {
                (this.neurons.get(0))[i] = Integer.parseInt(this.input.substring(i,i+1));
            }

            for ( int s = 1; s < this.sizes.length-1; s++ ) {
                for ( int i = 0; i < this.sizes[s]; i++ ) {
                    (this.neurons.get(s))[i] = 0;
                    for ( int j = 0; j < this.sizes[s-1]; j++ ) {
                        (this.neurons.get(s))[i] +=
                            (this.neurons.get(s-1))[j]*(this.weights.get(s-1))[i][j];
                    }
                    (this.neurons.get(s))[i] += (this.biases.get(s-1))[i];
                    (this.neurons.get(s))[i] = this.relu((this.neurons.get(s))[i]);
                }
            }
            int s = this.sizes.length-1;
            for ( int i = 0; i < this.sizes[s]; i++ ) {
                (this.neurons.get(s))[i] = 0;
                for ( int j = 0; j < this.sizes[s-1]; j++ ) {
                    (this.neurons.get(s))[i] += (this.neurons.get(s-1))[j]*(this.weights.get(s-1))[i][j];
                }
                // if ( Double.isNaN((this.neurons.get(s))[i]) ) System.out.println( "1HUI"+i );
                (this.neurons.get(s))[i] += (this.biases.get(s-1))[i];
                // if ( Double.isNaN((this.neurons.get(s))[i]) ) System.out.println( "2HUI"+i );
                (this.neurons.get(s))[i] = this.eToPower((this.neurons.get(s))[i]);
                // if ( Double.isNaN((this.neurons.get(s))[i]) ) System.out.println( "3HUI"+i );
            }
            this.softmax(this.neurons.get(s),0,this.sizes[s]/2);
            this.softmax(this.neurons.get(s),this.sizes[s]/2,this.sizes[s]);

            this.output = "";
            double sum = 0;
            double roll = Math.random();
            boolean success = false;
            // System.out.println( "x roll:" + roll );
            for ( int i = 0; i < this.sizes[s]/2; i++ ) {
                sum += (this.neurons.get(s))[i];
                // System.out.println( "x" + i + ":" + (this.neurons.get(s))[i] );
                if ( roll < sum ) {
                    this.output += i;
                    success = true;
                    break;
                }
            }
            // if (!success) this.printWeightsNBiases();
            sum = 0;
            roll = Math.random();
            success = false;
            // System.out.println( "y roll:" + roll );
            for ( int i = this.sizes[s]/2; i < this.sizes[s]; i++ ) {
                sum += (this.neurons.get(s))[i];
                // System.out.println( "y" + (i-this.sizes[s]/2) + ":" + (this.neurons.get(s))[i] );
                if ( roll < sum ) {
                    this.output += i-(this.sizes[s]/2);
                    success = true;
                    break;
                }
            }
            // if (!success) this.printWeightsNBiases();

            //kopiya nahui suka blyad
            ArrayList<double[]> archivedNeurons = new ArrayList<double[]>();
            //this.neurons
            for ( int i = 0; i < this.sizes.length; i++ ) {
                archivedNeurons.add(new double[this.sizes[i]]);
            }
            for ( int j = 0; j < this.sizes.length; j++ ) {
                for ( int i = 0; i < this.sizes[s]; i++ ) {
                    (archivedNeurons.get(j))[i] = (this.neurons.get(j))[i];
                }
            }

            this.neuronHistory.add(archivedNeurons);
            this.outputHistory.add(this.output);
        }
        // System.out.println(this.output);
    }

    public void rewardNactions(double reward, int actionCount) {
        ArrayList<double[]> biasCorrections = new ArrayList<double[]>();
        ArrayList<double[][]> weightCorrections = new ArrayList<double[][]>();
        int outSize = this.sizes[this.sizes.length-1];
        int histSize = this.outputHistory.size();
        // System.out.println( "       histSize:"+histSize );

        if ( actionCount < 1 || actionCount > histSize ) actionCount = histSize;

        //structure
        for ( int i = 1; i < this.sizes.length; i++ ) {
            biasCorrections.add(new double[this.sizes[i]]);
        }
        for ( int i = 1; i < this.sizes.length; i++ ) {
            weightCorrections.add(new double[this.sizes[i]][this.sizes[i-1]]);
        }

        for ( int cc = 0; cc < actionCount; cc++ ) {
            //debug
            // for ( int l = 0; l < outSize; l++ ) {
            //     System.out.println( "   output"+l+":"+
            //         (this.neuronHistory.get(histSize-1-cc).get(this.sizes.length-1))[l] );
            // }

            //loss & biases1
            for ( int l = 0; l < outSize/2; l++ ) {
                double prediction = -25.0;
                if ( l == Integer.parseInt(this.outputHistory.get(histSize-1-cc).substring(0,1)) ) {
                    prediction =
                        (this.neuronHistory.get(histSize-1-cc).get(this.sizes.length-1))[l];
                    // System.out.println( "prediction"+l+":"+prediction );
                    (this.neuronHistory.get(histSize-1-cc).get(this.sizes.length-1))[l] =
                        (-reward) * Math.pow(this.discountRate,cc) / prediction;
                    (biasCorrections.get(this.sizes.length-2))[l] =
                        prediction * (1.0-prediction) *
                        (this.neuronHistory.get(histSize-1-cc).get(this.sizes.length-1))[l];
                } else {
                    (this.neuronHistory.get(histSize-1-cc).get(this.sizes.length-1))[l] = 0.0;
                    (biasCorrections.get(this.sizes.length-2))[l] = 0.0;
                }
                if ( l == Integer.parseInt(this.outputHistory.get(histSize-1-cc).substring(1,2)) ) {
                    prediction =
                        (this.neuronHistory.get(histSize-1-cc).get(this.sizes.length-1))[l+(outSize/2)];
                    // System.out.println( "prediction"+(l+(outSize/2))+":"+prediction );
                    (this.neuronHistory.get(histSize-1-cc).get(this.sizes.length-1))[l+(outSize/2)] =
                        (-reward) * Math.pow(this.discountRate,cc) / prediction;
                    (biasCorrections.get(this.sizes.length-2))[l+(outSize/2)] =
                        prediction * (1.0-prediction) *
                        (this.neuronHistory.get(histSize-1-cc).get(this.sizes.length-1))[l+(outSize/2)];
                } else {
                    (this.neuronHistory.get(histSize-1-cc).get(this.sizes.length-1))[l+(outSize/2)] = 0.0;
                    (biasCorrections.get(this.sizes.length-2))[l+(outSize/2)] = 0.0;
                }
                // if ( Double.isNaN( 
                //     (this.neuronHistory.get(histSize-1-cc).get(this.sizes.length-1))[l] ) )
                //     System.out.println( "81HUI"+l );
                // if ( Double.isNaN( 
                //     (this.neuronHistory.get(histSize-1-cc).get(this.sizes.length-1))[l+(outSize/2)] ) )
                //     System.out.println( "82HUI"+(l+(outSize/2)) );
                // if ( Double.isNaN( (biasCorrections.get(this.sizes.length-2))[l] ) ) {
                //     System.out.println( "83HUI"+l );
                //     System.out.println( "PIDOR1:"+prediction );
                //     System.out.println( "PIDOR2:"+
                // (this.neuronHistory.get(histSize-1-cc).get(this.sizes.length-1))[l] );
                // }
                // if ( Double.isNaN( (biasCorrections.get(this.sizes.length-2))[l+(outSize/2)] ) ) {
                //     System.out.println( "84HUI"+(l+(outSize/2)) );
                //     System.out.println( "PIDOR1:"+prediction );
                //     System.out.println( "PIDOR2:"+
                // (this.neuronHistory.get(histSize-1-cc).get(this.sizes.length-1))[l+(outSize/2)] );
                // }
            }

            //backprop
            for ( int s = this.sizes.length-1; s >= 1; s-- ) {
                for ( int i = 0; i < this.sizes[s]; i++ ) {
                    for ( int j = 0; j < this.sizes[s-1]; j++ ) {
                        (weightCorrections.get(s-1))[i][j] =
                            (this.neuronHistory.get(histSize-1-cc).get(s-1))[j] *
                            (biasCorrections.get(s-1))[i];
                        // if ( Double.isNaN( (weightCorrections.get(s-1))[i][j] ) )
                        //     System.out.println( "7HUI"+s+"."+i+"."+j );
                    }
                }
                if ( s > 1 ) {
                    for ( int j = 0; j < this.sizes[s-1]; j++ ) {
                        if ( (this.neuronHistory.get(histSize-1-cc).get(s-1))[j] > 0.0 ) {
                            (biasCorrections.get(s-2))[j] =
                                (this.neuronHistory.get(histSize-1-cc).get(s-1))[j];
                        } else {
                            (biasCorrections.get(s-2))[j] = 0.0;
                        }
                        (this.neuronHistory.get(histSize-1-cc).get(s-1))[j] = 0.0;
                        for ( int i = 0; i < this.sizes[s]; i++ ) {
                            (this.neuronHistory.get(histSize-1-cc).get(s-1))[j] +=
                                (weightCorrections.get(s-1))[i][j] *
                                (biasCorrections.get(s-1))[i];
                        }
                    }
                }
            }

            //correction
            for ( int s = 1; s < this.sizes.length; s++ ) {
                for ( int i = 0; i < this.sizes[s]; i++ ) {
                    for ( int j = 0; j < this.sizes[s-1]; j++ ) {
                        (this.weights.get(s-1))[i][j] -= (weightCorrections.get(s-1))[i][j] *
                            this.learningRate/* / actionCount*/;//average optionally
                        // if ( Double.isNaN( (this.weights.get(s-1))[i][j] ) )
                        //     System.out.println( "5HUI"+s+"."+i+"."+j );
                    }
                }
                for ( int i = 0; i < this.sizes[s]; i++ ) {
                    (this.biases.get(s-1))[i] -= (biasCorrections.get(s-1))[i] *
                        this.learningRate/* / actionCount*/;//average optionally
                    // if ( Double.isNaN( (this.biases.get(s-1))[i] ) ) System.out.println( "6HUI"+s+"."+i );
                }
            }
        }

        this.outputHistory.clear();
        this.neuronHistory.clear();
    }

    public void printWeightsNBiases() {
        for ( int s = 0; s < this.sizes.length; s++ ) {
            System.out.println( "layer " + (s-1) + "-" + s + ":" );
            for ( int i = 0; i < this.sizes[s]; i++ ) {
                System.out.println( "\tneuron " + i + ":" );
                if (s > 0) {
                    System.out.println( "\t\tweights:" );
                    for ( int j = 0; j < this.sizes[s-1]; j++ ) {
                        System.out.println( "\t\t\t" + (this.weights.get(s-1))[i][j] );
                    }
                    System.out.println( "\t\tbias:" );
                    System.out.println( "\t\t\t" + (this.biases.get(s-1))[i] );
                }
                System.out.println( "\t\tvalue:" );
                System.out.println( "\t\t\t" + (this.neurons.get(s))[i] );
            }
        }
    }
}
