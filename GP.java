import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class GP {
    public Vector<SyntaxTree> popualtion;
    public SyntaxTree best;
    public long seed;
    public float data[][];
    public float accuracy;
    public float f1;
    public ArrayList<SyntaxTree> popu;
    public long execution;
    public String file;

    public GP(long seed, String file){
        // this.best = new SyntaxTree();
        // this.best.setFitness(99);
        this.best=null;
        this.popualtion = new Vector<>();
        this.seed = seed;
        this.data = new float[998][6];
        this.file = file;

        int row = 0;
        int col = 0;
        try(Scanner scanner = new Scanner(new File(file))){
            if(scanner.hasNextLine()){
                scanner.nextLine();
            }
            while(scanner.hasNextLine()){
                //if(row==0){scanner.nextLine();}

                String line = scanner.nextLine();
                String values[] = line.split(",");
                for(int i=0; i<values.length; i++){
                    this.data[row][i] = Float.parseFloat(values[i]);
                }
                row += 1;
             }
        scanner.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private double fitness(SyntaxTree tree, ArrayList<double[]> data, ArrayList<Integer> labels){
        int f =0;
        for(int i=0; i<data.size();i++){
            double[] input = data.get(i);
            float val = tree.compute((int) input[0], (int) input[1], (int) input[2], (int) input[3], (int) input[4]);
            if(Math.round(val) == labels.get(i)){
                f++;
            }
        }
        return (double) f/data.size();
    }

/*     public void fitnessFunction(SyntaxTree tree, ArrayList<double[]> data){
        for(int i=0; i<data.size();i++){
            double[] input = data.get(i);
            float val = tree.compute((int) input[0], (int) input[1], (int) input[2], (int) input[3], (int) input[4]);
            float fit = (float) (Math.abs(input[4] - val) / Math.max(val, input[4]));
            if(Float.isInfinite(fit) || Float.isNaN(fit)) fit = val;    
            //System.out.println("fitness="+ fit);
            if(fit < tree.getFitness())tree.setFitness(fit);
            //System.out.println("new fitness="+tree.getFitness());
            //break;
        }
    }    */

    public void fitnessFunction(SyntaxTree tree, ArrayList<double[]> data, ArrayList<Integer> labels) {
    int TP = 0, FP = 0, TN = 0, FN = 0;
    for (int i = 0; i < data.size(); i++) {
        double[] input = data.get(i);
        float val = tree.compute(input[0], input[0], input[0], input[0], input[0]);
        val = (float) (1 / (1 + Math.exp(-val))); 
        if (Float.isNaN(val)) val = 0.5f; 
        int predicted = (val >= 0.5f) ? 1 : 0;
        int actual = labels.get(i);

        if (predicted == actual) {
            if (predicted == 1) TP++;
            else TN++;
        } else {
            if (predicted == 1) FP++;
            else FN++;
        }
    }

    float accuracy = (float) (TP + TN) / (TP + TN + FP + FN);
    float precision = (TP == 0) ? 0 : (float) TP / (TP + FP);
    float recall = (TP == 0) ? 0 : (float) TP / (TP + FN);
    float f1 = (precision + recall == 0) ? 0 : 2 * (precision * recall) / (precision + recall);
    if(f1==0.0) f1=0.8f;
    // Use F1 score as the fitness
    if(f1!=0.0) tree.setFitness(f1);
}

private SyntaxTree selection(ArrayList<SyntaxTree> pop, int size, Random rand) {
    //SyntaxTree best = null;
    for (int i = 0; i < size; i++) {
        SyntaxTree candidate = pop.get(i);
        if (this.best == null || candidate.getFitness() < this.best.getFitness()) {
            if(candidate.getFitness()!=0.0) this.best = candidate;
        }
    }
    return this.best.clone();
}

public SyntaxTree GeneticProgramming(ArrayList<SyntaxTree> pop, int generationNum, double crossoverRate, double mutationRate, ArrayList<double[]> vals, ArrayList<Integer> valsLabels) {
    long start = System.nanoTime();
    Random rand = new Random(seed);
    //SyntaxTree best = null;

    for (int i = 0; i < generationNum; i++) {
        for (int j = 0; j < pop.size(); j++) {
            fitnessFunction(pop.get(j), vals, valsLabels);
            //System.out.println("Fitness (F1): " + pop.get(j).getFitness());
            //System.out.println("\r" + pop.get(i).interpret(pop.get(i).root, ""));
        }

        pop.sort((t1, t2) -> Double.compare(t2.getFitness(), t1.getFitness()));
        if (this.best == null || pop.get(0).getFitness() > this.best.getFitness()) {
            this.best = pop.get(0).clone();
            //System.out.println("Generation " + i + " Best F1: " + this.best.getFitness());
            //System.out.println("Generation " + i + " Best tree: " + this.best.interpret(best.root, ""));
        }

        ArrayList<SyntaxTree> newPop = new ArrayList<>();
        newPop.add(this.best.clone());

        while (newPop.size() < pop.size()) {
            SyntaxTree p1 = selection(pop, 5, rand);
            SyntaxTree p2 = selection(pop, 5, rand);

            SyntaxTree child = (rand.nextDouble() < crossoverRate) ? p1.crossover(p2, rand) : p1.clone();
            if (rand.nextDouble() < mutationRate) child.Mutation(rand.nextLong());

            if (child.validTree()) {
    newPop.add(child);
} else {
    newPop.add(p1.clone());
}
        }
        pop = newPop;
        this.popu = pop;
    }
    this.execution = System.nanoTime() - start;
    return this.best;
}



    public float newfitness(float a, float b){
        float res = (Math.abs(a-b) / Math.max(a, a));
        if(Float.isInfinite(res) || Float.isNaN(res)) res = 99;
        return res;
    }

    public float F1(){
        float TP=0, FP=0, FN=0;

        for(int i=0; i<998; i++){
            float ans = popu.get(i % 50).compute(data[i][0], data[i][0], data[i][0], data[i][0], data[i][0]);
            //System.out.println("ans="+ans);
            if((ans > data[i][0]) && (data[i][4] > data[i][0])){
                TP += 1;
            }else{
                FP += 1;
            }

            if((ans < data[i][0]) && (data[i][4] > data[i][0])){
                FN += 1;
            }
        }
        float precision = (TP / (TP + FP)) ;
        float recall = TP / (TP + FN);
        // System.out.println("tp="+TP);
        // System.out.println("fp="+FP);
        // System.out.println("fn="+FN);
        // System.out.println("precision="+precision);
        // System.out.println("recall="+recall);
        float res = 2 *((precision*recall) / (precision+recall));
        if(Float.isNaN(res)) res = 0f;

        return res;
    }

    public double Accuracy(){
        float TP=0, FP=0, FN=0, TN=0;

        for(int i=0; i<998; i++){
            float ans = popu.get(i % 50).compute(data[i][0], data[i][0], data[i][0], data[i][0], data[i][0]);
            //System.out.println("ans="+ans);
            if((ans > data[i][0]) && (data[i][4] > data[i][0])){
                TP += 1;
            }else{
                FP += 1;
            }

            if((ans < data[i][0]) && (data[i][4] > data[i][0])){
                FN += 1;
            }

            if((ans < data[i][0]) && (data[i][4] < data[i][0])){
                TN += 1;
            }
        }

        // System.out.println("tp="+TP);
        // System.out.println("fp="+FP);
        // System.out.println("fn="+FN);
        // System.out.println("tn="+TN);
        double res = (TP+TN) / (TP + TN + FN + FP);
        if(Double.isNaN(res)) res = 0d;

        return res;        
    }

    public void printReport() throws InterruptedException{
        System.out.println("=========================================Genetic Programming=========================================");
        System.out.println("Seed: " + this.seed);
        System.out.println("Data: " + this.file);
        System.out.println("Execution time: " + (this.execution/100000) + "(ns)");
        System.out.println("F1 Score: " + F1());
        System.out.println("Accuracy: " + Accuracy());
        //System.err.println("\r " + this.best.interpret(this.best.root, ""));
    }
}

