import java.util.ArrayList;
class Main{
    public static void main(String[] args) {
        long seed = System.currentTimeMillis();
        ////////////////////////////////////////////////////////////////
        System.out.println("GP Test\n");
/*         GP genetic_program = new GP(seed, "BTC_train.csv");
        
        ArrayList<SyntaxTree> population = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            SyntaxTree t = new SyntaxTree(seed + i, 5);
            t.buildSyntaxTree(seed + i);
            population.add(t);
        }
        
        ArrayList<double[]> data = new ArrayList<>();
        ArrayList<Integer> labels = new ArrayList<>();
        for (int i = 0; i < genetic_program.data.length; i++) {
            double[] inputs = new double[5];
            for (int j = 0; j < 5; j++) {
                inputs[j] = genetic_program.data[i][j];
            }
            data.add(inputs);
            labels.add((int)genetic_program.data[i][5]);
        }
        
        SyntaxTree bestTree = genetic_program.GeneticProgramming(
            population, 
            20,0.7,0.1,data, labels);
        
        System.out.println("\nbest tree:");
        System.out.println(bestTree.interpret(bestTree.getRoot(), ""));
        System.out.println("Fitness: " + bestTree.getFitness());
        System.out.println("Test compute: " + bestTree.compute(1, 2, 3, 4, 5));  */



/*     GP gpfornow = new GP(seed, "BTC_train.csv");
    SyntaxTree syn = new SyntaxTree(seed, 10);
    syn.buildSyntaxTree(seed+1);
    float val = syn.compute(0.974887187,0.926589953,0.983147606,0.968275672, -0.073907443);
    System.out.println("compute="+val);
    System.out.println("fitnessfromeq="+Math.abs(-0.073907443 - val) / Math.max(val, -0.073907443));

        ArrayList<double[]> data = new ArrayList<>();
        ArrayList<Integer> labels = new ArrayList<>();
        for (int i = 0; i < gpfornow.data.length; i++) {
            double[] inputs = new double[5];
            for (int j = 0; j < 5; j++) {
                inputs[j] = gpfornow.data[i][j];
            }
            data.add(inputs);
            labels.add((int)gpfornow.data[i][5]);
        }

        gpfornow.fitnessFunction(syn, data);
        System.out.println("fitnessaftersetting="+syn.fitness);
    } */
   //long seed = System.currentTimeMillis();
    GP gp = new GP(seed, "BTC_test.csv");
    
    // Initialize population
    ArrayList<SyntaxTree> population = new ArrayList<>();
    for (int i = 0; i < 50; i++) {
        SyntaxTree t = new SyntaxTree(seed + i, 5);
        t.buildSyntaxTree(seed + i);
        population.add(t);
    }
    
    // Prepare data and labels
    ArrayList<double[]> data = new ArrayList<>();
    ArrayList<Integer> labels = new ArrayList<>();
    for (int i = 0; i < gp.data.length; i++) {
        double[] inputs = new double[5];
        for (int j = 0; j < 5; j++) inputs[j] = gp.data[i][j];
        data.add(inputs);
        labels.add((int) gp.data[i][5]);
    }
    
    // Run GP
    SyntaxTree bestTree = gp.GeneticProgramming(population, 4, 0.7, 0.2, data, labels);
    
    // Output results
    System.out.println("\nBest Tree:");
    System.out.println(bestTree.interpret(bestTree.getRoot(), ""));
    System.out.println("Fitness (F1): " + bestTree.getFitness());
    }}
