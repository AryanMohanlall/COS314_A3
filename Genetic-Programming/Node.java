public class Node {
    public Node left;
    public Node right;
    public FunctionSet function;
    public Integer terminal;

    public Node(Node left, Node right, FunctionSet fs, Integer t){
        this.left = left;
        this.right = right;
        this.function = fs;
        this.terminal = t;
    }
}
