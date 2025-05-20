class Main{
    public static void main(String[] args) {
        Node plus = new FunctionNode(null, null, FunctionSet.PLUS);
        Node minus = new FunctionNode(null, null, FunctionSet.MINUS);
        Node multiply = new FunctionNode(null, null, FunctionSet.MULTIPLY);
        Node divide = new FunctionNode(null, null, FunctionSet.DIVIDE);

        SyntaxTree tree = new SyntaxTree(plus);
        tree.append(divide, tree.getRoot());
        tree.append(multiply, tree.getRoot());
        tree.append(minus, tree.getRoot());

        System.out.println(tree.interpret(tree.getRoot(), ""));

        tree.buildSyntaxTree();
    }
}