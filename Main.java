import java.util.ArrayList;
//import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Main{
    public static void main(String[] args) throws InterruptedException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("MLP Stock Predictor");
        System.out.println("-------------------");

        // Get seed value
        System.out.print("Enter random seed: ");
        long seed = scanner.nextLong();
        scanner.nextLine(); // Consume newline

        // Get file paths
        System.out.print("Enter full path to training file: ");
        String trainPath = scanner.nextLine().trim();

        System.out.print("Enter full path to test file: ");
        String testPath = scanner.nextLine().trim();

        // Load data
        System.out.println("\nLoading training data...");
        List<double[]> trainData = DataLoader.loadData(trainPath);
        System.out.println("Loading test data...");
        List<double[]> testData = DataLoader.loadData(testPath);

        // Verify data
        if (trainData.isEmpty() || testData.isEmpty()) {
            System.err.println("Error: Could not load data. Exiting.");
            System.exit(1);
        }

        // Normalize
        System.out.println("\nNormalizing data...");
        DataLoader.normalizeTrain(trainData);
        DataLoader.normalizeTest(testData);

        // Initialize network
        System.out.println("Initializing MLP...");
        MLP mlp = new MLP(
                5,
                10,
                1,
                0.01,
                seed);

        // Training
        System.out.println("Training network (1000 epochs)...");
        mlp.train(trainData, 1000);

        // Results
        System.out.println("\nEvaluation Results:");
        printMetrics("Training", mlp.evaluate(trainData));
        printMetrics("Testing", mlp.evaluate(testData));

        scanner.close();


        ////////////////////////////////////////////////////////////////////////////////////
    seed = System.currentTimeMillis();

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
    double[] y = {mlp.evaluate(trainData).get("accuracy"), mlp.evaluate(testData).get("accuracy")};
    WilcoxonTest wt = new WilcoxonTest(x, y);

    double pValue = wt.wilcoxonSignedRankTest(x, y);
    System.out.println("=========================================Wilcoxon Test=========================================");
    System.out.println("pValue: "+pValue);
    System.out.println("Reject Null Hypothesis: " + (pValue < 0.5));
    if(pValue < 0.5)System.out.println("Thus MLP and GP perform very differenly");
    if(pValue > 0.5)System.out.println("Thus MLP and GP perform very similalry");

    }


    private static void printMetrics(String prefix, Map<String, Double> metrics) {
        System.out.println("\n" + prefix + " Results:");
        System.out.printf("Accuracy: %.2f%%%n", metrics.get("accuracy") * 100);
        System.out.printf("F1 Score: %.3f%n", metrics.get("f1"));
        System.out.printf("Precision: %.3f%n", metrics.get("precision"));
        System.out.printf("Recall: %.3f%n%n", metrics.get("recall"));
    }
}
