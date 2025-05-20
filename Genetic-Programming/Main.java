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

        SyntaxTree tree = new SyntaxTree(divide);

        //tree.append(one, tree.root);
        //tree.append(minus, tree.root);
        //tree.append(two, tree.root);
        //tree.append(plus, tree.root);
        //tree.append(a, tree.root);

        tree.buildSyntaxTree(System.currentTimeMillis());

        System.out.println(tree.interpret(tree.getRoot(), ""));

    }
}