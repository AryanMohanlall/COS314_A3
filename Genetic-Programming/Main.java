import java.util.ArrayList;
class Main{
    public static void main(String[] args) {
        long seed = System.currentTimeMillis();
        ////////////////////////////////////////////////////////////////
        System.out.println("GP Test\n");

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
    SyntaxTree bestTree = gp.GeneticProgramming(population, 10, 0.7, 0.2, data, labels);
    
    // Output results
    System.out.println("\nBest Tree:");
    System.out.println(bestTree.interpret(bestTree.getRoot(), ""));
    System.out.println("F1 Score: " + gp.F1());
    System.out.println("Accuracy: " + gp.Accuracy());


 
    }}
