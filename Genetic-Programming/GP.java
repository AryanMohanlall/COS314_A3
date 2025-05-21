import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

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
            while(scanner.hasNextLine()){
                if(row==0){scanner.nextLine();}

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

    public void crossover(SyntaxTree tree1, SyntaxTree tree2){
        Node temptree = null;
    }

    public void GeneticProgramming(){


    }// algorithm

}
