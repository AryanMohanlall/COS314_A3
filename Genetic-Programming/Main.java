import java.util.ArrayList;
//import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;

class Main{
    public static void main(String[] args) throws InterruptedException {
        long seed = System.currentTimeMillis();
        ////////////////////////////////////////////////////////////////
        System.out.println("GP Train\n");

    GP gpTrain = new GP(seed, "BTC_train.csv");
    
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
    for (int i = 0; i < gpTrain.data.length; i++) {
        double[] inputs = new double[5];
        for (int j = 0; j < 5; j++) inputs[j] = gpTrain.data[i][j];
        data.add(inputs);
        labels.add((int) gpTrain.data[i][5]);
    }
    
    // Run gpTrain
    SyntaxTree bestTree = gpTrain.GeneticProgramming(population, 10, 0.7, 0.2, data, labels);
    
    // Output results
    System.out.println("Seed: " + seed);
    System.out.println("Best Tree:" + bestTree.interpret(bestTree.getRoot(), ""));
    System.out.println("F1 Score: " + gpTrain.F1());
    System.out.println("Accuracy: " + gpTrain.Accuracy());


 System.out.println("\nGP Test\n");

    GP gpTest = new GP(seed, "BTC_test.csv");
    
    // Initialize population
    //ArrayList<SyntaxTree> population = new ArrayList<>();
    for (int i = 0; i < 50; i++) {
        SyntaxTree t = new SyntaxTree(seed + i+1, 5);
        t.buildSyntaxTree(seed + i+i);
        population.add(t);
    }
    
    // Prepare data and labels
    data = new ArrayList<>();
    labels = new ArrayList<>();
    for (int i = 0; i < gpTest.data.length; i++) {
        double[] inputs = new double[5];
        for (int j = 0; j < 5; j++) inputs[j] = gpTest.data[i][j];
        data.add(inputs);
        labels.add((int) gpTest.data[i][5]);
    }
    
    // Run gpTest
    bestTree = gpTest.GeneticProgramming(population, 10, 0.7, 0.2, data, labels);
    
    // Output results
    System.out.println("Seed: " + seed);
    System.out.println("Best Tree:" + bestTree.interpret(bestTree.getRoot(), ""));
    System.out.println("F1 Score: " + gpTest.F1());
    System.out.println("Accuracy: " + gpTest.Accuracy());

    for(int i=0;i<100;i++){
        //System.out.print("\033[H\033[2J");
        //System.out.flush();
        System.out.print("\r" + gpTest.popu.get(i).interpret(gpTest.popu.get(i).root, ""));

        Thread.sleep(100); // 500 milliseconds
    }

    

    }
}
