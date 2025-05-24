import java.util.ArrayList;
//import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;

class Main{
    public static void main(String[] args) throws InterruptedException {
        long seed = System.currentTimeMillis();

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
    
    gpTrain.printReport();
    for(int i=0;i<gpTrain.popu.size();i++){
        System.out.print("\r" + gpTrain.popu.get(i).interpret(gpTrain.popu.get(i).root, ""));
        Thread.sleep(10); // 500 milliseconds
    }
    System.out.println("\rBest tree: " + bestTree.interpret(bestTree.root, ""));


///////////////////////////////////////////////////////////////////////////////////////////////
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
    gpTest.printReport();
     for(int i=0;i<gpTest.popu.size();i++){
        System.out.print("\r" + gpTest.popu.get(i).interpret(gpTest.popu.get(i).root, ""));
        Thread.sleep(10); // 500 milliseconds
    }
    System.out.println("\rBest tree: " + bestTree.interpret(bestTree.root, ""));

    System.out.println();
    double[] x = {gpTrain.Accuracy(), gpTest.Accuracy()};
    double[] y = {80, 60};
    WilcoxonTest wt = new WilcoxonTest(x, y);

    double pValue = wt.wilcoxonSignedRankTest(x, y);
    System.out.println("=========================================Wilcoxon Test=========================================");
    System.out.println("pValue: "+pValue);
    System.out.println("Reject Null Hypothesis: " + (pValue < 0.5));
    if(pValue < 0.5)System.out.println("Thus MLP and GP perform very differenly");
    if(pValue > 0.5)System.out.println("Thus MLP and GP perform very similalry");

    }
}
