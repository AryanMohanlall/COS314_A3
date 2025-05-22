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

    public GP(long seed, String file){
        this.best = null;
        this.popualtion = new Vector<>();
        this.seed = seed;
        this.data = new float[998][6];

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

/*     public void crossover(SyntaxTree tree1, SyntaxTree tree2){
        Node temptree = null;
    } */

    public SyntaxTree GeneticProgramming(ArrayList<SyntaxTree> pop, int generationNum, double crossoverRate, double mutationRate, ArrayList<double[]> vals, ArrayList<Integer> valsLabels){
        Random rand = new Random();
        SyntaxTree best=null;

        for(int i=0; i<generationNum; i++){
            for(int j=0; j<pop.size();j++){
                //double fit = fitness(pop.get(j), vals, valsLabels);
                //pop.get(j).setFitness(fit);
                fitnessFunction(pop.get(i), vals);
                System.out.println("new fitness="+pop.get(i).getFitness());

            }

            pop.sort(Comparator.comparingDouble(SyntaxTree::getFitness).reversed());
            if(best==null || pop.get(0).fitness > best.fitness){
                best = pop.get(0).clone();
                System.out.println("generation"+i+" best fitness:"+best.fitness);
            }

            ArrayList<SyntaxTree> newPop = new ArrayList<>();
            newPop.add(best.clone());

            while(newPop.size() < pop.size()){
                SyntaxTree p1 = selection(pop, 5, rand);
                SyntaxTree p2 = selection(pop, 5 , rand);

                SyntaxTree child;
                if(rand.nextDouble() < crossoverRate){
                    child = p1.crossover(p2, rand);
                }
                else{
                    child = p1.clone();
                }

                if(rand.nextDouble()<mutationRate){
                    child.Mutation(/* rand.nextLong() */ seed+1);
                }
                newPop.add(child);
            }
            pop = newPop;
        }
        return best;
    }// algorithm

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

    private SyntaxTree selection(ArrayList<SyntaxTree> pop, int size, Random rand){
        SyntaxTree best = null;
        for(int i=0; i<size; i++){
            SyntaxTree j = pop.get(rand.nextInt(pop.size()));
            if(best==null || j.fitness<best.fitness){
                best=j;
            }
        }
        return best.clone();
    }

    public void fitnessFunction(SyntaxTree tree, ArrayList<double[]> data){
        for(int i=0; i<data.size();i++){
            double[] input = data.get(i);
            float val = tree.compute((int) input[0], (int) input[1], (int) input[2], (int) input[3], (int) input[4]);
            float fit = (float) (Math.abs(input[4] - val) / Math.max(val, input[4]));
            if(Float.isInfinite(fit) || Float.isNaN(fit)) fit = val;    
            //System.out.println("fitness="+ fit);
            if(fit < tree.getFitness())tree.setFitness(fit);
            //System.out.println("new fitness="+tree.getFitness());
            break;
        }
    }   
}
