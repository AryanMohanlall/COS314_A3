import java.util.ArrayList;
class Main{
    public static void main(String[] args) {
        Node plus = new FunctionNode(null, null, FunctionSet.PLUS);
        Node minus = new FunctionNode(null, null, FunctionSet.MINUS);
        Node multiply = new FunctionNode(null, null, FunctionSet.MULTIPLY);
        Node divide = new FunctionNode(null, null, FunctionSet.DIVIDE);

        Node one = new TerminalNode(null, null, 1);
        Node two = new TerminalNode(null, null, 2);
        Node three = new TerminalNode(null, null, 3);

        Node a = new VariableNode(null, null, 'a');
        Node b = new VariableNode(null, null, 'b');
        Node c = new VariableNode(null, null, 'c');

        long seed = System.nanoTime();

        SyntaxTree tree = new SyntaxTree(seed, 10);

        tree.buildSyntaxTree(seed);

        System.out.println(tree.interpret(tree.getRoot(), ""));

        System.out.println("compute="+tree.compute(1,2,3,4,5));
        System.out.println("After mutation");
        tree.Mutation(seed);

        System.out.println(tree.interpret(tree.getRoot(), ""));
        System.out.println("compute="+tree.compute(1,2,3,4,5));

        System.out.println("Valid?"+tree.validTree());

        System.out.println("depth="+tree.depth(tree.root));

        SyntaxTree singleTree = new SyntaxTree(minus);
        singleTree.append(c, singleTree.root);
        singleTree.append(one, singleTree.root);
        singleTree.append(minus, singleTree.root);
        singleTree.append(b, singleTree.root);
        singleTree.append(minus, singleTree.root);
        singleTree.append(three, singleTree.root);

        System.out.println(singleTree.interpret(singleTree.root, ""));
        System.out.println("compute="+singleTree.compute(1, 2, 3, 4, 5));

        //edge c - -1.7221136 - b - -7.300377E-4


        ////////////////////////////////////////////////////////////////
        System.out.println("GP Test\n");
        GP genetic_program = new GP(seed, "BTC_train.csv");
        
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
        System.out.println("Test compute: " + bestTree.compute(1, 2, 3, 4, 5));
    }
}