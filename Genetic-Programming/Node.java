public class Node {
    public Node left;
    public Node right;
    public FunctionSet function;
    public float terminal;

    public Node(Node left, Node right, FunctionSet fs, float t){
        this.left = left;
        this.right = right;
        this.function = fs;
        this.terminal = t;
    }

    public Node getLeft(){
        return this.left;
    }

    public Node getRight(){
        return this.right;
    }

    public FunctionSet getFuncion(){
        return this.function;
    }

    public float getTerminal(){
        return this.terminal;
    }

    public void setLeft(Node l){
        this.left = l;
    }

    public void setRight(Node r){
        this.right = r;
    }

    public void setFunction(FunctionSet f){
        this.function = f;
    }

    public void setTerminal(float t){
        this.terminal = t;
    }

}
